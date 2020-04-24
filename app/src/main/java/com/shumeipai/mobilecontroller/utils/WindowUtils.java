package com.shumeipai.mobilecontroller.utils;

import android.content.Context;

public class WindowUtils {

    public static int getStatusBarHeight( Context context ) {
        if ( context == null ) {
            return 0;
        } else {
            int result = 0;
            int resourceId = context.getResources().getIdentifier( "status_bar_height", "dimen", "android" );
            if ( resourceId > 0 ) {
                result = context.getResources().getDimensionPixelSize( resourceId );
            }

            return result;
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px( Context context, float dpValue ) {
        if ( context == null ) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return ( int ) ( dpValue * scale + 0.5f );
    }




    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip( Context context, float pxValue ) {
        if ( context == null ) {
            return 0;
        }
        final float scale = context.getResources().getDisplayMetrics().density;
        return ( int ) ( pxValue / scale + 0.5f );
    }


    public static String getScreenPixels( Context context ) {
        if ( context == null ) {
            return "";
        }
        return getScreenWidth( context ) + "*" + getScreenHeight( context );
    }

    public static int getScreenWidth( Context context ) {
        if ( context == null ) {
            return 0;
        }
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight( Context context ) {
        if ( context == null ) {
            return 0;
        }
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    public static int getScreenDpi( Context context ) {
        if ( context == null ) {
            return 0;
        }
        return context.getResources().getDisplayMetrics().densityDpi;
    }
}
