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

package com.ndanh.mytranslator.app;

import android.app.Application;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatDelegate;

import com.facebook.stetho.Stetho;
import com.ndanh.mytranslator.dagger.AppComponent;
import com.ndanh.mytranslator.dagger.AppModule;
import com.ndanh.mytranslator.dagger.DaggerAppComponent;
import com.ndanh.mytranslator.model.Language;
import com.ndanh.mytranslator.screen.settings.SettingActivity;
import com.ndanh.mytranslator.util.LocaleHelper;

import org.opencv.android.OpenCVLoader;

import javax.inject.Inject;

import static com.ndanh.mytranslator.screen.settings.SettingActivity.PREF_DISPLAY_LANG;


public class TranslateApplication extends Application {

    @Inject
    SharedPreferences mPreferences;
    private AppComponent appComponent;
    private Language language;

    public AppComponent getAppComponent() {
        return appComponent;
    }

    static {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);

        if (!OpenCVLoader.initDebug()) {
            // Handle initialization error
        } else {
            System.loadLibrary("opencv_java3");
            System.loadLibrary("opencvocr");
        }
    }


    @Override
    public void onCreate() {
        super.onCreate();
        appComponent = initDagger(this);
        Stetho.initializeWithDefaults(this);
        appComponent.inject(this);
        language = Language.fromShort(mPreferences.getString(PREF_DISPLAY_LANG, "eng"));
        LocaleHelper.setLocale(this, language.getPhone());
    }

    protected AppComponent initDagger(TranslateApplication application) {
        return DaggerAppComponent.builder()
                .appModule(new AppModule(application))
                .build();
    }
}
