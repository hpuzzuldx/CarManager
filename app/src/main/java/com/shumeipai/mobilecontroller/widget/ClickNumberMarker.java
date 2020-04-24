package com.shumeipai.mobilecontroller.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.shumeipai.mobilecontroller.R;
import com.shumeipai.mobilecontroller.base.BaseMarker;
import com.shumeipai.mobilecontroller.model.CMarkerData;
import com.shumeipai.mobilecontroller.base.MapZIndexConstant;

public class ClickNumberMarker extends BaseMarker {

    public ClickNumberMarker(MapView mapView, CMarkerData data) {
        super(mapView);
        final LatLng latLng;
        latLng = new LatLng(data.poiLat, data.poiLng);
        final View markerView = generateMarkerView(mapView.getContext(),data);
        initOptions(latLng, BitmapDescriptorFactory.fromView(markerView));
    }

    private static View generateMarkerView(Context context,CMarkerData data) {
        View view = LayoutInflater.from(context).inflate(R.layout.click_marker_num_marker, null);
        TextView view1 = view.findViewById(R.id.number_text);
        view1.setText(data.getNumberIndex()+"");
        return view;
    }

    @Override
    protected void initOptions(LatLng latLng, BitmapDescriptor bitmapDescriptor) {
        super.initOptions(latLng, bitmapDescriptor);
        markerOptions.zIndex(MapZIndexConstant.ZINDEX_TOP_MARKER);
        markerOptions.anchor(0.5f, 0.5f);
    }

    public void updateMarkerPositon(LatLng latLng) {
        updatePosition(latLng);
    }

    public void updateMarkerView(CMarkerData data) {
        View markerView = generateMarkerView(mMapView.getContext(),data);
        setObject(data);
        setIcon(markerView);
    }
}