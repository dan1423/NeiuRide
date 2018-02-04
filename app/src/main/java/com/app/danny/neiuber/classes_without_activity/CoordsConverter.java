package com.app.danny.neiuber.classes_without_activity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.util.List;
import java.util.Locale;

/**
 * Created by danny on 1/31/18.
 * Responsible for converting longitude and latitude into address by using Google's Geocoder API
 */

public class CoordsConverter {

    private double latitude;
    private double longitude;
    Context context;

    public CoordsConverter(double latitude, double longitude, Context context) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.context = context;
    }

    public String convertLocation() {
        return convertCoordinatesToAddress(this.latitude, this.longitude);
    }

    private String convertCoordinatesToAddress(double latt, double lngg) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latt, lngg, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i <= returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();

            } else {

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strAdd;
    }
}
