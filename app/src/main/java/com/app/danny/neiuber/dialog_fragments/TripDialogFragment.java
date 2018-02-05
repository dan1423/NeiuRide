package com.app.danny.neiuber.dialog_fragments;

import android.content.Context;
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
import android.widget.Toast;

import com.app.danny.neiuber.R;
import com.app.danny.neiuber.menu.items.MapMenu;

import java.text.DecimalFormat;

import static android.content.ContentValues.TAG;

/**
 * Created by danny on 2/1/18.
 * This Fragment gets Dialog object and changes its views according to the object attributes
 */

public class TripDialogFragment extends DialogFragment {

    View tripDialogView;
    private TextView heading, name, totalMiles;
    private Button btnDecsion, btnCancel, btnGetDirections;
    private String startingLat, startingLng, endingLat, endingLng;
    private ImageView hideDialog;
    DialogObject currentDialog;


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

    // Defines the listener interface
    public interface ChangeDialog {
        void onChangeDialog();
    }
    public ChangeDialog changeDialog;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            changeDialog = (ChangeDialog)getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        tripDialogView = inflater.inflate(R.layout.during_trip_dialog, null);//set view for dialog(ride requests)
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
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
        currentDialog =(DialogObject)bundle.getSerializable("current_dialog");

        setDialogView();

    }

    private void setDialogView(){
        Toast.makeText(getContext(),new MapMenu().currentLat+"",Toast.LENGTH_LONG).show();
       /* String tm =  new DecimalFormat("#.##").format(currentDialog.getTotalMiles());

        heading.setText(currentDialog.getHeading());
        name.setText(currentDialog.getPassengerName());
        totalMiles.setText(tm +" miles from current location");
        btnDecsion.setText(currentDialog.getButtonText());

        if(currentDialog.isMoving()) {
            btnGetDirections.setVisibility(View.VISIBLE);
            btnGetDirections.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {

                }
            });

        }*/


        }
    }


