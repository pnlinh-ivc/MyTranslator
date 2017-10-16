package com.ndanh.mytranslator.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

import com.ndanh.mytranslator.R;
import com.ndanh.mytranslator.app.Config;

/**
 * Created by ndanh on 5/15/2017.
 */

public class Setting {
    private static Context context;

    public final static int NONE_SCREEN_MODE = 0;
    public final static int CAMERA_SCREEN_MODE = 1;
    public final static int TEXT_SCREEN_MODE = 2;
    public final static int VOICE_SCREEN_MODE = 3;

    public static void initSetting(final Context ct) {
        context = ct;
    }

    private
    @DrawableRes
    int iconSetting;
    private
    @StringRes
    int textSetting;
    private
    @DrawableRes
    int checkBoxSetting;
    private boolean isSet;

    public boolean isSet() {
        return isSet;
    }

    public void setSet(boolean set) {
        isSet = set;
    }

    public
    @DrawableRes
    int getIconSetting() {
        return iconSetting;
    }

    public int getTextSetting() {
        return textSetting;
    }

    public
    @DrawableRes
    int getCheckBoxSetting() {
        return checkBoxSetting;
    }

    public Setting(@DrawableRes int iconSetting, @StringRes int textSetting, @DrawableRes int checkBoxSetting, int startScreen) {
        this.iconSetting = iconSetting;
        this.textSetting = textSetting;
        this.checkBoxSetting = checkBoxSetting;
        int temp = 0;
        switch (textSetting) {
            case R.string.setting_camera_action:
                temp = CAMERA_SCREEN_MODE;
                break;
            case R.string.setting_keyboard_action:
                temp = TEXT_SCREEN_MODE;
                break;
            case R.string.setting_voice_action:
                temp = VOICE_SCREEN_MODE;
                break;
            default:
                temp = NONE_SCREEN_MODE;
                break;

        }
        isSet = temp == startScreen;
    }

    public static void clean() {
        context = null;
    }

    public static void saveScreenMode(@StringRes int textSetting) {
        SharedPreferences pre = context.getSharedPreferences(Config.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pre.edit();
        switch (textSetting) {
            case R.string.setting_camera_action:
                editor.putInt(Config.SP_START_SCREEN_KEY, CAMERA_SCREEN_MODE);
                break;
            case R.string.setting_keyboard_action:
                editor.putInt(Config.SP_START_SCREEN_KEY, TEXT_SCREEN_MODE);
                break;
            case R.string.setting_voice_action:
                editor.putInt(Config.SP_START_SCREEN_KEY, VOICE_SCREEN_MODE);
                break;
            default:
                editor.putInt(Config.SP_START_SCREEN_KEY, NONE_SCREEN_MODE);
                break;

        }
        editor.apply();
    }

    public static int getScreenMode(SharedPreferences pre) {
        return pre.getInt(Config.SP_START_SCREEN_KEY, NONE_SCREEN_MODE);
    }
}
