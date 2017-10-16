package com.ndanh.mytranslator.dagger;

import android.content.Context;

import com.ndanh.mytranslator.modulesimpl.TextDetectModuleImpl;
import com.ndanh.mytranslator.modulesimpl.VoiceDetectModuleImpl;
import com.ndanh.mytranslator.services.ITextDetect;
import com.ndanh.mytranslator.services.IVoiceDetect;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by ndanh on 9/1/2017.
 */

@Module
public class RecognizeModule {
    @Provides
    @Singleton
    ITextDetect provideTextDetector(Context context) {
        return new TextDetectModuleImpl (context);
    }

    @Provides
    @Singleton
    IVoiceDetect provideVoiceDetect(Context context) {
        return new VoiceDetectModuleImpl (context);
    }
}
