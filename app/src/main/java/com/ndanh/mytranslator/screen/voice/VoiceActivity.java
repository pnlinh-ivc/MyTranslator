package com.ndanh.mytranslator.screen.voice;

import java.util.ArrayList;
import java.util.List;

import android.Manifest;
import android.content.Context;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ToggleButton;

import com.ndanh.mytranslator.R;
import com.ndanh.mytranslator.app.TranslateApplication;
import com.ndanh.mytranslator.base.NavigatorFooterAndPermissionActivity;
import com.ndanh.mytranslator.model.Language;
import com.ndanh.mytranslator.modulesimpl.GoogleTranslateHelper;
import com.ndanh.mytranslator.util.CLog;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.speech.SpeechRecognizer.createSpeechRecognizer;

public class VoiceActivity extends NavigatorFooterAndPermissionActivity implements VoiceTranslatorContract.VoiceTranslatorView {
    private static final String TAG = VoiceActivity.class.getSimpleName();
    private static final String LOG_TAG = "VoiceActivity";
    private static final String STRING_EMPTY = "";
    @Inject
    VoiceTranslatorContract.VoiceTranslatorPresenter presenter;
    private MyRecognizer mMyRecognizer;
    private Language langSrc = Language.ENGLISH, langDest = Language.JAPANESE;

    @BindView(R.id.text_source)
    TextView textSource;
    @BindView(R.id.edt_translate)
    TextView textTranslate;
    @BindView(R.id.micro_action)
    ToggleButton toggleButton;
    @BindView(R.id.micro_background)
    RelativeLayout microBG;
    @BindView(R.id.navigation_footer_voice)
    RelativeLayout hiddenPanel;
    @BindView(R.id.action_choose_source)
    Button comboboxSource;
    @BindView(R.id.action_choose_dest)
    Button comboboxDest;

    private RecognitionListener recognitionListener = new MyRecognizer.SimpleRecognitionListener() {
        @Override
        public void onEndOfSpeech() {
            toggleButton.setChecked(false);
        }

        @Override
        public void onError(int errorCode) {
            Log.e(TAG, "onError() called with: errorCode = [" + errorCode + "]");
            String errorMessage = getErrorText(errorCode);
            CLog.d(LOG_TAG, "FAILED " + errorMessage);
            textSource.setText(errorMessage);
            textTranslate.setText(STRING_EMPTY);
            toggleButton.setChecked(false);
            //  mMyRecognizer.stopListening();
        }

        @Override
        public void onResults(Bundle results) {
            CLog.i(LOG_TAG, "onResults");
            ArrayList<String> matches = results
                    .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String text = (matches != null && matches.size() > 0) ? matches.get(0) : STRING_EMPTY;
            textSource.setText(text);
            translate(null);
        }

    };

    private OnCheckedChangeListener toggleButtonListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Log.e(TAG, String.format("onCheckedChanged: "));
            if (!mMyRecognizer.isReady())
                return;
            if (isChecked) {
                microBG.setSelected(true);
                mMyRecognizer.startListening();
            } else {
                microBG.setSelected(false);
                mMyRecognizer.stopListening();
            }
        }
    };


    //TODO General
    private void updateLanguage() {
        comboboxSource.setText(langSrc.getNormalName());
        comboboxDest.setText(langDest.getNormalName());
    }

    private String getErrorText(int errorCode) {
        switch (errorCode) {
            case SpeechRecognizer.ERROR_AUDIO:
                return getString(R.string.voice_error_audio);
            case SpeechRecognizer.ERROR_CLIENT:
                return getString(R.string.voice_error_client);
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                return getString(R.string.voice_error_insufficient_permissions);
            case SpeechRecognizer.ERROR_NETWORK:
                return getString(R.string.voice_error_network);
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                return getString(R.string.voice_error_network_timeout);
            case SpeechRecognizer.ERROR_NO_MATCH:
                return getString(R.string.voice_error_no_match);
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                return getString(R.string.voice_error_recognizer_busy);
            case SpeechRecognizer.ERROR_SERVER:
                return getString(R.string.voice_error_server);
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                return getString(R.string.voice_error_speech_timeout);
            default:
                return getString(R.string.voice_error_default);
        }
    }

    @Override
    public void initView() {
        setContentView(R.layout.activity_voice_translator);
        ((TranslateApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        mMyRecognizer = new MyRecognizer(langSrc);
        updateLanguage();
        presenter.setView(this);

    }

    @Override
    protected void afterRequestPermission() {
        mMyRecognizer.init(this, recognitionListener);
        toggleButton.setOnCheckedChangeListener(toggleButtonListener);
    }

    @Override
    protected String[] setPermissions() {
        return new String[]{Manifest.permission.RECORD_AUDIO, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @Override
    public void invisibleView() {
        hiddenPanel.setVisibility(View.GONE);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, VoiceActivity.class);
        context.startActivity(starter);
    }


    //TODO View event
    @OnClick(R.id.btn_swap_language)
    public void changeLanguage(View v) {
        Language tempLang = langDest;
        langDest = langSrc;
        langSrc = tempLang;
        updateLanguage();
        mMyRecognizer.changeLanguage(langSrc);

        String srcText = textTranslate.getText().toString();
        if (!TextUtils.isEmpty(srcText)) {
            textSource.setText(srcText);
        }
        translate(null);
    }

    @OnClick(R.id.action_choose_source)
    public void changeSource(View view) {
        showChangeLanguagePopup(view, true);
    }

    @OnClick(R.id.action_choose_dest)
    public void changeDest(View view) {
        showChangeLanguagePopup(view, false);
    }

    private void showChangeLanguagePopup(View view, final boolean isSource) {
        PopupMenu pum = new PopupMenu(this, view);
        List<Language> list = Language.getAll();
        for (Language language : list) {
            pum.getMenu().add(language.getNormalName());
        }
        pum.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Language language = Language.fromNormal(item.getTitle().toString());
                boolean needUpdate = false;
                if (isSource) {
                    needUpdate = !langSrc.equals(language);
                    langSrc = language;
                } else {
                    langDest = language;
                }
                updateLanguage();

                if (needUpdate) {
                    mMyRecognizer.changeLanguage(langSrc);
                }
                translate(null);
                return true;
            }
        });
        pum.show();
    }

    public void translate(View view) {
        String temp = textSource.getText().toString();
        if (TextUtils.isEmpty(temp))
            return;
        this.presenter.doTranslate(temp, langSrc.getShortName(), langDest.getShortName());
    }


    //TODO Presenter methods
    @Override
    public void displayResultTranslate(String result, GoogleTranslateHelper.RequestModel model) {
        String srcText = textSource.getText().toString();
        if (TextUtils.equals(srcText, model.query.get(0))
                && TextUtils.equals(langSrc.getShortName(), model.source)
                && TextUtils.equals(langDest.getShortName(), model.target)) {
            textTranslate.setText(result);
        }
    }

    @Override
    public void displayMessage(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMyRecognizer.destroy();
    }
}