package com.shumeipai.mobilecontroller.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.shumeipai.mobilecontroller.R;
import com.shumeipai.mobilecontroller.model.CMarkerData;

public class MySelfMarker extends BaseMarker {

    public MySelfMarker(MapView mapView, CMarkerData data) {
        super(mapView);
        final LatLng latLng;
        latLng = new LatLng(data.poiLat, data.poiLng);
        final View markerView = generateMarkerView(mapView.getContext());
        initOptions(latLng, BitmapDescriptorFactory.fromView(markerView));
    }

    private static View generateMarkerView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_self_location_marker, null);
        return view;
    }

    @Override
    protected void initOptions(LatLng latLng, BitmapDescriptor bitmapDescriptor) {
        super.initOptions(latLng, bitmapDescriptor);
        markerOptions.zIndex(MapZIndexConstant.ZINDEX_TOP_MARKER);
        markerOptions.anchor(0.5f, 0.5f);
    }

    public void updateMarkerView(LatLng latLng) {
       updatePosition(latLng);
    }
}