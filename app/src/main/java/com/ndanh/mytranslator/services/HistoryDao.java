package com.ndanh.mytranslator.services;

import com.ndanh.mytranslator.model.History;

import java.util.List;

public interface HistoryDao {
    List<History> getAllRecords();
    History getRecord(long timeStamp);
    void deleteRecord(long timeStamp);
    long addHistory(History history);
}