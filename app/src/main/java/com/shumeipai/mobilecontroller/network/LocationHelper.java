package com.shumeipai.mobilecontroller.network;

import java.util.HashMap;
import java.util.Map;

/**
 * @author congtaowang
 * @since 2020-01-02
 * <p>
 * 缓存当前经纬度位置，便于接口请求
 */
public class LocationHelper {

    private static volatile LocationHelper sInstance;

    private LocationHelper() {
    }

    public static LocationHelper getInstance() {
        if ( sInstance == null ) {
            synchronized ( LocationHelper.class ) {
                if ( sInstance == null ) {
                    sInstance = new LocationHelper();
                }
            }
        }
        return sInstance;
    }

    public synchronized void release() {
        sInstance = null;
    }

    private Map< String, Object > mLocationProperties = new HashMap<>();

    public synchronized Map< String, Object > getLocationProperties() {
        return mLocationProperties;
    }
}
