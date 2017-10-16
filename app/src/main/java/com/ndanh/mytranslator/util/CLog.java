package com.ndanh.mytranslator.util;

import android.util.Log;

/**
 * Created by ndanh on 5/11/2017.
 */

public final class CLog {
    private static final boolean enableLog = true;

    public static void i(String tag, String message){
        if(enableLog){
            Log.i(tag, message);
        }
    }

    public static void d(String tag, String message){
        if(enableLog){
            Log.d(tag, message);
        }
    }
    public static void w(String tag, String message){
        if(enableLog){
            Log.w(tag, message);
        }
    }
    public static void e(String tag, String message){
        if(enableLog){
            Log.e(tag, message);
        }
    }
    public static void v(String tag, String message){
        if(enableLog){
            Log.v(tag, message);
        }
    }
    public static void i(String tag, String message, Throwable throwable){
        if(enableLog){
            Log.i(tag, message, throwable);
        }
    }

    public static void d(String tag, String message, Throwable throwable){
        if(enableLog){
            Log.d(tag, message, throwable);
        }
    }
    public static void w(String tag, String message, Throwable throwable){
        if(enableLog){
            Log.w(tag, message, throwable);
        }
    }
    public static void e(String tag, String message, Throwable throwable){
        if(enableLog){
            Log.e(tag, message, throwable);
        }
    }
    public static void v(String tag, String message, Throwable throwable){
        if(enableLog){
            Log.v(tag, message, throwable);
        }
    }
}
