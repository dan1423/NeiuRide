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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.danny.neiuber.R;
import com.app.danny.neiuber.classes_without_activity.CurrentLocation;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by danny on 2/1/18.
 * This Fragment gets Dialog object and changes its views according to the object attributes
 */

public class TripDialogFragment extends DialogFragment {

    View tripDialogView;
    private TextView heading, name, totalMiles;
    private Button btnDecsion, btnCancel, btnGetDirections;
    private ImageView hideDialog;
    DialogObject currentDialogObject;

    public TripDialogFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);

    }

    public static TripDialogFragment newInstance(String title) {
       TripDialogFragment frag = new TripDialogFragment();
        Bundle args = new Bundle();
        args.putString("Trip", title);
        frag.setArguments(args);
        return frag;
    }

   //interface responsible for handling button click when  driver presses decision button anytime during the request/trip
    public interface ChangeDialog {
        void onChangeDialog();
    }
    public ChangeDialog changeDialog;

    //if user presses cancel button on any of the dialogs, we call this method
    public interface CancelAllTripDialogs{
        void onButtonCancel();
    }

    public CancelAllTripDialogs cancelAllTripDialogs;

    //button appears when driver is driving(going to passenger's location and going to set destination)
    public interface GetDirection{
        void onButtonGetDirection(double lat, double lng);
    }

    public GetDirection getDirection;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            changeDialog = (ChangeDialog)getTargetFragment();
            cancelAllTripDialogs = (CancelAllTripDialogs) getTargetFragment();
            getDirection = (GetDirection)getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG,e.getMessage());
        }


    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        tripDialogView = inflater.inflate(R.layout.during_trip_dialog, null);//set view for dialog(ride requests)
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        heading = tripDialogView.findViewById(R.id.txtDuringTripHeading);
        name = tripDialogView.findViewById(R.id.txtDuringTripPassengerName);
        totalMiles = tripDialogView.findViewById(R.id.txtDuringTripDistance);
        btnDecsion = tripDialogView.findViewById(R.id.btnDuringTripRideDecision);
        btnCancel = tripDialogView.findViewById(R.id.btnDuringTripCancelRide);
        btnGetDirections = tripDialogView.findViewById(R.id.btnDuringTripGetDirections);
        hideDialog = tripDialogView.findViewById(R.id.hideTripDialog);

        btnDecsion.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                changeDialog.onChangeDialog();
                getDialog().dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                cancelAllTripDialogs.onButtonCancel();
                getDialog().cancel();
            }
        });

        hideDialog.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                getDialog().hide();
            }
        });


        getBundleArgs();
        return  tripDialogView;
    }

    private void getBundleArgs(){
        Bundle bundle = new Bundle();
        bundle = this.getArguments();
        currentDialogObject =(DialogObject)bundle.getSerializable("current_dialog");

        setDialogView();
    }

    private void setDialogView(){
        //Toast.makeText(getContext(), Arrays.toString(new CurrentLocation(getContext()).getCurrentLocation()),Toast.LENGTH_LONG).show();
        String tm =  new DecimalFormat("#.##").format(currentDialogObject.getTotalMiles());
        //Toast.makeText(getContext(),"Total Miles"+currentDialogObject.getTotalMiles(),Toast.LENGTH_LONG).show();

        heading.setText(currentDialogObject.getHeading());
        name.setText(currentDialogObject.getPassengerName());
        totalMiles.setText(tm +" miles from current location");
        btnDecsion.setText(currentDialogObject.getButtonText());

        if(currentDialogObject.isMoving()) {
            btnGetDirections.setVisibility(View.VISIBLE);
            btnGetDirections.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    getDirection.onButtonGetDirection(currentDialogObject.getDestination()[0],currentDialogObject.getDestination()[1]);
                }
            });

        }
    }

    }


