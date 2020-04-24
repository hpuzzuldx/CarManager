package com.shumeipai.mobilecontroller.network;

import androidx.collection.ArrayMap;

import java.util.Map;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public final class RetrofitFactory {

    private static final Map< String, Retrofit> sRpcServiceMap = new ArrayMap< String,Retrofit>();

    private RetrofitFactory(){}

    public static synchronized Retrofit getInstance( String baseUrl) {
        Retrofit target = sRpcServiceMap.get(baseUrl);
        if(target == null){
            target = new Retrofit.Builder().
                    client(OkHttpFactory.getInstance()).
                    baseUrl(baseUrl).
                    addCallAdapterFactory( RxJava2CallAdapterFactory.create()).
                    addConverterFactory(GsonConverterFactory.create()).
                    build();
            sRpcServiceMap.put(baseUrl,target);
        }
        return target;
    }

}
