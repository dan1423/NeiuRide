package com.app.danny.neiuber.classes_without_activity;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.util.List;
import java.util.Locale;

/**
 * Created by danny on 1/9/18.
 * This class is responsible for calcualting distance between starting location of
 * passenger request to ending location and the total trip time.
 *
 * NOTE:remove convert methods and use CoordsToAddressConverter class
 */

public class TripCalculation {

    private double fromLat;
    private double fromLng;
    private double toLat;
    private double toLng;
    private String startingLocation= "";
    private String endingLocation = "";

    private String totalTripTimeInMinutes;
    private Context mapContext;
    private Locale mapLocale;

    private double fare = 0.0;
    private double totalMiles = 0.0;

    public TripCalculation(double fromLat, double fromLng, double toLat, double toLng, String totalTripTime, Context context) {
        this.fromLat = fromLat;
        this.fromLng = fromLng;
        this.toLat = toLat;
        this.toLng = toLng;
        this.totalTripTimeInMinutes = totalTripTime;
        this.mapContext = context;


    }

    public void convertStartingLocation() {
        this.startingLocation = convertCoordinatesToAddress(this.fromLat, this.fromLng);
    }

    public void convertEndingLocation() {
        this.endingLocation = convertCoordinatesToAddress(this.toLat, this.toLng);
    }

    public String getStartingLocation() {
        return this.startingLocation;
    }

    public String getEndingLocation() {
        return this.endingLocation;
    }


    public double getTotalMiles(){
        return getDistanceInMiles();
    }
    public double getFare() {
        return fareCalculation();
    }

    @Override
    public String toString() {
        return "TripCalculation{" +
                "fromLat=" + fromLat +
                ", fromLng=" + fromLng +
                ", toLat=" + toLat +
                ", toLng=" + toLng +
                ", startingLocation='" + startingLocation + '\'' +
                ", endingLocation='" + endingLocation + '\'' +
                ", totalTripTimeInMinutes='" + totalTripTimeInMinutes + '\'' +
                ", mapContext=" + mapContext +
                ", mapLocale=" + mapLocale +
                ", fare=" + fare +
                ", totalMiles=" + totalMiles +
                '}';
    }

    private String convertCoordinatesToAddress(double latt, double lngg) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(mapContext, Locale.getDefault());
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

    private double fareCalculation() {
    //fare = $2.10 + ($0.15/min) + ($0.90/mile)
        double timeFare = Integer.parseInt(this.totalTripTimeInMinutes)  * 0.15;
        double distanceFare = getDistanceInMiles();


        return (2.10 + timeFare + distanceFare);
    }

    private double getDistanceInMiles() {
      return  distanceInKmBetweenEarthCoordinates(this.fromLat, this.fromLng, this.toLat, this.toLng);
    }

    private double  distanceInKmBetweenEarthCoordinates(double startingLat, double startingLng, double endingLat, double endingLng) {
        double earthRadiusKm = 6371;

        double dLat = Math.toRadians(endingLat-startingLat);
        double dLon = Math.toRadians(endingLng-startingLng);

        double fromLatToRadians = startingLat;
        double toLatToRadians  = endingLat;


        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.sin(dLon/2) * Math.sin(dLon/2) * Math.cos(fromLatToRadians) * Math.cos(toLatToRadians);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        return earthRadiusKm * c;
    }


}
