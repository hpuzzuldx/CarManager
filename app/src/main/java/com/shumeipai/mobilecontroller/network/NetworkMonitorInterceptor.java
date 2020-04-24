package com.shumeipai.mobilecontroller.network;

import android.content.Context;

import com.shumeipai.mobilecontroller.network.constant.NetConfig;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;


final class NetworkMonitorInterceptor implements Interceptor {
    private static final String TAG = "NetworkMonitorInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Context appContext = NetConfig.instance().getAppContext();
        if(appContext != null && !CheckUtils.isNetworkConnected(appContext)){
            throw new NetworkUnavailableException("Network is unavailable");
        }
        return chain.proceed(chain.request());
    }
}
