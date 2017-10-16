package com.ndanh.mytranslator.screen.camera;

import android.graphics.Bitmap;
import android.os.Environment;

import com.ndanh.mytranslator.screen.camera.ui.MaskView;
import com.ndanh.mytranslator.util.OpenCVUtils;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.imgproc.Imgproc;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by ndanh on 9/8/2017.
 */
public class CameraFrame {

    private ByteBuffer byteBuffer;
    private Mat mYuvData, mRgba;
    private int previewFormat;

    public CameraFrame(byte[] data, int width, int height, int format) {
        mRgba = new Mat();
        this.byteBuffer = ByteBuffer.wrap(data);
        this.mYuvData = new Mat(height + (height / 2), width, CvType.CV_8UC1);
        previewFormat = format;
    }

    public ByteBuffer getByteBuffer() {
        return byteBuffer;
    }

    public void release() {
        mRgba.release();
        mYuvData.release();
        byteBuffer = null;
    }

    public Mat rgba() {
        mYuvData.put(0, 0, byteBuffer.array());
        Imgproc.cvtColor(mYuvData, mRgba, Imgproc.COLOR_YUV2BGR_NV12, 4);
        OpenCVUtils.RotateMat(mRgba, 90);
        return mRgba;
    }


    private Map<Rect, String> detectData;

    public List<MaskView.Data> getData() {
        List<MaskView.Data> resutl = new ArrayList<>();
        for (Map.Entry<Rect, String> item : detectData.entrySet()) {
            resutl.add(new MaskView.Data(
                    new android.graphics.Rect(item.getKey().x,
                            item.getKey().y,
                            item.getKey().x + item.getKey().width,
                            item.getKey().y + item.getKey().height),
                    item.getValue()));
        }
        return resutl;
    }
}

//try {
//    Bitmap bitmap = Bitmap.createBitmap(mRgba.cols(), mRgba.rows(), Bitmap.Config.ARGB_8888);
//    Utils.matToBitmap(mRgba, bitmap);
//    OutputStream stream = new FileOutputStream(Environment.getExternalStorageDirectory() + "/ttt/" + System.currentTimeMillis() + "aa.png");
//    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
//    } catch (Exception ex) {
//    ex.printStackTrace();
//}