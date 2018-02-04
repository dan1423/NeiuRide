package com.app.danny.neiuber.ride_history.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.danny.neiuber.R;

import java.util.ArrayList;

public class RideHistoryAdapter extends BaseAdapter {

    Context c;
    ArrayList<RideHistoryAdapter> rha;

    public RideHistoryAdapter(Context c, ArrayList<RideHistoryAdapter> rha) {
        this.c = c;
        this.rha = rha;
    }

    @Override
    public int getCount() {
        return rha.size();
    }
    @Override
    public Object getItem(int i) {
        return rha.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(view==null)
        {
            view= LayoutInflater.from(c).inflate(R.layout.ride_history_list,viewGroup,false);
        }
        final RideHistoryItems rh= (RideHistoryItems) this.getItem(i);
        ImageView profileImage= (ImageView) view.findViewById(R.id.imgRideHistoryImage);
        ImageView historyInfo = (ImageView) view.findViewById(R.id.btnHistoryInfo);
        TextView rideTime = (TextView) view.findViewById(R.id.ride_time);
        TextView rideFare = (TextView) view.findViewById(R.id.ride_fare);
        rideTime.setText(rh.getRideTime());
        rideFare.setText(rh.getRideFare());
        profileImage.setImageResource(rh.getImage());


        return view;
    }
}
