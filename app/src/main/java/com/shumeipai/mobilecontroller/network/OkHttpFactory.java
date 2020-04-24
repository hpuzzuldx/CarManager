package com.shumeipai.mobilecontroller.network;

import com.shumeipai.mobilecontroller.network.constant.NetConfig;

import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

/**
 * <p>
 * Before getting an global instance of {@link OkHttpClient}, you can use {@link NetConfig} to set some
 * parameters of network. Note that network configuration must be first set before you get an instance of {@link OkHttpClient},
 * for the first time, otherwise configuration is invalid<p/>
 *
 */
public final class OkHttpFactory {

    private static volatile OkHttpClient sInstance;

    private OkHttpFactory() {
    }

    public static OkHttpClient getInstance() {
        if (sInstance == null) {
            synchronized (OkHttpClient.class) {
                if (sInstance == null) {
                    final OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.addInterceptor(new NetworkMonitorInterceptor());
                    builder.addInterceptor(new RequestLogInterceptor());
                    builder.addInterceptor(new ResponseLogInterceptor());
                    builder.hostnameVerifier(NetConfig.instance().getHostnameVerifier());
                    if ( NetConfig.instance().getSslContext() != null ) {
                        builder.sslSocketFactory( NetConfig.instance().getSslContext().getSocketFactory());
                    }
                    builder.connectTimeout(NetConfig.instance().getConnectTimeout(), TimeUnit.MILLISECONDS);
                    builder.readTimeout(NetConfig.instance().getReadTimeout(), TimeUnit.MILLISECONDS);
                    builder.writeTimeout(NetConfig.instance().getWriteTimeout(), TimeUnit.MILLISECONDS);
                    final Set<Interceptor> interceptors = NetConfig.instance().getInterceptors();
                    for(Interceptor interceptor : interceptors){
                        builder.addInterceptor(interceptor);
                    }

                    final Set<Interceptor> networkInterceptors = NetConfig.instance().getNetworkInterceptors();
                    for(Interceptor networkInterceptor : networkInterceptors){
                        builder.addNetworkInterceptor(networkInterceptor);
                    }

                    sInstance = builder.build();
                }
            }
        }

        return sInstance;
    }
}