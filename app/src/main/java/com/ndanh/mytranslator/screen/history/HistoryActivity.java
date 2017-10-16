package com.ndanh.mytranslator.screen.history;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;

import com.ndanh.mytranslator.R;
import com.ndanh.mytranslator.adapter.HistoryRecyclerViewApdater;
import com.ndanh.mytranslator.app.TranslateApplication;
import com.ndanh.mytranslator.base.BaseActivity;
import com.ndanh.mytranslator.base.PermissionActivity;
import com.ndanh.mytranslator.model.History;
import com.ndanh.mytranslator.util.DialogHelper;
import com.ndanh.mytranslator.util.PermissionHelper;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HistoryActivity extends PermissionActivity implements HistoryContract.HistoryView, Observer {
    @BindView(R.id.lst_history)
    RecyclerView recyclerView;
    @BindView(R.id.panel_delete)
    LinearLayout panel_delete;

    @Inject
    HistoryContract.HistoryPresenter presenter;

    private HistoryRecyclerViewApdater adapter;
    private LinearLayoutManager layoutManager;
    private DeleteProcessListener deleteProcessListener;

    public void initView() {
        setContentView(R.layout.activity_history);
        ((TranslateApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        DeleteMode.getInstance().addObserver(HistoryActivity.this);
        presenter.setView(this);
        adapter = new HistoryRecyclerViewApdater(getApplicationContext());
        adapter.addItems(new ArrayList<HistoryRecyclerViewApdater.HistoryView>());
        deleteProcessListener = adapter;
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.presenter.getHistoryRecords();
    }

    @Override
    protected void onPause() {
        super.onPause();
        DeleteMode.getInstance().off();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.presenter = null;
        adapter = null;
    }

    @Override
    public void showHistory(List<History> lstHistories) {
        List<HistoryRecyclerViewApdater.HistoryView> lstHis = new ArrayList<HistoryRecyclerViewApdater.HistoryView>();
        Iterator<History> it = lstHistories.iterator();
        while (it.hasNext()) {
            lstHis.add(new HistoryRecyclerViewApdater.HistoryView(it.next()));
        }
        this.adapter.addItems(lstHis);
    }

    @OnClick(R.id.garbage)
    public void doDelete(View v) {
        DialogHelper.confirm(HistoryActivity.this, getString(R.string.history_message_confirm_delete_history), new DialogHelper.OnDialogListener() {
            @Override
            public void onAccept() {
                deleteHistory();
            }
        });
    }

    private void deleteHistory() {
        long[] lstDelete = deleteProcessListener.beforeDelete();
        this.presenter.deleteHistory(lstDelete);
        DeleteMode.getInstance().off();
        this.presenter.getHistoryRecords();
    }

    @OnClick(R.id.action_back)
    public void back(View v) {
        finish();
    }

    @OnClick(R.id.checkbox)
    public void selectAll(View v) {
        deleteProcessListener.onSelectAll();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (DeleteMode.getInstance().isDeleteMode()) {
            panel_delete.setVisibility(View.VISIBLE);
        } else {
            panel_delete.setVisibility(View.GONE);
        }
    }

    @Override
    protected String[] setPermissions() {
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    public interface DeleteProcessListener {
        void onSelectAll();

        long[] beforeDelete();
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, HistoryActivity.class);
        context.startActivity(starter);
    }

}
