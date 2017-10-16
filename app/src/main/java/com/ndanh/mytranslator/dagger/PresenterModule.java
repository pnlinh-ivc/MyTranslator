/*
 * Copyright (c) 2016 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.ndanh.mytranslator.dagger;

import android.content.Context;

import com.ndanh.mytranslator.screen.camera.CameraContract;
import com.ndanh.mytranslator.screen.camera.CameraPresenterImpl;
import com.ndanh.mytranslator.screen.history.HistoryContract;
import com.ndanh.mytranslator.screen.history.HistoryPresenterImpl;
import com.ndanh.mytranslator.screen.text.TextTranslatorContract;
import com.ndanh.mytranslator.screen.text.TextTranslatorPresenterImpl;
import com.ndanh.mytranslator.screen.voice.VoiceTranslatorContract;
import com.ndanh.mytranslator.screen.voice.VoiceTranslatorPresenterImpl;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class PresenterModule {

  @Provides
  CameraContract.CameraPresenter provideCameraContractICameraPresenter(Context context) {
    return new CameraPresenterImpl (context);
  }

  @Provides
  @Singleton
  TextTranslatorContract.TextTranslatorPresenter provideTextTranslatorPresenter(Context context) {
    return new TextTranslatorPresenterImpl (context);
  }


  @Provides
  @Singleton
  VoiceTranslatorContract.VoiceTranslatorPresenter provideVoiceTranslatorPresenter(Context context) {
    return new VoiceTranslatorPresenterImpl (context);
  }

  @Provides
  @Singleton
  HistoryContract.HistoryPresenter provideHistoryPresenter(Context context) {
    return new HistoryPresenterImpl (context);
  }


}
