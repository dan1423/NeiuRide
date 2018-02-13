package com.app.danny.neiuber.dialog_fragments;

import android.content.Context;

import java.io.Serializable;

/**
 * Created by danny on 2/1/18.
 */

public class DialogObject implements Serializable{

    private String passengerName;
    private String heading;
    private String buttonText;
    private float totalMiles;
    private boolean isMoving; //this decides whether we show the Get Directions button
    private double[] destination = new double[2];

    public DialogObject(String passengerName,String heading, String buttonText, float totalMiles, boolean isMoving, double[] destination) {
        this.passengerName = passengerName;
        this.heading = heading;
        this.buttonText = buttonText;
        this.totalMiles = totalMiles;
        this.isMoving = isMoving;
        this.destination = destination;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getButtonText() {
        return buttonText;
    }

    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    public float getTotalMiles() {
        return totalMiles;
    }

    public void setTotalMiles(float totalMiles) {
        this.totalMiles = totalMiles;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public double[] getDestination() {
        return destination;
    }

    public void setDestination(double[] destination) {
        this.destination = destination;
    }

}
