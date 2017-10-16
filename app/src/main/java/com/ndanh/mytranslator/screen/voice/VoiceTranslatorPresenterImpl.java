package com.ndanh.mytranslator.screen.voice;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.ndanh.mytranslator.app.TranslateApplication;
import com.ndanh.mytranslator.model.History;
import com.ndanh.mytranslator.model.TranslateGCloud.TranslatorResponse;
import com.ndanh.mytranslator.modulesimpl.GoogleTranslateHelper;
import com.ndanh.mytranslator.screen.text.TextTranslatorContract;
import com.ndanh.mytranslator.screen.text.TextTranslatorPresenterImpl;
import com.ndanh.mytranslator.services.HistoryDao;
import com.ndanh.mytranslator.services.ITranslate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

/**
 * Created by ndanh on 4/28/2017.
 */

public class VoiceTranslatorPresenterImpl implements VoiceTranslatorContract.VoiceTranslatorPresenter {
    private static final String TAG = "dd";

    @Inject
    HistoryDao historyDao;
    @Inject
    ITranslate mTranslator;

    private History mLastHistory, mPendingHistory;
    private VoiceTranslatorContract.VoiceTranslatorView view;

    private ITranslate.OnTranslateListener mTranslateListener = new ITranslate.OnTranslateListener() {
        @Override
        public void onSuccess(TranslatorResponse result, GoogleTranslateHelper.RequestModel requestModel) {
            Log.e(TAG, "onSuccess(): result = [" + result.getData().megerAll() + "], requestModel = [" + requestModel + "]");
            String resultString = result.getData().megerAll();
            view.displayResultTranslate(resultString, requestModel);
            mLastHistory = mPendingHistory.clone();
            mLastHistory.setTimestamp(System.currentTimeMillis());
            mLastHistory.setTextdes(resultString);
            historyDao.addHistory(mLastHistory);
        }

        @Override
        public void onFailed(String msg) {
            view.displayMessage(msg);
        }
    };

    public VoiceTranslatorPresenterImpl(Context context) {
        ((TranslateApplication) context).getAppComponent().inject(this);
        mTranslator.setOnTranslateListener(mTranslateListener);
    }


    @Override
    public void doTranslate(String what, String from, String to) {
        Log.e(TAG, "doTranslate(): what = [" + what + "], from = [" + from + "], to = [" + to + "]");
        List<String> list = new ArrayList<>();
        list.add(what);
        GoogleTranslateHelper.RequestModel requestModel = new GoogleTranslateHelper.RequestModel(list, from, to);

        if (TextUtils.equals(from, to)) {
            view.displayResultTranslate(what, requestModel);
            return;
        }

        boolean needCallApi = true;
        if (mPendingHistory != null) {
            if (mPendingHistory.equals(what, from, to)) {
                if (mPendingHistory.sameRequest(mLastHistory)) {
                    needCallApi = false;
                }
            }
        }
        if (needCallApi) {
            mPendingHistory = new History(what, from, to);
            mTranslator.translate(list, from, to);
        } else view.displayResultTranslate(mLastHistory.getTextdes(), requestModel);
    }

    @Override
    public void setView(VoiceTranslatorContract.VoiceTranslatorView view) {
        this.view = view;
    }
}
