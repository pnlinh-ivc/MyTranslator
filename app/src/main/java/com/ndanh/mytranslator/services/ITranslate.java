package com.ndanh.mytranslator.services;

import com.ndanh.mytranslator.model.TranslateGCloud.TranslatorResponse;
import com.ndanh.mytranslator.modulesimpl.GoogleTranslateHelper;

import java.util.List;

/**
 * Created by ndanh on 3/31/2017.
 */

public interface ITranslate {
    void translate(List<String> src, String srclang, String destLang);

    void setOnTranslateListener(OnTranslateListener listener);

    interface OnTranslateListener {
        void onSuccess(TranslatorResponse result, GoogleTranslateHelper.RequestModel requestModel);

        void onFailed(String msg);
    }
}
