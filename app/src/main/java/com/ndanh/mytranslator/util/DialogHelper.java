package com.ndanh.mytranslator.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.ndanh.mytranslator.R;

/**
 * Created by ndanh on 5/16/2017.
 */

public class DialogHelper {
    public static void confirm(final Context context, String message ,final OnDialogListener listener ){
        AlertDialog.Builder b=new AlertDialog.Builder(context);
        b.setTitle ( context.getString( R.string.dialog_confirm_title) );
        b.setMessage ( message );
        b.setPositiveButton (context.getString( R.string.dialog_action_dimiss_title), new DialogInterface. OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss ();
            }
        });
        b.setNegativeButton ( R.string.dialog_action_accept_title, new DialogInterface. OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                listener.onAccept ();
            }
        });
        b.create().show();
    }

    public static void alert(final Context context, String message ){
        AlertDialog.Builder b=new AlertDialog.Builder(context);
        b.setTitle ( context.getString( R.string.dialog_alert_title) );
        b.setMessage ( message );
        b.setPositiveButton( R.string.dialog_action_ok_title, new DialogInterface. OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss ();
            }
        });
        b.create().show();
    }
    public interface OnDialogListener{
        void onAccept();
    }
}
