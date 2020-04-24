package com.shumeipai.mobilecontroller.utils;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.model.LatLng;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class NewbeeLocationClient {
    private static final String TAG = "NewbeeLocationClient";
    private static boolean locationSuccess = false;
    private static final Set<ILocationChangedListener> locationChangeListeners = new HashSet<>();
    private static final InternalLocationListener internalLocationListener = new InternalLocationListener();

    private AMapLocationClient locationClient;

    private static NewbeeLocation lastKnowLocation = new NewbeeLocation();
    private static AMapLocation mAMapLocation = null;
    private static NewbeeLocationClient sInstance;

    public static synchronized NewbeeLocationClient getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new NewbeeLocationClient(context);
        }
        return sInstance;
    }

    public NewbeeLocationClient(Context context) {
        locationClient = new AMapLocationClient(context.getApplicationContext());
        locationClient.setLocationListener(internalLocationListener);
    }

    private void setLocationOptions(long locateInterval){
        AMapLocationClientOption option = new AMapLocationClientOption();
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        option.setNeedAddress(true);
        option.setInterval(locateInterval);
        locationClient.setLocationOption(option);
    }

    public void start() {
        start(2000L);
    }

    public void start(long locateInterval) {
        MapConfig.instance().setLocateInterval(locateInterval);
        setLocationOptions(locateInterval);
        locationClient.startLocation();
        Log.d(TAG,"start location, location interval is " + locateInterval);
    }

    public void stop() {
        locationClient.stopLocation();
        Log.d(TAG,"stop location");
    }

    public static NewbeeLocation getLastKnowLocation() {
        return lastKnowLocation;
    }

    public synchronized AMapLocation getLocation() {
        return mAMapLocation;
    }

    public static LatLng getLastKnowPoint(){
        final NewbeeLocation location = getLastKnowLocation();
        if ( location != null ) {
            return new LatLng( location.getLatitude(), location.getLongitude() );
        }
        return null;
    }

    public void addLocationListener(ILocationChangedListener listener) {
        if (listener != null) {
            synchronized (locationChangeListeners) {
                locationChangeListeners.add(listener);
            }
        }
    }

    public void removeLocationListener(ILocationChangedListener listener) {
        synchronized (locationChangeListeners) {
            locationChangeListeners.remove(listener);
        }
    }

    /**
     * 定位SDK监听函数
     */
    private static class InternalLocationListener implements AMapLocationListener {
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation == null ||
                    aMapLocation.getLatitude() == 0.0D ||
                    aMapLocation.getLongitude() == 0.0D ) {
                locationSuccess = false;
                return;
            }else{
                mAMapLocation = aMapLocation;
                locationSuccess = true;
            }
            Log.d(TAG, aMapLocation.toString());

            lastKnowLocation.setLatitude(aMapLocation.getLatitude());
            lastKnowLocation.setLongitude(aMapLocation.getLongitude());
            synchronized (locationChangeListeners) {
                Iterator iterator = locationChangeListeners.iterator();
                while (iterator.hasNext()) {
                    ILocationChangedListener listener = (ILocationChangedListener) iterator.next();
                    listener.onLocationChanged(lastKnowLocation.clone());
                }
            }
        }
    }
}

