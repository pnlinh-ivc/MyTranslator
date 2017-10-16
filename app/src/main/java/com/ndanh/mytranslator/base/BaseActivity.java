package com.ndanh.mytranslator.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;


/**
 * Created by ndanh on 5/15/2017.
 */

public abstract class BaseActivity extends AppCompatActivity {

    protected abstract void initView();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        initView();
    }
}
