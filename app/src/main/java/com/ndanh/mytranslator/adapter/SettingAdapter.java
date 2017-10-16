package com.ndanh.mytranslator.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.ndanh.mytranslator.R;
import com.ndanh.mytranslator.model.Setting;
import com.ndanh.mytranslator.util.DialogHelper;

import java.util.List;

/**
 * Created by ndanh on 5/15/2017.
 */

public class SettingAdapter extends BaseAdapter {

    private final Context context;
    private List<Setting> lstSetting;
    private OnItemClickListener itemClickListener;
    private int mSelectedItem;

    public SettingAdapter(final Context context, List<Setting> lstSetting, OnItemClickListener itemClickListener) {
        this.context = context;
        this.lstSetting = lstSetting;
        this.itemClickListener = itemClickListener;
    }

    @Override
    public int getCount() {
        return lstSetting.size();
    }

    @Override
    public Setting getItem(int position) {
        return lstSetting.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            ViewHolder viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.setting_item, parent, false);
            viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.setting_icon);
            viewHolder.tvSetting = (TextView) convertView.findViewById(R.id.setting_text);
            viewHolder.ivSelected = (ImageView) convertView.findViewById(R.id.setting_checkbox);
            convertView.setTag(viewHolder);
        }

        ViewHolder viewHolder = ((ViewHolder) convertView.getTag());

        viewHolder.tvSetting.setText(context.getString(lstSetting.get(position).getTextSetting()));
        viewHolder.ivIcon.setImageResource(lstSetting.get(position).getIconSetting());
        viewHolder.ivSelected.setImageResource(lstSetting.get(position).getCheckBoxSetting());
        if (lstSetting.get(position).isSet()) {
            viewHolder.ivSelected.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivSelected.setVisibility(View.GONE);
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onSelect(lstSetting.get(position));
            }
        });
        return convertView;
    }


    public interface OnItemClickListener {
        void onSelect(Setting setting);
    }

    public void changeStartMode(Setting setting) {
        Setting.initSetting(context);
        for (Setting item : lstSetting) {
            item.setSet(false);
        }
        setting.setSet(true);
        Setting.saveScreenMode(setting.getTextSetting());
        notifyDataSetChanged();
    }

    static class ViewHolder {
        ImageView ivIcon, ivSelected;
        TextView tvSetting;
    }

}
