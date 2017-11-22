package com.ndanh.mytranslator.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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

public class SettingAdapter extends ArrayAdapter<Setting> {


    private List<Setting> lstSetting;
    private Context mContext;
    private int resId;
    private int mSelectedItem;

    public SettingAdapter(@NonNull Context context, int resource, @NonNull List<Setting> objects) {
        super(context, resource, objects);
        mContext = context;
        lstSetting = objects;
        resId = resource;
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
            convertView = inflater.inflate(resId, parent, false);
            viewHolder.ivIcon = convertView.findViewById(R.id.setting_icon);
            viewHolder.tvSetting = convertView.findViewById(R.id.setting_text);
            viewHolder.ivSelected = convertView.findViewById(R.id.setting_checkbox);
            convertView.setTag(viewHolder);
        }

        ViewHolder viewHolder = (ViewHolder) convertView.getTag();

        viewHolder.tvSetting.setText(mContext.getString(lstSetting.get(position).getTextSetting()));
        viewHolder.ivIcon.setImageResource(lstSetting.get(position).getIconSetting());
        viewHolder.ivSelected.setImageResource(lstSetting.get(position).getCheckBoxSetting());
        if (lstSetting.get(position).isSet()) {
            viewHolder.ivSelected.setVisibility(View.VISIBLE);
        } else {
            viewHolder.ivSelected.setVisibility(View.GONE);
        }

        return convertView;
    }


    public void changeStartMode(Setting setting) {
        Setting.initSetting(mContext);
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
