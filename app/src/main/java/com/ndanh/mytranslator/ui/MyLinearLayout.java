package com.ndanh.mytranslator.ui;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.LinearLayout;

/**
 * Created by ndanh on 5/4/2017.
 */

public class MyLinearLayout extends LinearLayout {
    public MyLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super ( context, attrs );
    }

    private OnSoftKeyboardListener onSoftKeyboardListener;

    @Override
    protected void onMeasure(final int widthMeasureSpec, final int heightMeasureSpec) {

        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();

        int height = metrics.heightPixels;
        int width = metrics.widthPixels;
        if (onSoftKeyboardListener != null) {
            final int newSpec = MeasureSpec.getSize(heightMeasureSpec);
            if((height - newSpec) > (height /4) ){
                onSoftKeyboardListener.onShown();
            } else {
                onSoftKeyboardListener.onHidden();
            }
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public final void setOnSoftKeyboardListener(final OnSoftKeyboardListener listener) {
        this.onSoftKeyboardListener = listener;
    }

    public interface OnSoftKeyboardListener {
        void onShown();
        void onHidden();
    }

}
