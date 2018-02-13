package com.app.danny.neiuber.classes_without_activity;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by danny on 2/9/18.
 */

public class CurrentLocation implements LocationListener {
    LocationManager lm;
    Context ctx;

    public CurrentLocation(Context ctx){
        this.ctx = ctx;

    }

    public double[] getCurrentLocation(){
        try{
            lm = (LocationManager)ctx.getSystemService(ctx.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_LOW);
            String provider = lm.getBestProvider(criteria, true);

            Location location = lm.getLastKnownLocation(provider);

            if (location == null) {
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            } else {
                //onLocationChanged(location);
                return new double[]{location.getLatitude(),location.getLongitude()};
            }

        }catch (SecurityException e){
            Toast.makeText(ctx,"SECURITY EXCEPTION: "+e.getMessage(),Toast.LENGTH_SHORT).show();
        }
        return new double[]{0.0,0.0};
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
