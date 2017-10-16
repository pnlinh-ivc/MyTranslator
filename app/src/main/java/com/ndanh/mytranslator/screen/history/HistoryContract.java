package com.ndanh.mytranslator.screen.history;

import android.content.Context;

import com.ndanh.mytranslator.model.History;

import java.util.List;

/**
 * Created by ndanh on 4/18/2017.
 */

public interface HistoryContract {
    interface HistoryView {
        void showHistory(List<History> lstHistories);
    }
    interface HistoryPresenter {
        void getHistoryRecords();
        void deleteHistory(long[] timestamps);
        void setView(HistoryView historyView);
    }
}
