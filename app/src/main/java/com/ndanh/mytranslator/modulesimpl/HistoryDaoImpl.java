package com.ndanh.mytranslator.modulesimpl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.ndanh.mytranslator.model.History;
import com.ndanh.mytranslator.services.HistoryDao;
import com.ndanh.mytranslator.util.SimpleSQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ndanh on 5/9/2017.
 */

public class HistoryDaoImpl implements HistoryDao {
    private static final String GET_ALL_QUERY = "select * from " + SimpleSQLiteOpenHelper.TABLE_RECORD + " order by " + SimpleSQLiteOpenHelper.KEY_TIMESTAMP +" DESC;";
    private static final String GET_QUERY = "select * from " + SimpleSQLiteOpenHelper.TABLE_RECORD + " where " + SimpleSQLiteOpenHelper.KEY_TIMESTAMP + "= ?;" ;
    private SQLiteOpenHelper _openHelper;

    public HistoryDaoImpl(Context context) {
        _openHelper = new SimpleSQLiteOpenHelper(context);
    }

    @Override
    public List<History> getAllRecords() {
        List<History> list = new ArrayList<History>();
        SQLiteDatabase db = _openHelper.getReadableDatabase();
        if (db == null) {
            return null;
        }
        Cursor cursor = db.rawQuery(GET_ALL_QUERY, null);

        if (cursor.moveToFirst()) {
            do {
                History history = new History();
                history.setTimestamp(cursor.getInt((cursor.getColumnIndex(SimpleSQLiteOpenHelper.KEY_TIMESTAMP))));
                history.setTextsrc(cursor.getString((cursor.getColumnIndex(SimpleSQLiteOpenHelper.KEY_TEXT_SRC))));
                history.setTextdes(cursor.getString((cursor.getColumnIndex(SimpleSQLiteOpenHelper.KEY_TEXT_DES))));
                history.setLangsrc(cursor.getString((cursor.getColumnIndex(SimpleSQLiteOpenHelper.KEY_LANG_SRC))));
                history.setLangdes(cursor.getString((cursor.getColumnIndex(SimpleSQLiteOpenHelper.KEY_LANG_DES))));

                list.add(history);
            } while (cursor.moveToNext());
        }
        if(!cursor.isClosed()) cursor.close();
        if(db.isOpen()) db.close();
        return list;
    }

    @Override
    public History getRecord(long timeStamp) {
        SQLiteDatabase db = _openHelper.getReadableDatabase();

        Cursor cursor = db.rawQuery(GET_QUERY, new String[] { String.valueOf(timeStamp) });

        if (cursor != null)
            cursor.moveToFirst();

        History history = new History();
        history.setTimestamp(cursor.getInt((cursor.getColumnIndex(SimpleSQLiteOpenHelper.KEY_TIMESTAMP))));
        history.setTextsrc(cursor.getString((cursor.getColumnIndex(SimpleSQLiteOpenHelper.KEY_TEXT_SRC))));
        history.setTextdes(cursor.getString((cursor.getColumnIndex(SimpleSQLiteOpenHelper.KEY_TEXT_DES))));
        history.setLangsrc(cursor.getString((cursor.getColumnIndex(SimpleSQLiteOpenHelper.KEY_LANG_SRC))));
        history.setLangdes(cursor.getString((cursor.getColumnIndex(SimpleSQLiteOpenHelper.KEY_LANG_DES))));

        if(!cursor.isClosed()) cursor.close();
        if(db.isOpen()) db.close();

        return history;
    }

    @Override
    public void deleteRecord(long timeStamp) {
        SQLiteDatabase db = _openHelper.getWritableDatabase();
        db.delete(SimpleSQLiteOpenHelper.TABLE_RECORD, SimpleSQLiteOpenHelper.KEY_TIMESTAMP + " = ?",
                new String[] { String.valueOf(timeStamp) });
        if(db.isOpen()) db.close();
    }

    @Override
    public long addHistory(History history) {
        SQLiteDatabase db = _openHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SimpleSQLiteOpenHelper.KEY_TIMESTAMP, history.getTimestamp());
        values.put(SimpleSQLiteOpenHelper.KEY_TEXT_SRC, history.getTextsrc());
        values.put(SimpleSQLiteOpenHelper.KEY_TEXT_DES, history.getTextdes());
        values.put(SimpleSQLiteOpenHelper.KEY_LANG_SRC, history.getLangsrc());
        values.put(SimpleSQLiteOpenHelper.KEY_LANG_DES, history.getLangdes());

        // insert row
        long history_timestamp = db.insert(SimpleSQLiteOpenHelper.TABLE_RECORD, null, values);
        if(db.isOpen()) db.close();
        return history_timestamp;
    }
}
