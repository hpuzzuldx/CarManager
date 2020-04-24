package com.shumeipai.mobilecontroller.utils;

public class NewbeeLocation implements Cloneable{
    private double latitude = 0;
    private double longitude = 0;

    public NewbeeLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public NewbeeLocation() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    protected NewbeeLocation clone(){
        try {
            return (NewbeeLocation) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return this;
    }
}

