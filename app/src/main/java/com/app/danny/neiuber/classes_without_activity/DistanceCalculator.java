package com.app.danny.neiuber.classes_without_activity;

/**
 * Created by danny on 2/4/18.
 */

public class DistanceCalculator {

    private double distanceLat1;
    private double distanceLng1;
    private double distanceLat2;
    private double distanceLng2;

    public DistanceCalculator(double distanceLat1, double distanceLng1, double distanceLat2, double distanceLng2) {
        this.distanceLat1 = distanceLat1;
        this.distanceLng1 = distanceLng1;
        this.distanceLat2 = distanceLat2;
        this.distanceLng2 = distanceLng2;
    }

    public double getDistanceBetweenCoordinatesInMiles(){
        return distanceInKmBetweenEarthCoordinates(distanceLat1, distanceLng1, distanceLat2, distanceLng2);
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
