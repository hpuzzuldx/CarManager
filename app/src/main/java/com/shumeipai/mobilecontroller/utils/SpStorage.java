package com.shumeipai.mobilecontroller.utils;

import com.shumeipai.mobilecontroller.PiCarApplication;


public class SpStorage {

    public static String getIP() {
        return SharedPrefsMgr.getInstance( PiCarApplication.getContext() ).getString( "ip" );
    }

    public static void setIP( String ip ) {
        SharedPrefsMgr.getInstance( PiCarApplication.getContext() ).putString( "ip", ip );
    }

    public static String getPort() {
        return SharedPrefsMgr.getInstance( PiCarApplication.getContext() ).getString( "port" );
    }

    public static void setPort( String port ) {
        SharedPrefsMgr.getInstance( PiCarApplication.getContext() ).putString( "port", port );
    }
}
