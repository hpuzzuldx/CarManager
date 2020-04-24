package com.shumeipai.mobilecontroller.network;

import android.text.TextUtils;

/**
 * Created by congtaowang on 2018/10/21.
 */
public class HttpParamsEx extends HttpParams {

    private static final String TAG = "HttpParamsEx";

    @Override
    public BaseParams put( String key, Object value ) {

        if ( !TextUtils.isEmpty( key ) ) {
            if ( value == null ) {
               // Logger.e( TAG, "%s with illegal value", key );
            }
        }
        return super.put( key, value );
    }
}
