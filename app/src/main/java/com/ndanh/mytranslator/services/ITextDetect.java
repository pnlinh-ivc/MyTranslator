package com.ndanh.mytranslator.services;

import android.graphics.Bitmap;

import com.ndanh.mytranslator.model.DetectResult;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.List;

/**
 * Created by ndanh on 4/18/2017.
 */

public interface ITextDetect {
    void release();
    void detectBitmap(Bitmap bitmap);
    String detectMat(Mat mat);
    String detectbyte( byte[] dataArr , int w , int h, int bpp , int bpl);
    void setMat(Mat mat);
    String getData(Rect rect);
    void setLanguage(String lang);
    void setDetectBitmapCallback(DetectBitmapCallback callback);
    interface DetectBitmapCallback{
        void onSuccess(List<DetectResult> result);
    }
}
