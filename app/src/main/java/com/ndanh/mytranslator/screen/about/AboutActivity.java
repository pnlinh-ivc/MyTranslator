package com.ndanh.mytranslator.screen.about;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.ndanh.mytranslator.R;
import com.ndanh.mytranslator.base.BaseActivity;
import com.ndanh.mytranslator.screen.privacypolicy.PrivacyPolicyActivity;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class AboutActivity extends BaseActivity {

    @Override
    protected void initView() {
        setContentView ( R.layout.activity_about );
        ButterKnife.bind ( this );
    }

    @OnClick(R.id.action_back)
    public void back(View v){
        finish ();
    }

    @OnClick(R.id.txt_privacy_policy)
    public void transferToPrivacyPolicy(View v){
        PrivacyPolicyActivity.start ( this );
    }

    public static void start(Context context) {
      Intent starter = new Intent(context, AboutActivity.class);
      context.startActivity(starter);
    }

}
