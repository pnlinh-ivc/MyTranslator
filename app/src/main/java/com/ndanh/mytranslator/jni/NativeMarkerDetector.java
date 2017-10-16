package com.ndanh.mytranslator.jni;

import android.util.Log;

import com.ndanh.mytranslator.model.OCRResult;

import org.opencv.core.Mat;

public class NativeMarkerDetector {
    private static final String TAG = NativeMarkerDetector.class.getSimpleName();
    private long mNativeObj;

    public NativeMarkerDetector(String tessFolderPath, String lang, String nm1File, String nm2File) {
        mNativeObj = nativeCreateObject(tessFolderPath, lang, nm1File, nm2File, OCRResult.class.getCanonicalName().replace('.', '/'));
    }

    public OCRResult[] doOcr(Mat imageBgra) {
        Mat transformationsMat = new Mat();
        try {
            return nativeFindMarkers(mNativeObj, imageBgra.nativeObj, transformationsMat.nativeObj, 1);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new OCRResult[]{};
    }

    public void release() {
        nativeDestroyObject(mNativeObj);
        mNativeObj = 0;
    }

    private native long nativeCreateObject(String tessFolderPath, String lang, String nm1File, String nm2File, String className);

    private native OCRResult[] nativeFindMarkers(long thiz, long imageBgra, long transformations, float scale);

    private native void nativeDestroyObject(long thiz);
}
