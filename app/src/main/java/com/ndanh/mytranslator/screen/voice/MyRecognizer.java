package com.ndanh.mytranslator.screen.voice;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;

import com.ndanh.mytranslator.model.Language;

/**
 * Created by pnlinh on 9/15/2017.
 */

public class MyRecognizer {
    private static final String TAG = MyRecognizer.class.getSimpleName();
    private SpeechRecognizer mRecognizer;
    private Intent mRecognizerIntent;
    private Language mLanguage;
    private boolean isReady;

    public MyRecognizer(Language language) {
        mLanguage = language;
    }

    public boolean isReady() {
        return isReady;
    }

    public void init(Context context, RecognitionListener listener) {
        createIntent(context);
        mRecognizer = SpeechRecognizer.createSpeechRecognizer(context);
        mRecognizer.setRecognitionListener(listener);
        isReady = true;
    }

    private void createIntent(Context context) {
        mRecognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, mLanguage.getLocateName());
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getPackageName());
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
    }

    public void changeLanguage(Language language) {
        mRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, language.getLocateName());
    }

    public void startListening() {
        mRecognizer.startListening(mRecognizerIntent);
    }

    public void stopListening() {
        Log.e(TAG, String.format("stopListening: "));
        mRecognizer.stopListening();
    }

    public void destroy() {
        mRecognizer.cancel();
        mRecognizer.destroy();
    }

    public static class SimpleRecognitionListener implements RecognitionListener {
        @Override
        public void onReadyForSpeech(Bundle params) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float rmsdB) {

        }

        @Override
        public void onBufferReceived(byte[] buffer) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int error) {

        }

        @Override
        public void onResults(Bundle results) {

        }

        @Override
        public void onPartialResults(Bundle partialResults) {

        }

        @Override
        public void onEvent(int eventType, Bundle params) {

        }
    }
}
