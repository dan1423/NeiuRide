package com.app.danny.neiuber.authentication;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.app.danny.neiuber.Navigation;
import com.app.danny.neiuber.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.HashMap;


/**
 * Created by danny on 1/21/18.
 */

/*
* This class is responsible for the loading screen the user sees before the app is fully loaded
* it is also responsible for making sure user allows GPS location
* */

public class SplashScreen extends Activity {
    private final int SPLASH_SCREEN_DELAY_LENGTH = 1000;
    private static final String TAG = "TEST ACTIVITY";
    private static final int ERROR_DIALOG_REQUEST = 9001;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

      /*  if(isServicesOK()) {
            checkIfUserIsLoggedIn();
            statusCheck();
        }*/

      //show the loading screen for delay length
       new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                    checkIfUserIsLoggedIn();


            }
        }, SPLASH_SCREEN_DELAY_LENGTH);

    }

    //if user is logged in, open homepage, otherwise send to log in page
    private void checkIfUserIsLoggedIn() {
        //Intent in = new Intent(SplashScreen.this, MapTestActivity.class);
        //startActivity(in);

       if(SavedUserInfo.getUserInfo(SplashScreen.this).length() == 0) {//user isn't logged in, send to Login activity
          sendToLogin();
        }else{
           String s = SavedUserInfo.getUserInfo(SplashScreen.this);
           Intent intent = new Intent(SplashScreen.this, Navigation.class);
           HashMap<String, String> driverInfo = convertStringToMap();
           intent.putExtra("driver_info", driverInfo);
           startActivity(intent);
        }
    }

    //if the user is logged in, send his saved info to the homepage
    public HashMap<String, String> convertStringToMap() {
        //get from shared prefs
        String storedHashMapString = SavedUserInfo.getUserInfo( getApplicationContext());
        java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
        Gson gson = new Gson();
        HashMap<String, String> retHashMap = gson.fromJson(storedHashMapString, type);

        return retHashMap;
    }

    public void sendToLogin(){
        Intent intent = new Intent(SplashScreen.this, Login.class);
        startActivity(intent);
    }

    //check if user's device allows GPS location and goole services
    public boolean isServicesOK(){
        Log.d(TAG,"checking version");

        int available = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(SplashScreen.this);
        if(available == ConnectionResult.SUCCESS){
            Log.d(TAG,"Map is working");
            return true;
        }else if(GoogleApiAvailability.getInstance().isUserResolvableError(available)){
            //error occured but we can resolve it
            Log.d(TAG,"Fixable error");

        }else{
            Log.d(TAG,"CAn't maqke map request");
        }
        return false;
    }

    private void statusCheck() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();

        }
    }

    //if user has not allowed GPS, the dialog pops up to get user permission
    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}
