package com.ndanh.mytranslator.screen.text;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ndanh.mytranslator.R;
import com.ndanh.mytranslator.app.TranslateApplication;
import com.ndanh.mytranslator.base.NavigatorFooterAndPermissionActivity;
import com.ndanh.mytranslator.model.Language;
import com.ndanh.mytranslator.modulesimpl.GoogleTranslateHelper;
import com.ndanh.mytranslator.ui.MyLinearLayout;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class TextActivity extends NavigatorFooterAndPermissionActivity implements TextTranslatorContract.TextTranslatorView {
    private static final String TAG = TextActivity.class.getSimpleName();
    @BindView(R.id.iv_clear_text)
    ImageView ivClearText;
    @BindView(R.id.edt_translate)
    EditText edtTranslate;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.action_keyboard)
    RelativeLayout doTextKeyboard;
    @BindView(R.id.activity_text_translator)
    MyLinearLayout layout_parent;
    @BindView(R.id.navigator_action_bar)
    LinearLayout navigator_layout;
    @BindView(R.id.navigation_footer_text)
    RelativeLayout hiddenPanel;
    @BindView(R.id.action_choose_source)
    Button btnChangeSrcLang;
    @BindView(R.id.action_choose_dest)
    Button btnChangeDestLang;

    private Language langSrc = Language.ENGLISH, langDest = Language.JAPANESE;

    @Inject
    TextTranslatorContract.TextTranslatorPresenter presenter;

    private static final String STRING_EMPTY = "";

    @Override
    public void initView() {
        setContentView(R.layout.activity_text_translator);
        ((TranslateApplication) getApplication()).getAppComponent().inject(this);
        ButterKnife.bind(this);
        presenter.setView(this);
        updateLanguage();
    }

    private void updateLanguage() {
        btnChangeSrcLang.setText(langSrc.getNormalName());
        btnChangeDestLang.setText(langDest.getNormalName());
    }

    @Override
    protected void afterRequestPermission() {
        layout_parent.setOnSoftKeyboardListener(new MyLinearLayout.OnSoftKeyboardListener() {
            @Override
            public void onShown() {
                doTextKeyboard.setVisibility(View.GONE);
                navigator_layout.setVisibility(View.GONE);
            }

            @Override
            public void onHidden() {
                doTextKeyboard.setVisibility(View.VISIBLE);
                navigator_layout.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    protected String[] setPermissions() {
        return new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
    }

    @OnClick(R.id.action_keyboard)
    public void showSoftKeyboard(View view) {
        if (edtTranslate.requestFocus()) {
            InputMethodManager imm = (InputMethodManager)
                    getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(edtTranslate, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    @OnClick(R.id.iv_clear_text)
    public void doClearText(View view) {
        resetAll();
    }

    private void resetAll() {
        if (!TextUtils.isEmpty(edtTranslate.getText()))
            edtTranslate.setText(STRING_EMPTY);
        tvResult.setText(STRING_EMPTY);
        ivClearText.setVisibility(View.GONE);
    }

    @OnTextChanged(R.id.edt_translate)
    public void onTextChangeTranslate(CharSequence s, int start, int before, int count) {
        if (s.toString().length() == 0) {
            resetAll();
        } else {
            ivClearText.setVisibility(View.VISIBLE);
        }
    }

    @OnClick(R.id.btn_swap_language)
    public void changeLanguage(View v) {
        Language tempLang = langDest;
        langDest = langSrc;
        langSrc = tempLang;
        updateLanguage();

        String srcText = tvResult.getText().toString();
        if(!TextUtils.isEmpty(srcText)){
            edtTranslate.setText(srcText);
        }
        translate(null);
    }

    @OnClick(R.id.btn_translate)
    public void translate(View view) {
        String temp = edtTranslate.getText().toString();
        if (TextUtils.isEmpty(temp))
            return;
        this.presenter.doTranslate(temp, langSrc.getShortName(), langDest.getShortName());
    }

    @OnClick(R.id.action_choose_source)
    public void changeSource(View view) {
        showChangeLanguagePopup(view, true);
    }


    @OnClick(R.id.action_choose_dest)
    public void changeDest(View view) {
        showChangeLanguagePopup(view, false);
    }

    private void showChangeLanguagePopup(View view, final boolean isSource) {
        PopupMenu pum = new PopupMenu(this, view);
        List<Language> list = Language.getAll();
        for (Language language : list) {
            pum.getMenu().add(language.getNormalName());
        }
        pum.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Language language = Language.fromNormal(item.getTitle().toString());
                if (isSource)
                    langSrc = language;
                else
                    langDest = language;
                updateLanguage();
                translate(null);
                return true;
            }
        });
        pum.show();
    }


    @Override
    public void displayResultTranslate(String result, GoogleTranslateHelper.RequestModel model) {
        String srcText = edtTranslate.getText().toString();
        if (TextUtils.equals(srcText, model.query.get(0))
                && TextUtils.equals(langSrc.getShortName(), model.source)
                && TextUtils.equals(langDest.getShortName(), model.target)) {
            tvResult.setText(result);
        }
    }

    @Override
    public void displayMessage(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void invisibleView() {
        hiddenPanel.setVisibility(View.GONE);
    }

    public static void start(Context context, int... flags) {
        Intent starter = new Intent(context, TextActivity.class);
        for (int flag : flags) {
            starter.addFlags(flag);
        }
        context.startActivity(starter);
    }
}


