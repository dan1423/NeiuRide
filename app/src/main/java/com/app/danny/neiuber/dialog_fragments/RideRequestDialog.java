package com.app.danny.neiuber.dialog_fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.app.danny.neiuber.R;

import static android.content.ContentValues.TAG;

/**
 * Created by danny on 1/30/18.
 */



public class RideRequestDialog extends DialogFragment {

    Button acceptRequest, denyRequest;
    private TextView textPassengerName, textRequestDistance, textRequestHeading, textRequestTimer;
    String heading,name,distance;

    private CountDownTimer timeRemainingForRequest;
    boolean isButtonClicked = false;


    View requestView;

    public RideRequestDialog() {
    }

    // Defines the listener interface
    public interface AcceptRequestListener {
        void onFinishRideRequestDialog(boolean accepted);
    }
    public AcceptRequestListener acceptRequestListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            acceptRequestListener = (AcceptRequestListener)getTargetFragment();
        }catch (ClassCastException e){
            Log.e(TAG,e.getMessage());
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen);

        requestCountdownTimer();
    }

    public static RideRequestDialog newInstance(String title) {
        RideRequestDialog frag = new RideRequestDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        requestView = inflater.inflate(R.layout.ride_request_dialog, null);//set view for dialog(ride requests)
        getDialog().getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        textPassengerName = requestView.findViewById(R.id.requestDialogName);
        textRequestDistance = requestView.findViewById(R.id.requestDialogDistance);
        textRequestHeading = requestView.findViewById(R.id.requestDialogHeading);
        textRequestTimer = requestView.findViewById(R.id.requestDialogTimer);
       acceptRequest = (Button) requestView.findViewById(R.id.btnRideRequestAccept);
        denyRequest = (Button) requestView.findViewById(R.id.btnRideRequestDeny);

        acceptRequest.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                isButtonClicked = true;
                acceptRequestListener.onFinishRideRequestDialog(true);
                getDialog().dismiss();
            }
        });

        denyRequest.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                isButtonClicked = true;
                acceptRequestListener.onFinishRideRequestDialog(false);
                getDialog().dismiss();
            }
        });

        getBundleArgs();
        return  requestView;
    }

    public void getBundleArgs() {
        Bundle bundle = new Bundle();
        bundle = this.getArguments();
        heading = bundle.getString("heading");
        name = bundle.getString("name");
        distance = bundle.getString("distance");

        setRequestInfo();
    }

    public void setRequestInfo() {
        textPassengerName.setText(name);
        textRequestDistance.setText(distance);
        textRequestHeading.setText(heading);
    }

    //Driver has 30 seconds to accept request
       public void requestCountdownTimer() {
        timeRemainingForRequest =  new CountDownTimer(30000, 1000) {
               public void onTick(long millisUntilFinished) {
                   if(isButtonClicked && timeRemainingForRequest!=null){
                       timeRemainingForRequest.cancel();
                       timeRemainingForRequest = null;
                   }
                   if(millisUntilFinished / 1000 < 10) {
                       textRequestTimer.setText("00:0" + millisUntilFinished / 1000);//give timer leading zero
                   }else{
                       textRequestTimer.setText("00:" + millisUntilFinished / 1000);
                   }
               }

               public void onFinish() {
                   acceptRequestListener.onFinishRideRequestDialog(false);
                   getDialog().dismiss();
               }
           }.start();
       }





}
