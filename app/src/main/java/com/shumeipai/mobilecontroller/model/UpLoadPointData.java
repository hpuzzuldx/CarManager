package com.shumeipai.mobilecontroller.model;

import com.amap.api.maps.model.LatLng;

import java.util.ArrayList;

public class UpLoadPointData {
    private ArrayList<LatLng> pointList;

    public ArrayList<LatLng> getPointList() {
        return pointList;
    }

    public void setPointList(ArrayList<LatLng> pointList) {
        this.pointList = pointList;
    }
}
