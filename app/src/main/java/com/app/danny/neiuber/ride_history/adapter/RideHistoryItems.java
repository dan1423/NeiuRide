package com.app.danny.neiuber.ride_history.adapter;

/**
 * Created by danny on 11/16/17.
 */

public class RideHistoryItems {
    private int image;
    private String rideTime;
    private String rideFare;

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getRideTime() {
        return rideTime;
    }

    public void setRideTime(String rideTime) {
        this.rideTime = rideTime;
    }

    public String getRideFare() {
        return rideFare;
    }

    public void setRideFare(String rideFare) {
        this.rideFare = rideFare;
    }

    @Override
    public String toString() {
        return "RideHistoryItems{" +
                "image=" + image +
                ", rideTime='" + rideTime + '\'' +
                ", rideFare='" + rideFare + '\'' +
                '}';
    }
}
