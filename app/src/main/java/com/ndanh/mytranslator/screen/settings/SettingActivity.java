package com.ndanh.mytranslator.screen.settings;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.ndanh.mytranslator.R;
import com.ndanh.mytranslator.adapter.SettingAdapter;
import com.ndanh.mytranslator.app.TranslateApplication;
import com.ndanh.mytranslator.base.PermissionActivity;
import com.ndanh.mytranslator.model.Language;
import com.ndanh.mytranslator.model.Setting;
import com.ndanh.mytranslator.screen.settings.view.ChoseLanguageDialog;
import com.ndanh.mytranslator.screen.settings.view.ChoseLanguageDialog.Callback;
import com.ndanh.mytranslator.screen.settings.view.CustomDialog;
import com.ndanh.mytranslator.util.DialogHelper;
import com.ndanh.mytranslator.util.LocaleHelper;
import com.ndanh.mytranslator.util.SimpleSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends PermissionActivity {
    private static final String TAG = SettingActivity.class.getSimpleName();
    public static final String PREF_DISPLAY_LANG = "display_lang";
    public static final String PREF_TRANSLATE_LANG = "translate_lang";
    @Inject
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor mEditor;
    @BindView(R.id.lst_settings)
    ListView lstSetting;
    @BindView(R.id.iv_flag_display)
    ImageView ivFlagDisplay;
    @BindView(R.id.iv_flag_translate)
    ImageView ivFlagTranslate;
    @BindView(R.id.ll_language_display)
    LinearLayout llLanguageDisplay;
    @BindView(R.id.ll_language_translate)
    LinearLayout llLanguageTranslate;

    private List<Setting> mListSetting;
    private Callback mDisplayCallback, mTranslateCallback;


    private SettingAdapter adapter;
    private Language mDisplayLang, mTranslateLang;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_setting);
        ((TranslateApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        mListSetting = getListSetting();
        this.adapter = new SettingAdapter(this, R.layout.setting_item, mListSetting);
        lstSetting.setAdapter(adapter);
        initData();
    }

    private void initData() {
        mDisplayLang = Language.fromShort(sharedPreferences.getString(PREF_DISPLAY_LANG, "eng"));
        mTranslateLang = Language.fromShort(sharedPreferences.getString(PREF_TRANSLATE_LANG, "eng"));
        ivFlagDisplay.setImageResource(mDisplayLang.getResId());
        ivFlagTranslate.setImageResource(mTranslateLang.getResId());

        lstSetting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                DialogHelper.confirm(SettingActivity.this, getString(R.string.title_confirm_change_setting_mode), new DialogHelper.OnDialogListener() {
                    @Override
                    public void onAccept() {
                        adapter.changeStartMode(mListSetting.get(position));
                    }
                });
            }
        });
    }

    @Override
    protected String[] setPermissions() {
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @OnClick(R.id.iv_clear_text)
    public void deleteDataBase() {
        DialogHelper.confirm(SettingActivity.this, getString(R.string.setting_message_confirm_delete_history), new DialogHelper.OnDialogListener() {
            @Override
            public void onAccept() {
                SettingActivity.this.deleteDatabase(SimpleSQLiteOpenHelper.DATABASE_NAME);
            }
        });

    }

    @OnClick(R.id.action_back)
    public void back() {
        finish();
    }

    private List<Setting> getListSetting() {
        List<Setting> lstSetting = new ArrayList<>();
        Setting setting = new Setting(R.drawable.ic_setting_camera, R.string.setting_camera_action, R.drawable.checked, Setting.getScreenMode(sharedPreferences));
        lstSetting.add(setting);
        setting = new Setting(R.drawable.ic_setting_keyboard, R.string.setting_keyboard_action, R.drawable.checked, Setting.getScreenMode(sharedPreferences));
        lstSetting.add(setting);
        setting = new Setting(R.drawable.ic_setting_voice, R.string.setting_voice_action, R.drawable.checked, Setting.getScreenMode(sharedPreferences));
        lstSetting.add(setting);
        return lstSetting;
    }


    public static void start(Context context) {
        Intent starter = new Intent(context, SettingActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
    }

//    CustomDialog customDialog = new CustomDialog(this, R.style.AppCompatAlertDialogStyle);
//    customDialog.show();

    @OnClick(R.id.ll_language_display)
    public void onChangeDisplayClicked() {
        if (mDisplayCallback == null)
            mDisplayCallback = new Callback() {
                @Override
                public void onChange(Language language) {
                    if (mDisplayLang != language) {
                        mDisplayLang = language;
                        if (mEditor == null)
                            mEditor = sharedPreferences.edit();
                        mEditor.putString(PREF_DISPLAY_LANG, mDisplayLang.getShortName());
                        mEditor.apply();
                        ivFlagDisplay.setImageResource(mDisplayLang.getResId());
                        LocaleHelper.setLocale(SettingActivity.this, language.getPhone());
                        restartActivity();
                    }
                }
            };
        ChoseLanguageDialog.getInstance(mDisplayLang, mDisplayCallback)
                .show(getSupportFragmentManager(), "");
    }

    private void restartActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                recreate();
            }
        }, 300);
    }

    @OnClick(R.id.ll_language_translate)
    public void onChangeTranslateClicked() {
        if (mTranslateCallback == null)
            mTranslateCallback = new Callback() {
                @Override
                public void onChange(Language language) {
                    if (mTranslateLang != language) {
                        mTranslateLang = language;
                        if (mEditor == null)
                            mEditor = sharedPreferences.edit();
                        mEditor.putString(PREF_TRANSLATE_LANG, mTranslateLang.getShortName());
                        mEditor.apply();
                        ivFlagTranslate.setImageResource(mTranslateLang.getResId());
                    }
                }
            };
        ChoseLanguageDialog.getInstance(mTranslateLang, mTranslateCallback)
                .show(getSupportFragmentManager(), "");
    }

}
