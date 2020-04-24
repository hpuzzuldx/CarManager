package com.shumeipai.mobilecontroller.base;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.shumeipai.mobilecontroller.PiCarApplication;
import com.shumeipai.mobilecontroller.model.CMarkerData;

public class InfoWinAdapter implements AMap.InfoWindowAdapter, View.OnClickListener {
    private Context mContext = PiCarApplication.getContext();
    private LatLng latLng;
    private String agentName;
    private String snippet;
    private CMarkerData markerData;
    private OnWinClickListener onWinClickListener;

    public OnWinClickListener getOnWinClickListener() {
        return onWinClickListener;
    }

    public void setOnWinClickListener(OnWinClickListener onWinClickListener) {
        this.onWinClickListener = onWinClickListener;
    }

    public static interface  OnWinClickListener{
        void onItemClick(View view, CMarkerData data);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        initData(marker);
        View view = initView();
        return view;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    private void initData(Marker marker) {
        latLng = marker.getPosition();
        snippet = marker.getSnippet();
        agentName = marker.getTitle();
        markerData = (CMarkerData) marker.getObject();
    }

    @NonNull
    private View initView() {
        if (markerData != null) {
        }
        return null;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
       /* switch (id) {
            case R.id.pop_win_fu_pack_get:  //点击抢占红包
                if (onWinClickListener != null){
                    onWinClickListener.onItemClick(v,markerData);
                }
                break;

            case R.id.oil_use_welfare:  //点击加油站去加油
                if (onWinClickListener != null){
                    onWinClickListener.onItemClick(v,markerData);
                }
                break;
        }*/
    }

}
