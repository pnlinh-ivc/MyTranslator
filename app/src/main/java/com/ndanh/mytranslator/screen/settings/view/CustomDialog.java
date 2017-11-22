package com.ndanh.mytranslator.screen.settings.view;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;

import com.ndanh.mytranslator.R;

/**
 * Created by pnlinh on 11/22/2017.
 */

public class CustomDialog extends Dialog {
    public CustomDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        setContentView(R.layout.dialog_chose_language);

    }

    public CustomDialog(@NonNull Context context) {
        super(context);

    }

}
