package com.ndanh.mytranslator.screen.settings.view;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ndanh.mytranslator.R;
import com.ndanh.mytranslator.model.Language;
import com.ndanh.mytranslator.screen.settings.adapter.ChoseLanguageRecyclerAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * A Dialog for chose language
 * Get list Language from @{@link Language#getAll()}
 */
public class ChoseLanguageDialog extends DialogFragment {
    private static final String BUNDLE_LANGUAGE = "lang";
    private static final String TAG = ChoseLanguageDialog.class.getSimpleName();

    @BindView(R.id.rcl_language)
    RecyclerView rclLanguage;
    Unbinder unbinder;

    public static ChoseLanguageDialog getInstance(Language language) {
        ChoseLanguageDialog fragment = new ChoseLanguageDialog();
        Bundle args = new Bundle();
        args.putString(BUNDLE_LANGUAGE, language.getShortName());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setStyle(R.style.AppCompatAlertDialogStyle, R.style.AppTheme);
        super.onCreate(savedInstanceState);

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(
                R.layout.dialog_chose_language, null, false
        );
        ButterKnife.bind(this, view);
        String lang = getArguments().getString(BUNDLE_LANGUAGE);
        rclLanguage.setAdapter(new ChoseLanguageRecyclerAdapter(Language.fromShort(lang)) {
            @Override
            protected void onItemClick(Language language) {
                Log.e(TAG, String.format("onItemClick: "));
            }
        });
        rclLanguage.setLayoutManager(new LinearLayoutManager(getContext()));
        return new AlertDialog.Builder(getContext())
                .setTitle(R.string.title_chose_language)
                .setView(view)
                .setPositiveButton(R.string.action_ok, null)
                .create();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_chose_language, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        String lang = getArguments().getString(BUNDLE_LANGUAGE);
//        rclLanguage.setAdapter(new ChoseLanguageRecyclerAdapter(Language.fromShort(lang)) {
//            @Override
//            protected void onItemClick(Language language) {
//                Log.e(TAG, String.format("onItemClick: "));
//            }
//        });
//        rclLanguage.setLayoutManager(new LinearLayoutManager(getContext()));
    }

//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }


}
