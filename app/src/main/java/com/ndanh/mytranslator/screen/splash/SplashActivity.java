package com.ndanh.mytranslator.screen.splash;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import com.ndanh.mytranslator.R;
import com.ndanh.mytranslator.app.TranslateApplication;
import com.ndanh.mytranslator.base.BaseActivity;
import com.ndanh.mytranslator.model.Setting;
import com.ndanh.mytranslator.screen.camera.CameraActivity;
import com.ndanh.mytranslator.screen.text.TextActivity;
import com.ndanh.mytranslator.screen.voice.VoiceActivity;

import javax.inject.Inject;

import butterknife.ButterKnife;


public class SplashActivity extends BaseActivity {
    @Inject
    SharedPreferences sharedPreferences;

    @Override
    protected void initView() {
        new AsyncLoadXMLFeed().execute();
        setContentView(R.layout.activity_splash);
        ((TranslateApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
    }

    private class AsyncLoadXMLFeed extends AsyncTask<Void, Integer, Void> {

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected Void doInBackground(Void... voids){
            return null;
        }

        @Override
        protected void onPostExecute(Void params){
            int screenMode = Setting.getScreenMode (sharedPreferences);
            Intent intent;
            switch (screenMode){
                case Setting.CAMERA_SCREEN_MODE:
                    intent = new Intent(SplashActivity.this, CameraActivity.class);
                    break;
                case Setting.TEXT_SCREEN_MODE:
                    intent = new Intent(SplashActivity.this, TextActivity.class);
                    break;
                case Setting.VOICE_SCREEN_MODE:
                    intent = new Intent(SplashActivity.this, VoiceActivity.class);
                    break;
                default:
                    intent = new Intent(SplashActivity.this, CameraActivity.class);
                    break;
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
    public static void start(Context context) {
      Intent starter = new Intent(context, SplashActivity.class);
      context.startActivity(starter);
    }
}

