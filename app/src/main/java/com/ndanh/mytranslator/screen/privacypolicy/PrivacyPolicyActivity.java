package com.ndanh.mytranslator.screen.privacypolicy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.ndanh.mytranslator.R;
import com.ndanh.mytranslator.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PrivacyPolicyActivity extends BaseActivity {
    @BindView ( R.id.webview )
    WebView webView;

    @Override
    protected void initView() {
        setContentView ( R.layout.activity_privacy_policy );
        ButterKnife.bind ( this );
        webView.loadUrl ( "file:///android_asset/html/privacy.html" );
    }

    @OnClick(R.id.action_back)
    public void back(View v){
        finish ();
    }

    public static void start(Context context) {
      Intent starter = new Intent(context, PrivacyPolicyActivity.class);
      context.startActivity(starter);
    }
}
