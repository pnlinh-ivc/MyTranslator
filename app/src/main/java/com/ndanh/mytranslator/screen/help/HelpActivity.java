package com.ndanh.mytranslator.screen.help;

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

public class HelpActivity extends BaseActivity {
    @BindView( R.id.webview )
    WebView webView;

    @Override
    protected void initView() {
        setContentView ( R.layout.activity_help );
        ButterKnife.bind ( this );
        webView.loadUrl ( "file:///android_asset/html/help.html" );
    }

    @OnClick(R.id.action_back)
    public void back(View v){
        finish ();
    }

    public static void start(Context context, int... flags) {
      Intent starter = new Intent(context, HelpActivity.class);
        for (  int flag : flags ) {
            starter.addFlags ( flag );
        }
      context.startActivity(starter);
    }
}
