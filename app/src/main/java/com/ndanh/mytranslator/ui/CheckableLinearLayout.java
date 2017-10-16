package com.ndanh.mytranslator.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.LinearLayout;

/**
 * Created by ndanh on 5/11/2017.
 */

public final class CheckableLinearLayout extends LinearLayout implements Checkable {
    private boolean isChecked;
    public CheckableLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super ( context, attrs );
    }

    @Override
    public void setChecked(boolean checked) {

    }

    @Override
    public boolean isChecked() {
        return false;
    }

    @Override
    public void toggle() {

    }
}
