package com.ndanh.mytranslator.screen.text;

import android.content.Context;

import com.ndanh.mytranslator.modulesimpl.GoogleTranslateHelper;

import java.util.List;

/**
 * Created by ndanh on 3/30/2017.
 */

public interface TextTranslatorContract {
    interface TextTranslatorView {
        void displayResultTranslate(String result, GoogleTranslateHelper.RequestModel model);
        void displayMessage(String msg);
    }

    interface TextTranslatorPresenter {
        void doTranslate(String what, String from, String to);

        void setView(TextTranslatorView view);
    }
}
