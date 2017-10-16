package com.ndanh.mytranslator.base;

import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.view.MenuItem;
import android.view.View;

import com.ndanh.mytranslator.R;
import com.ndanh.mytranslator.screen.about.AboutActivity;
import com.ndanh.mytranslator.screen.camera.CameraActivity;
import com.ndanh.mytranslator.screen.help.HelpActivity;
import com.ndanh.mytranslator.screen.history.HistoryActivity;
import com.ndanh.mytranslator.screen.settings.SettingActivity;
import com.ndanh.mytranslator.screen.text.TextActivity;
import com.ndanh.mytranslator.screen.voice.VoiceActivity;

import butterknife.OnClick;

/**
 * Created by ndanh on 5/15/2017.
 */

public abstract class NavigatorFooterAndPermissionActivity extends PermissionActivity {

    @OnClick(R.id.nav_text_screen)
    protected void transferToTextScreen(View view) {
        TextActivity.start(NavigatorFooterAndPermissionActivity.this, Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        finish();
    }

    @OnClick(R.id.nav_voice_screen)
    protected void transferToVoiceScreen(View view) {
        Intent intent = new Intent(NavigatorFooterAndPermissionActivity.this, VoiceActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.nav_camera_screen)
    protected void transferToCameraScreen(View view) {
        Intent intent = new Intent(NavigatorFooterAndPermissionActivity.this, CameraActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.nav_other_pages)
    protected void doNavOtherPage(View view) {
        PopupMenu pum = new PopupMenu(this, findViewById(view.getId()));
        pum.inflate(R.menu.nav_menu);
        pum.setOnMenuItemClickListener(popupMenuNavigatorListener);
        pum.show();
    }

    private PopupMenu.OnMenuItemClickListener popupMenuNavigatorListener = new PopupMenu.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem item) {

            switch (item.getItemId()) {
                case R.id.action_history:
                    HistoryActivity.start(NavigatorFooterAndPermissionActivity.this);
                    break;
                case R.id.action_about:
                    AboutActivity.start(NavigatorFooterAndPermissionActivity.this);
                    break;
                case R.id.action_helps:
                    HelpActivity.start(NavigatorFooterAndPermissionActivity.this);
                    break;
                case R.id.action_settings:
                    SettingActivity.start(NavigatorFooterAndPermissionActivity.this);
                    break;
                default:
                    break;
            }
            return true;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        invisibleView();
    }

    public abstract void invisibleView();

}
