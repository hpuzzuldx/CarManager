package com.shumeipai.mobilecontroller.utils;

public class MapConfig {
    private long mLocateInterval = 2_000L;

    public static MapConfig instance(){
        return InstanceHolder.INSTANCE;
    }

    private static class InstanceHolder{
        private static final MapConfig INSTANCE = new MapConfig();
    }

    public void setLocateInterval(long locateInterval){
        this.mLocateInterval = locateInterval;
    }

    public long getLocateInterval(){
        return mLocateInterval;
    }
}
