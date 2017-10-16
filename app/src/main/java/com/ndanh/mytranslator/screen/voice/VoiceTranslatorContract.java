package com.ndanh.mytranslator.screen.voice;


import com.ndanh.mytranslator.modulesimpl.GoogleTranslateHelper;

/**
 * Created by ndanh on 4/28/2017.
 */

public interface VoiceTranslatorContract {
    interface VoiceTranslatorView {
        void displayResultTranslate(String result, GoogleTranslateHelper.RequestModel model);

        void displayMessage(String msg);
    }

    interface VoiceTranslatorPresenter {
        void setView(VoiceTranslatorView view);

        void doTranslate(String what, String from, String to);
    }
}
