package com.app.danny.neiuber.ride_history;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.danny.neiuber.R;
import com.app.danny.neiuber.menu.items.RideHistoryMenu;

import java.util.HashMap;

public class DetailedRideHistory extends Fragment  {

    private View viewForDetailed;
    private TextView dateText, fromText, toText,distanceText,fareText;
    private ImageView iv;
    private HashMap<String, String> rideHistoryMap = new HashMap<>();
    private HashMap<String, String> driverInfo = new HashMap<>();

    public DetailedRideHistory(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = new Bundle();
        bundle = this.getArguments();
        if (bundle != null) {
            rideHistoryMap = (HashMap<String, String>) bundle.getSerializable("history");
            driverInfo = (HashMap<String, String>) bundle.getSerializable("driver_info");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        viewForDetailed =  inflater.inflate(R.layout.ride_history_inner, container, false);

         dateText = (TextView) viewForDetailed.findViewById(R.id.txtDetailedRideHistoryDate);
         fromText = (TextView) viewForDetailed.findViewById(R.id.txtDetailedRideHistoryStartingLocation);
         toText = (TextView) viewForDetailed.findViewById(R.id.txtDetailedRideHistoryEndingLocation);
         distanceText = (TextView) viewForDetailed.findViewById(R.id.txtDetailedRideHistoryDistance);
         fareText = (TextView) viewForDetailed.findViewById(R.id.txtDetailedRideHistoryFare);
         iv = (ImageView) viewForDetailed.findViewById(R.id.arrowBackToHistoryList);

        dateText.setText("Date: "+rideHistoryMap.get("TripDate"));
        fromText.setText("From: "+ rideHistoryMap.get("StartingLocation"));
        toText.setText("To: "+ rideHistoryMap.get("EndingLocation"));
        distanceText.setText(rideHistoryMap.get("TotalMiles")+" miles");
        fareText.setText("$"+ rideHistoryMap.get("TotalFare"));

        iv.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                backToHistoryListView();
            }
        });
        return  viewForDetailed;
    }


    private void backToHistoryListView() {
        FragmentManager fm = getFragmentManager();
        Fragment f = new RideHistoryMenu();
        Bundle bundle = new Bundle();
        bundle.putSerializable("driver_info", driverInfo);
        f.setArguments(bundle);
       fm.popBackStack();
        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.main_layout_screen,f).addToBackStack(null).commit();

    }


}
