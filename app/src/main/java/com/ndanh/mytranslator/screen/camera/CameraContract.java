package com.ndanh.mytranslator.screen.camera;


import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Handler;
import android.os.Message;

import com.ndanh.mytranslator.model.DetectResult;
import com.ndanh.mytranslator.model.Language;
import com.ndanh.mytranslator.model.TranslateGCloud.TranslatorResponse;
import com.ndanh.mytranslator.modulesimpl.GoogleTranslateHelper;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import java.util.List;

/**
 * Created by ndanh on 4/18/2017.
 */

public interface CameraContract {
    interface CameraView {
        void displayResult(CameraFrame frame);

        void onError(String msg);

        void showMessage(String msg);

        void displayResult(List<String> response, List<String> request);

        void setBitmap(Bitmap bitmap);

        String getSrcLang();

        String getDestLang();

    }

    interface CameraPresenter {
        void setView(CameraView view);

        void stopRec();


        void translate(List<String> data, String srcLang, String destLang);

        void startDetector(String tessPath, String lang, String path01, String path02);

        void bufferCamera(Camera camera);
    }
}
