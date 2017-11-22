package com.ndanh.mytranslator.screen.settings.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ndanh.mytranslator.R;
import com.ndanh.mytranslator.model.Language;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by pnlinh on 11/22/2017.
 */

public abstract class ChoseLanguageRecyclerAdapter extends RecyclerView.Adapter<ChoseLanguageRecyclerAdapter.MyViewHolder> {

    public ChoseLanguageRecyclerAdapter(Language language) {
        mLanguage = language;
    }

    private Language mLanguage;

    protected abstract void onItemClick(Language language);

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.item_recycler_change_language, parent, false
                )
        );
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        Language language = Language.getAll().get(position);
        holder.mIvFlag.setImageResource(language.getResId());
        holder.mTvLanguage.setText(language.getNormalName());
        holder.mIvCheck.setSelected(mLanguage == language);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick(Language.getAll().get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return Language.getAll().size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.iv_flag)
        ImageView mIvFlag;
        @BindView(R.id.tv_language)
        TextView mTvLanguage;
        @BindView(R.id.iv_check)
        ImageView mIvCheck;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }


    }

}
