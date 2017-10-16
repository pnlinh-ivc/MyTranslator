package com.ndanh.mytranslator.util;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Environment;
import android.util.Log;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.io.File;
import java.io.FileOutputStream;


public class OpenCVUtils {
    private static final String TAG = OpenCVUtils.class.getSimpleName();

    public static void RotateMat(Mat matsrc, int rotation) {
        int flipTime = rotation / 90;
        Core.transpose(matsrc, matsrc);
        Core.flip(matsrc, matsrc, flipTime);
    }

    public static void saveFile(byte[] data, Camera mCamera) {
        try {
            Log.e(TAG, String.format("saveFile: "));
            Camera.Parameters parameters = mCamera.getParameters();
            Camera.Size size = parameters.getPreviewSize();
            YuvImage image = new YuvImage(data, parameters.getPreviewFormat(),
                    size.width, size.height, null);
            File file = new File(Environment.getExternalStorageDirectory().getPath() + "/a.png");
            FileOutputStream filecon = new FileOutputStream(file);
            image.compressToJpeg(new Rect(0, 0, image.getWidth(), image.getHeight()), 90, filecon);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

//if (!save && System.currentTimeMillis() - curTime > 2000) {
//        // OpenCVUtils.saveFile(data,mCamera);
//        Log.e(TAG, String.format("onPreviewFrame: "));
//
//        Camera.Size size = mCamera.getParameters().getPreviewSize();
//        Mat mYuv = new Mat(size.height + (size.height / 2), size.width, CvType.CV_8UC1);
//        mYuv.put(0, 0, data);
//
//        Mat mRgba = new Mat();
//        Imgproc.cvtColor(mYuv, mRgba, Imgproc.COLOR_YUV2BGR_NV12, 4);
//        OpenCVUtils.RotateMat(mRgba, 90);
//        Bitmap bitmap = Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.ARGB_8888);
//        Utils.matToBitmap(mRgba, bitmap);
//        mView.setBitmap(bitmap);
//        save = true;
//    }


//        if (!save && System.currentTimeMillis() - curTime > 2000) {
//            // OpenCVUtils.saveFile(data,mCamera);
//            Log.e(TAG, String.format("onPreviewFrame: "));
//
//            Camera.Size size = mCamera.getParameters().getPreviewSize();
//            Mat mYuv = new Mat(size.height + (size.height / 2), size.width, CvType.CV_8UC1);
//            mYuv.put(0, 0, data);
//            Log.e(TAG, String.format("onPreviewFrame: %s %s", size.height , size.width));
//
//            Mat mRgba = new Mat();
//            Imgproc.cvtColor(mYuv, mRgba, Imgproc.COLOR_YUV2BGR_NV12, 4);
//            OpenCVUtils.RotateMat(mRgba, 90);
//            Bitmap bitmap = Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.ARGB_8888);
//            Utils.matToBitmap(mRgba, bitmap);
//            mView.setBitmap(bitmap);
//            save = true;
//        }
//        mCamera.addCallbackBuffer(data);
