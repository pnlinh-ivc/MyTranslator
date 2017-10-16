package com.ndanh.mytranslator.base;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.ndanh.mytranslator.util.PermissionHelper;

/**
 * Created by ndanh on 5/15/2017.
 */

public abstract class PermissionActivity extends BaseActivity {
    private static final int REQUEST_NESS = 200;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (PermissionHelper.hasPermissions(this,  setPermissions( ))){
            afterRequestPermission ();
            return;
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M){
            PermissionHelper.requestPermission ( this, setPermissions( ) );
        } else{
            afterRequestPermission ();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_NESS) {
            for(int item : grantResults) {
                if(item != PackageManager.PERMISSION_GRANTED )
                    finish();
            }
        }
        afterRequestPermission ();
    }

    protected void afterRequestPermission( ){

    };
    protected abstract String[] setPermissions( );
}
