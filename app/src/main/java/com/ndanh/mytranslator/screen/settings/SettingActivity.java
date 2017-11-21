package com.ndanh.mytranslator.screen.settings;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.ndanh.mytranslator.R;
import com.ndanh.mytranslator.adapter.SettingAdapter;
import com.ndanh.mytranslator.app.Config;
import com.ndanh.mytranslator.app.TranslateApplication;
import com.ndanh.mytranslator.base.PermissionActivity;
import com.ndanh.mytranslator.model.Language;
import com.ndanh.mytranslator.model.Setting;
import com.ndanh.mytranslator.util.SimpleSQLiteOpenHelper;
import com.ndanh.mytranslator.util.DialogHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends PermissionActivity implements SettingAdapter.OnItemClickListener {
    private static final String PREF_DISPLAY_LANG = "display_lang";
    private static final String PREF_TRANSLATE_LANG = "translate_lang";
    @Inject
    SharedPreferences sharedPreferences;
    @BindView(R.id.action_about)
    ListView lstSetting;
    @BindView(R.id.iv_flag_display)
    ImageView ivFlagDisplay;
    @BindView(R.id.iv_flag_translate)
    ImageView ivFlagTranslate;

    private SettingAdapter adapter;
    private Language mDisplayLang, mTranslateLang;

    @Override
    protected void initView() {
        setContentView(R.layout.activity_setting);
        ((TranslateApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        this.adapter = new SettingAdapter(getApplicationContext(), getListSetting(), this);
        lstSetting.setAdapter(adapter);
        initData();
    }

    private void initData() {
        mDisplayLang = Language.fromShort(sharedPreferences.getString(PREF_DISPLAY_LANG, "eng"));
        mTranslateLang = Language.fromShort(sharedPreferences.getString(PREF_DISPLAY_LANG, "eng"));
        ivFlagDisplay.setImageResource(mDisplayLang.getResId());
        ivFlagTranslate.setImageResource(mTranslateLang.getResId());
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

    @Override
    public void onSelect(final Setting setting) {
        DialogHelper.confirm(SettingActivity.this, getString(R.string.setting_message_confirm_change_start_mode), new DialogHelper.OnDialogListener() {
            @Override
            public void onAccept() {
                adapter.changeStartMode(setting);
            }
        });
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, SettingActivity.class);
        context.startActivity(starter);
    }
}
