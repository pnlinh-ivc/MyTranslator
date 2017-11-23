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

import com.ndanh.mytranslator.app.TranslateApplication;
import com.ndanh.mytranslator.screen.camera.CameraActivity;
import com.ndanh.mytranslator.screen.camera.CameraPresenterImpl;
import com.ndanh.mytranslator.screen.history.HistoryActivity;
import com.ndanh.mytranslator.screen.history.HistoryPresenterImpl;
import com.ndanh.mytranslator.screen.settings.SettingActivity;
import com.ndanh.mytranslator.screen.splash.SplashActivity;
import com.ndanh.mytranslator.screen.text.TextActivity;
import com.ndanh.mytranslator.screen.text.TextTranslatorPresenterImpl;
import com.ndanh.mytranslator.screen.voice.VoiceActivity;
import com.ndanh.mytranslator.screen.voice.VoiceTranslatorPresenterImpl;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class, PresenterModule.class, NetworkModule.class, DatabaseModule.class, RecognizeModule.class})
public interface AppComponent {

    void inject(TranslateApplication application);
    void inject(CameraActivity target);
    void inject(HistoryActivity target);
    void inject(TextActivity target);
    void inject(SplashActivity target);
    void inject(VoiceActivity target);
    void inject(SettingActivity target);

    void inject(CameraPresenterImpl target);
    void inject(HistoryPresenterImpl target);
    void inject(TextTranslatorPresenterImpl target);
    void inject(VoiceTranslatorPresenterImpl target);

}
