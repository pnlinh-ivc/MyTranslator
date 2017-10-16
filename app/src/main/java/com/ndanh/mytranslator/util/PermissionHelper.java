package com.ndanh.mytranslator.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

import com.annimon.stream.function.Function;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.permission;

/**
 * Created by ndanh on 4/28/2017.
 */

public class PermissionHelper {

    public static final int REQUEST_PERMISSION_CODE = 100;

    public static void requestPermission(Activity context, String... permissions){
        List<String> lst = new ArrayList<String> (  );

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            if (PermissionHelper.shouldAskPermission(context, permissions)){

                ActivityCompat.requestPermissions(context, lst.toArray( new String[lst.size ()]), REQUEST_PERMISSION_CODE);
            }

        }
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    private static boolean shouldAskPermission() {
        return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
    }

    public static boolean shouldAskPermission(Context context, String[] permission){
        if (shouldAskPermission()) {
            for (String item : permission) {
                int permissionResult = ActivityCompat.checkSelfPermission(context, item);
                if (permissionResult != PackageManager.PERMISSION_GRANTED) {
                    return true;
                }
            }
        }
        return false;
    }
}