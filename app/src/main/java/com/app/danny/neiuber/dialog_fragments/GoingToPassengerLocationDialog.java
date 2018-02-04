package com.app.danny.neiuber.dialog_fragments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.danny.neiuber.R;
import com.app.danny.neiuber.classes_without_activity.CoordsConverter;

import static android.content.ContentValues.TAG;

/**
 * Created by danny on 1/30/18.
 */

/*
Within this dialog, switch ui from one to another, like domino, Arrive->pick->drop-off
if user cancels at anythime,send to mapmenu fragment, if user finishes ride, then also send to maop menu
* */

/*
* This dialog will be called three times:when request is accepted, driver going to passenger location,
* and taking passenger to destination.
*
* */

public class GoingToPassengerLocationDialog extends DialogFragment {

    Button btnTripCancel,btnTripDecision;
    private ImageView hideTripDialog;
    private TextView textTripDescription, textTripHeading,textTripPassengerName, textTripDistance;

    String decision,heading,name,distance;
    double startingLat,startingLng, endingLat,endingLng;
    public GoToPassengerLocation buttonClicked;


    View dialogView;

    public GoingToPassengerLocationDialog() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }


    // Defines the listener interface
    public interface GoToPassengerLocation {
        void onGoToPassengerLocationClicked(boolean accepted);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            buttonClicked = (GoToPassengerLocation) getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);//set the size of the dialog
    }

    public static GoingToPassengerLocationDialog newInstance(String title) {
        GoingToPassengerLocationDialog frag = new GoingToPassengerLocationDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dialogView = inflater.inflate(R.layout.during_trip_dialog, container);
       getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

        btnTripDecision =(Button)  dialogView.findViewById(R.id.btnDuringTripRideDecision);
        btnTripCancel =(Button)  dialogView.findViewById(R.id.btnDuringTripCancelRide);
        hideTripDialog =(ImageView)  dialogView.findViewById(R.id.hideTripDialog);

        textTripHeading= (TextView) dialogView.findViewById(R.id.txtDuringTripHeading);
        textTripDescription = (TextView)  dialogView.findViewById(R.id.txtDuringTripDescription);
        textTripPassengerName = (TextView)  dialogView.findViewById(R.id.txtDuringTripPassengerName);
        textTripDistance = (TextView)  dialogView.findViewById(R.id.txtDuringTripDistance);


        hideTripDialog.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getDialog().hide();
            }
        });
        btnTripDecision.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                openGoogleMap(String.valueOf(startingLat), String.valueOf(startingLng));
            }
        });

        getBundleArgs();
        return  dialogView;
    }

    public void getBundleArgs() {
        Bundle bundle = new Bundle();
        bundle = this.getArguments();
        heading = bundle.getString("heading");
        name = bundle.getString("name");
        distance = bundle.getString("distance");

        startingLat = bundle.getDouble("starting_lat");
        startingLng = bundle.getDouble("starting_lng");
        endingLat = bundle.getDouble("ending_lat");
        endingLng = bundle.getDouble("ending_lng");

        setDisplay();
    }

    //set textfields to passed in bundle arguments
    public void setDisplay() {
        String loc = convertCoordsToAddress();
        btnTripDecision.setText(decision);
        textTripHeading.setText("Heading to " + loc);
        textTripPassengerName.setText(name);
        textTripDistance.setText(distance);


    }

    public String convertCoordsToAddress() {
        CoordsConverter cc = new CoordsConverter(startingLat, startingLng, getContext());
        String address = cc.convertLocation();
        return address;

    }

    private void openGoogleMap(String lattitudeParam, String longitudeParam){
        // Toast.makeText(getContext(), currentLat+" : "+currentLng, Toast.LENGTH_LONG).show();
        String uri = "http://maps.google.com/maps?saddr=" +
                String.valueOf(41.8756719) + "," +String.valueOf(-87.62434689999998) +
                "&daddr=" +  lattitudeParam + "," + longitudeParam;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }



}
