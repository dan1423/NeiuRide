package com.app.danny.neiuber.settings.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.danny.neiuber.R;
import com.app.danny.neiuber.ride_history.adapter.RideHistoryItems;

import java.util.ArrayList;

/**
 * Created by danny on 1/17/18.
 */

public class SettingsAdapter extends BaseAdapter {

    Context c;
    ArrayList<SettingsAdapter> sAdapter;

    public SettingsAdapter(Context c, ArrayList<SettingsAdapter> sAdapter) {
        this.c = c;
        this.sAdapter = sAdapter;
    }

    @Override
    public int getCount() {
        return sAdapter.size();
    }

    @Override
    public Object getItem(int position) {
        return sAdapter.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if(view == null){
            view= LayoutInflater.from(c).inflate(R.layout.settings_listview,parent, false);
        }

        final SettingsItems si = (SettingsItems) this.getItem(position);
        ImageView settingsImage= (ImageView) view.findViewById(R.id.settings_image);
        ImageView editSettings = (ImageView) view.findViewById(R.id.btnEditSetting);
        TextView settingTitle = (TextView) view.findViewById(R.id.setting_title);
        TextView settingDesc = (TextView) view.findViewById(R.id.setting_description);
        settingTitle.setText(si.getSettingTitle());
        settingDesc.setText(si.getSettingDesc());
        settingsImage.setImageResource(si.getImage());


        return view;
    }
    /*public String getTitle(int position) {

    }*/
}
