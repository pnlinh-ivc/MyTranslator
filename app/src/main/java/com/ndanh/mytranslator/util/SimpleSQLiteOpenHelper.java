package com.ndanh.mytranslator.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ndanh on 5/9/2017.
 */

public class SimpleSQLiteOpenHelper extends SQLiteOpenHelper {

    // Logcat tag
    private static final String LOG = "SimpleSQLiteOpenHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "history.db";

    // Table Names
    public static final String TABLE_RECORD = "records";

    // NOTES Table - column name
    public static final String KEY_TIMESTAMP = "timestamp";
    public static final String KEY_LANG_SRC = "langsrc";
    public static final String KEY_LANG_DES = "langdes";
    public static final String KEY_TEXT_SRC = "textsrc";
    public static final String KEY_TEXT_DES = "textdes";
    public static final int MAX_COUNT = 100;

    public static final String TRIGGER_MAX_RECORD = " CREATE TRIGGER tr_removeold AFTER INSERT ON " + TABLE_RECORD +
                    " WHEN (SELECT COUNT(*) FROM " + TABLE_RECORD + ") > "+ MAX_COUNT +
                    " BEGIN " +
                    "    DELETE FROM  " + TABLE_RECORD + " where  " + KEY_TIMESTAMP + " IN (SELECT MIN( " + KEY_TIMESTAMP + ") FROM  " + TABLE_RECORD + ");" +
                    " END;";


    private static final String CREATE_TABLE_RECORD = "CREATE TABLE IF NOT EXISTS " + TABLE_RECORD + "("
            + KEY_TIMESTAMP + " INTEGER PRIMARY KEY,"
            + KEY_LANG_SRC + " TEXT,"
            + KEY_LANG_DES + " TEXT,"
            + KEY_TEXT_SRC + " TEXT,"
            + KEY_TEXT_DES + " TEXT"
            + ")";



    public SimpleSQLiteOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_RECORD);
        db.execSQL ( TRIGGER_MAX_RECORD );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORD);
        // create new tables
        onCreate(db);
    }
}
