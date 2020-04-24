package com.shumeipai.mobilecontroller.network;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by congtaowang on 2018/3/29.
 */

public class Utils {

    public static String getCellId( Context context ) {
        TelephonyManager tm = ( TelephonyManager ) context.getSystemService( Context.TELEPHONY_SERVICE );
        if ( tm == null ) {
            return "";
        }

        PackageManager pm = context.getPackageManager();
        boolean accessCoarseLocationPermission = ( PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission( Manifest.permission.ACCESS_COARSE_LOCATION, context.getPackageName() ) );
        boolean accessFineLocationPermission = ( PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission( Manifest.permission.ACCESS_FINE_LOCATION, context.getPackageName() ) );
        if ( !accessCoarseLocationPermission || !accessFineLocationPermission )
            return "noPermission";

        CellLocation location = null;
        try {
            location = tm.getCellLocation();
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        if ( location != null ) {
            // Gsm网络 , 联通移动的网络属于这一套
            if ( location instanceof GsmCellLocation ) {
                GsmCellLocation gsmLoc = ( GsmCellLocation ) location;
                int cellid = gsmLoc.getCid();
                return String.valueOf( cellid );
                // Cdma网络 , 电信网络属于这一种
            } else if ( location instanceof CdmaCellLocation ) {
                CdmaCellLocation cdmaLoc = ( CdmaCellLocation ) location;
                return String.valueOf( cdmaLoc.getBaseStationId() );
            }
        }
        return "";
    }


    public static final String GET = "get";
    public static final String GSM_SERIAL = "gsm.serial";
    public static final String FOTA_VERSION = "ro.fota.version";
    public static final String PROPERTIES = "android.os.SystemProperties";

    public static String getSn() {
        return getSystemProperties( GSM_SERIAL );
    }

    public static String getFotaVersion() {
        return getSystemProperties( FOTA_VERSION );
    }

    public static String getSystemProperties( String name ) {
        String value = "";

        try {
            Class< ? > c = Class.forName( PROPERTIES );
            Method get = c.getMethod( GET, String.class );
            value = ( String ) get.invoke( c, name );
        } catch ( ClassNotFoundException var3 ) {
            var3.printStackTrace();
        } catch ( NoSuchMethodException var4 ) {
            var4.printStackTrace();
        } catch ( InvocationTargetException var5 ) {
            var5.printStackTrace();
        } catch ( IllegalAccessException var6 ) {
            var6.printStackTrace();
        }
        return value;
    }
}
