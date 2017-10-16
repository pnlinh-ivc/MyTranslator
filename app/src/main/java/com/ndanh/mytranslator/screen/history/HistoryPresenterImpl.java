package com.ndanh.mytranslator.screen.history;

import android.content.Context;

import com.ndanh.mytranslator.app.TranslateApplication;
import com.ndanh.mytranslator.services.HistoryDao;

import javax.inject.Inject;

/**
 * Created by ndanh on 5/10/2017.
 */

public class HistoryPresenterImpl implements HistoryContract.HistoryPresenter {

    @Inject
    HistoryDao historyDao;

    private HistoryContract.HistoryView view;

    public HistoryPresenterImpl(Context context) {
        ((TranslateApplication) context).getAppComponent().inject(this);
    }


    @Override
    public void getHistoryRecords() {
        view.showHistory(historyDao.getAllRecords());
    }

    @Override
    public void deleteHistory(long[] timestamps) {
        for (int i = 0, length = timestamps.length; i < length; i++) {
            historyDao.deleteRecord(timestamps[i]);
        }
    }

    @Override
    public void setView(HistoryContract.HistoryView historyView) {
        view = historyView;
    }
}
