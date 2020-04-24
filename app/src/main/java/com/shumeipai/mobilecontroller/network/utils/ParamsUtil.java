package com.shumeipai.mobilecontroller.network.utils;

import android.os.Build;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.collection.ArrayMap;
import com.shumeipai.mobilecontroller.network.utils.GsonUtil;


import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Set;

import okhttp3.RequestBody;

public class ParamsUtil {

    private static final String TAG = "ParamsUtil";



    public static RequestBody convert( Map< String, Object > map ) {
        String json = GsonUtil.getGson().toJson( map );
        //Logger.d( TAG, "request params: %s", json );
        RequestBody requestBody = RequestBody.create( okhttp3.MediaType.parse( "application/json; charset=utf-8" ), json );
        return requestBody;
    }

    /**
     * post 请求only
     *
     * @param url            地址
     * @param params         参数 （公共+业务+签名）
     * @param businessParams query 串中不需要保留的参数，一般为业务参数
     * @return
     */
    public static String toQueryUrl( @NonNull String url, Map< String, Object > params, Map< String, Object > businessParams ) {

        if ( TextUtils.isEmpty( url ) ) {
            return url;
        }

        params = diff( params, businessParams );
        if ( params == null || params.isEmpty() ) {
            return url;
        }
        final Set< String > keys = params.keySet();
        StringBuilder builder = new StringBuilder();
        for ( String key : keys ) {
            if ( TextUtils.isEmpty( key ) ) {
               // Logger.w( TAG, "key is illegal" );
                continue;
            }

            final Object value = params.get( key );
            if ( value == null ) {
               // Logger.w( TAG, "%s - value is illegal", key );
                continue;
            }
            String targetValue = value.toString();
            try {
                targetValue = URLEncoder.encode( targetValue, "utf-8" );
            } catch ( UnsupportedEncodingException e ) {
                targetValue = value.toString();
            }
            builder.append( key ).append( "=" ).append( targetValue ).append( "&" );
        }
        String queryString = builder.toString();
        if ( queryString.endsWith( "&" ) ) {
            queryString = queryString.substring( 0, queryString.length() - 1 );
        }

        if ( !url.endsWith( "?" ) ) {
            url += "?";
        }
        return url + queryString;
    }

    /**
     * @param parent 全部参数
     * @param child  业务参数
     * @return
     */
    public static Map< String, Object > diff( Map< String, Object > parent, Map< String, Object > child ) {
        if ( parent == null || parent.isEmpty() || child == null || child.isEmpty() ) {
            return parent;
        }
        for ( String key : child.keySet() ) {
            if ( TextUtils.isEmpty( key ) || TextUtils.equals( "extra_id", key ) ) {
                continue;
            }
            parent.remove( key );
        }
        return parent;
    }
}
