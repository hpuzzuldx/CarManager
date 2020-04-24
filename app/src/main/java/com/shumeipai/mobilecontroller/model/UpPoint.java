package com.shumeipai.mobilecontroller.model;

import java.io.Serializable;

public class UpPoint implements Serializable {
    public double lon;
    public double lat;

    public UpPoint(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }
}
