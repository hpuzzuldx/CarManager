package com.shumeipai.mobilecontroller.base;

import android.Manifest;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.CellLocation;
import android.telephony.TelephonyManager;
import android.telephony.cdma.CdmaCellLocation;
import android.telephony.gsm.GsmCellLocation;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Created by congtaowang on 2018/3/29.
 */

public class Utils {

    /**
     * 验证手机格式
     */
    public static boolean isMobilePhone( String mobiles ) {
        String telRegex = "[1]\\d{10}";
        if ( TextUtils.isEmpty( mobiles ) ) return false;
        else return mobiles.matches( telRegex );
    }

    public static boolean isVerifyCodeRight( String code ) {
        if ( !TextUtils.isEmpty( code ) && code.trim().length() == 4 ) {
            return true;
        }
        return false;
    }

    // 格式化用于拨打的电话号码
    public static String formatPhoneNumber( String phoneNumber ) {
        String StrTmp = "tel:" + phoneNumber.replace( "-", "" ).replace( " ", "" );
        StrTmp = StrTmp.replace( "转", "p" );
        return StrTmp;
    }

    /**
     * 当前点击点是否在视图中
     */
    public static boolean isInsideView( MotionEvent event, View view ) {
        if ( view != null && event != null ) {
            float eventX = event.getRawX();
            float eventY = event.getRawY();

            int[] contentArray = new int[2];

            Rect contentRect = new Rect();
            view.getLocationOnScreen( contentArray );
            view.getDrawingRect( contentRect );
            contentRect.offsetTo( contentArray[0], contentArray[1] );

            return contentRect.contains( ( int ) eventX, ( int ) eventY );
        }

        return false;
    }

    public static String getChannel( Context appContext ) {
        String channel = "";
        try {
            final ApplicationInfo appInfo = appContext.getPackageManager().getApplicationInfo( appContext.getPackageName(), PackageManager.GET_META_DATA );
            Bundle configBundle = appInfo.metaData;
            if ( null != configBundle ) {
                channel = configBundle.getString( "com.elegant.analytics.AnalyticsConfig.Channel", "" );
            }
        } catch ( final PackageManager.NameNotFoundException e ) {
            e.printStackTrace();
        }
        return channel;
    }

    public static File getDiskCacheDir( Context context, String uniqueName ) {
        String cachePath;
        if ( Environment.MEDIA_MOUNTED.equals( Environment
                .getExternalStorageState() ) && context.getExternalCacheDir() != null ) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File( cachePath + File.separator + uniqueName );
    }

    public static File saveBitmap( Bitmap source, String targetPath ) {
        File file = new File( targetPath, System.currentTimeMillis() + ".jpg" );
        if ( !file.getParentFile().exists() ) {
            file.mkdirs();
        }
        try {
            FileOutputStream fos = new FileOutputStream( file );
            source.compress( Bitmap.CompressFormat.JPEG, 100, fos );
            fos.flush();
            fos.close();
        } catch ( Exception e ) {
            return null;
        }
        return file;
    }

    public static void showKeyBoard( Context context ) {
        try {
            InputMethodManager imm = ( InputMethodManager ) context.getSystemService( Context.INPUT_METHOD_SERVICE );
            if ( imm != null ) {
                imm.toggleSoftInput( 0, InputMethodManager.HIDE_NOT_ALWAYS );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

    public static void hiddenKeyBoard( Context context, EditText editText ) {
        try {
            InputMethodManager imm = ( InputMethodManager ) context.getSystemService( Context.INPUT_METHOD_SERVICE );
            if ( imm != null ) {
                imm.hideSoftInputFromWindow( editText.getWindowToken(), 0 );
            }
        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

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

    /**
     * 判断微信是否已安装
     *
     * @param context
     * @return
     */
    public static boolean isWeichatAvailable( Context context ) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List< PackageInfo > pinfo = packageManager.getInstalledPackages( 0 );// 获取所有已安装程序的包信息
        if ( pinfo != null ) {
            for ( int i = 0; i < pinfo.size(); i++ ) {
                String pn = pinfo.get( i ).packageName;
                if ( pn.equals( "com.tencent.mm" ) ) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * 判断qq是否已安装
     *
     * @param context
     * @return
     */
    public static boolean isQQAvailable( Context context ) {
        final PackageManager packageManager = context.getPackageManager();
        List< PackageInfo > pinfo = packageManager.getInstalledPackages( 0 );
        if ( pinfo != null ) {
            for ( int i = 0; i < pinfo.size(); i++ ) {
                String pn = pinfo.get( i ).packageName;
                if ( pn.equals( "com.tencent.mobileqq" ) ) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String getVersionName( Context context, String packageName ) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo( packageName, 0 );
            return packInfo.versionName;
        } catch ( Exception e ) {
            return "";
        }
    }

    public static int getVersionCode( Context context, String packageName ) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo( packageName, 0 );
            return packInfo.versionCode;
        } catch ( Exception e ) {
            return 0;
        }
    }

}
