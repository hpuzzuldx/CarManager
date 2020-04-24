package com.shumeipai.mobilecontroller.utils;

import com.amap.api.maps.MapView;
import com.shumeipai.mobilecontroller.model.CMarkerData;
import com.shumeipai.mobilecontroller.widget.ClickNumberMarker;
import com.shumeipai.mobilecontroller.widget.ClickSendMarker;
import com.shumeipai.mobilecontroller.base.MySelfMarker;

public class MarkerFactory {

    public static ClickSendMarker getClickSendMarker(MapView mapView, CMarkerData data) {

        if (data == null || mapView == null) {
            return null;
        }

        return new ClickSendMarker(mapView, data);
    }

    public static ClickNumberMarker getClickNumberMarker(MapView mapView, CMarkerData data) {

        if (data == null || mapView == null) {
            return null;
        }

        return new ClickNumberMarker(mapView, data);
    }

    public static MySelfMarker getMySelfMarker(MapView mapView, CMarkerData data) {

        if (data == null || mapView == null) {
            return null;
        }

        return new MySelfMarker(mapView, data);
    }

}
