package com.ndanh.mytranslator.modulesimpl;

import android.content.Context;
import android.speech.SpeechRecognizer;

import com.ndanh.mytranslator.services.IVoiceDetect;

import static android.speech.SpeechRecognizer.createSpeechRecognizer;

/**
 * Created by ndanh on 3/31/2017.
 */

public final class VoiceDetectModuleImpl implements IVoiceDetect {
    private SpeechRecognizer voiceRecognize;
    private Context mContext;
    public VoiceDetectModuleImpl(final Context context) {
        this.mContext = context;
        voiceRecognize = createSpeechRecognizer(context);
    }

    private VoiceDetectModuleImpl() {

    }

    @Override
    public void release() {
        voiceRecognize.destroy ();
    }
}
