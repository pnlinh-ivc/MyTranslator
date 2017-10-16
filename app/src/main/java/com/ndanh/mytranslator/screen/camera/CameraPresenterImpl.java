package com.ndanh.mytranslator.screen.camera;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.util.Log;

import com.ndanh.mytranslator.app.TranslateApplication;
import com.ndanh.mytranslator.jni.NativeMarkerDetector;
import com.ndanh.mytranslator.model.Language;
import com.ndanh.mytranslator.model.OCRResult;
import com.ndanh.mytranslator.model.TranslateGCloud.Translation;
import com.ndanh.mytranslator.model.TranslateGCloud.TranslatorResponse;
import com.ndanh.mytranslator.modulesimpl.GoogleTranslateHelper;
import com.ndanh.mytranslator.services.ITextDetect;
import com.ndanh.mytranslator.services.ITranslate;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

/**
 * Created by ndanh on 4/18/2017.
 */

public class CameraPresenterImpl implements CameraContract.CameraPresenter, Camera.PreviewCallback {
    private static final String TAG = "CameraPresenterImpl";
    @Inject
    ITranslate translateAPI;


    private CameraContract.CameraView mView;
    private Thread mDetectorThread;
    private DetectorRunnable mDetectorRunnable;
    private Map<byte[], CameraFrame> mBytesToByteBuffer = new HashMap<>();
    private Camera mCamera;


    public CameraPresenterImpl(Context context) {
        ((TranslateApplication) context).getAppComponent().inject(this);

        translateAPI.setOnTranslateListener(new ITranslate.OnTranslateListener() {
            @Override
            public void onSuccess(TranslatorResponse result, GoogleTranslateHelper.RequestModel requestModel) {
                List<Translation> reponse = result.getData().getTranslations();
                if (reponse == null)
                    return;
                List<OCRResult> listOCR = new ArrayList<>();
                for (Translation s : reponse) {
                    OCRResult result1 = parseString(s.getTranslatedText());
                    if (result1 != null)
                        listOCR.add(result1);
                }
                EventBus.getDefault().post(listOCR);
            }

            @Override
            public void onFailed(String msg) {
                mView.showMessage(msg);
            }
        });
    }

    @Override
    public void setView(CameraContract.CameraView view) {
        this.mView = view;
    }


    @Override
    public void stopRec() {
        Log.e(TAG, String.format("stopRec: "));
        if (mDetectorRunnable != null)
            mDetectorRunnable.stop();
        for (byte[] frame : mBytesToByteBuffer.keySet()) {
            mBytesToByteBuffer.get(frame).release();
        }
    }


    @Override
    public void translate(List<String> data, String srcLang, String destLang) {
        translateAPI.translate(data, srcLang, destLang);
    }


    @Override
    public void startDetector(String tessPath, String lang, String path01, String path02) {
        mDetectorRunnable = new DetectorRunnable(tessPath, lang, path01, path02);
        mDetectorThread = new Thread(mDetectorRunnable);
        mDetectorThread.start();
    }

    @Override
    public void bufferCamera(Camera camera) {
        mCamera = camera;
        mCamera.setPreviewCallbackWithBuffer(this);
        Camera.Size size = mCamera.getParameters().getPreviewSize();
        int previewFormat = mCamera.getParameters().getPreviewFormat();
        mCamera.addCallbackBuffer(createPreviewBuffer(size.width, size.height, previewFormat));
        mCamera.addCallbackBuffer(createPreviewBuffer(size.width, size.height, previewFormat));
        mCamera.addCallbackBuffer(createPreviewBuffer(size.width, size.height, previewFormat));
        mCamera.addCallbackBuffer(createPreviewBuffer(size.width, size.height, previewFormat));
    }

    private byte[] createPreviewBuffer(int width, int height, int previewFormat) {
        int bitsPerPixel = ImageFormat.getBitsPerPixel(previewFormat);
        long sizeInBits = width * height * bitsPerPixel;
        int bufferSize = (int) Math.ceil(sizeInBits / 8.0d) + 1;
        byte[] byteArray = new byte[bufferSize];
        CameraFrame frame = new CameraFrame(byteArray, width, height, previewFormat);
        mBytesToByteBuffer.put(byteArray, frame);
        return byteArray;
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        if (mDetectorRunnable != null) {
            mDetectorRunnable.setNextFrame(data);
        }
    }

    private void callTranslate(OCRResult[] listOCRResult) {
        try {
            //text_x_y_width_height
            String textFormat = "%s_%s_%s_%s_%s";
            List<String> listString = new ArrayList<>();
            if (listOCRResult != null && listOCRResult.length > 0) {
                for (OCRResult ocrResult : listOCRResult) {
                    String s = String.format(textFormat, ocrResult.text, ocrResult.x, ocrResult.y,
                            ocrResult.width, ocrResult.height);
                    listString.add(s);
                }
                translateAPI.translate(listString, mView.getSrcLang(), mView.getDestLang());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private OCRResult parseString(String text) {
        OCRResult result = new OCRResult();
        try {
            String[] split = text.split("_");
            result.text = split[0];
            result.x = Integer.valueOf(split[1]);
            result.y = Integer.valueOf(split[2]);
            result.width = Integer.valueOf(split[3]);
            result.height = Integer.valueOf(split[4]);
            return result;

        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    private class DetectorRunnable implements Runnable {
        private final Object mLock = new Object();
        private NativeMarkerDetector mNativeOcr;
        private boolean mActive = true;
        private CameraFrame mPendingFrameData;

        DetectorRunnable(String tessPath, String lang, String path01, String path02) {
            mNativeOcr = new NativeMarkerDetector(tessPath, lang, path01, path02);
        }

        void setNextFrame(byte[] data) {
            synchronized (mLock) {
                if (mPendingFrameData != null) {
                    mCamera.addCallbackBuffer(mPendingFrameData.getByteBuffer().array());
                    mPendingFrameData = null;
                }
                if (!mBytesToByteBuffer.containsKey(data)) {
                    return;
                }
                mPendingFrameData = mBytesToByteBuffer.get(data);
                mLock.notifyAll();
            }
        }

        void stop() {
            synchronized (mLock) {
                mNativeOcr.release();
                mActive = false;
                mLock.notifyAll();
            }
        }


        @Override
        public void run() {
            CameraFrame data;
            while (true) {
                synchronized (mLock) {
                    while (mActive && (mPendingFrameData == null)) {
                        try {
                            mLock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                            return;
                        }
                    }

                    if (!mActive) {
                        return;
                    }
                    data = mPendingFrameData;
                    mPendingFrameData = null;
                }
                try {
                    OCRResult[] list = mNativeOcr.doOcr(data.rgba());
                    callTranslate(list);
                } catch (Exception ex) {
                    ex.printStackTrace();
                } finally {
                    try {
                        mCamera.addCallbackBuffer(data.getByteBuffer().array());
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
    }


}
