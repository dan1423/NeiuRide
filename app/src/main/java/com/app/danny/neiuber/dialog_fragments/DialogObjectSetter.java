package com.app.danny.neiuber.dialog_fragments;

import android.content.Context;

import com.app.danny.neiuber.classes_without_activity.CoordsToAddressConverter;
import com.app.danny.neiuber.classes_without_activity.DistanceCalculator;
import com.app.danny.neiuber.menu.items.MapMenu;

import java.util.Arrays;

/**
 * Created by danny on 2/4/18.
 * This class is responsible for setting the four dialogs we need after the driver accepts a ride request
 */

public class DialogObjectSetter {
    private double lat1,lng1,lat2,lng2;
    private String passengerName;
    private Context ctx;
    private DialogObject[] arrayOfDialogs = new DialogObject[4];
    MapMenu mm = new MapMenu();

    public DialogObjectSetter(double lat1, double lng1, double lat2, double lng2, String passengerName, Context ctx) {
        this.lat1 = lat1;
        this.lng1 = lng1;
        this.lat2 = lat2;
        this.lng2 = lng2;
        this.passengerName = passengerName;
        this.ctx = ctx;
    }

    public DialogObject[] getDialogs(){
        setDialogOne();
        setDialogTwo();
        setDialogThree();
        setDialogFour();

        return arrayOfDialogs;
    }

    private void setDialogOne(){
        String addr = new CoordsToAddressConverter(lat1,lng1,ctx).convertLocation();
        double tMiles = new DistanceCalculator(mm.currentLat,mm.currentLng,lat2,lng2).getDistanceBetweenCoordinatesInMiles();
        arrayOfDialogs[0] = new DialogObject(passengerName,"Heading to "+addr,"GO",(float)tMiles,false);
    }

    private void setDialogTwo(){
        String addr = new CoordsToAddressConverter(lat1,lng1,ctx).convertLocation();
        double tMiles = new DistanceCalculator(mm.currentLat,mm.currentLng,lat2,lng2).getDistanceBetweenCoordinatesInMiles();
        arrayOfDialogs[1] = new DialogObject(passengerName,"Heading to "+addr,"ARRIVE",(float)tMiles,true);
    }

    private void setDialogThree(){
        String addr = new CoordsToAddressConverter(lat1,lng1,ctx).convertLocation();
        double tMiles = new DistanceCalculator(lat1,lng1,lat2,lng2).getDistanceBetweenCoordinatesInMiles();
        arrayOfDialogs[2] = new DialogObject(passengerName,"Waiting for Passenger ","START TRIP",(float)tMiles,false);
    }

    private void setDialogFour(){
        String addr = new CoordsToAddressConverter(lat2,lng2,ctx).convertLocation();
        double tMiles = new DistanceCalculator(lat1,lng1,lat2,lng2).getDistanceBetweenCoordinatesInMiles();
        arrayOfDialogs[3] = new DialogObject(passengerName,"Dropping off passenger at "+addr,"FINISH TRIP",(float)tMiles,true);
    }

    @Override
    public String toString() {
        return "DialogObjectSetter{" +
                "lat1=" + lat1 +
                ", lng1=" + lng1 +
                ", lat2=" + lat2 +
                ", lng2=" + lng2 +
                ", passengerName='" + passengerName + '\'' +
                ", ctx=" + ctx +
                ", arrayOfDialogs=" + Arrays.toString(arrayOfDialogs) +
                '}';
    }
}
