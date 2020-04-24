package com.shumeipai.mobilecontroller.network;


import android.util.Log;

import com.shumeipai.mobilecontroller.network.constant.NetConfig;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Response;
import okhttp3.ResponseBody;


final class ResponseLogInterceptor implements Interceptor {
    private static final String TAG = "ResponseLogInterceptor";

    @Override
    public Response intercept( Chain chain ) throws IOException {
        long startTime = System.nanoTime();
        Response response = chain.proceed( chain.request() );
        long endTime = TimeUnit.NANOSECONDS.toMillis( System.nanoTime() - startTime );

        ResponseBody responseBody = response.body();
        String responseContent = null;
        String bodySize = null;
        MediaType contentType = null;
        boolean consumedResponse = false;

        StringBuilder logMsg = new StringBuilder();

        if ( responseBody != null ) {
            long contentLength = responseBody.contentLength();
            bodySize = contentLength != -1L ? contentLength + "-byte" : "unknown-length";
            contentType = responseBody.contentType();
            responseContent = responseBody.string();
            consumedResponse = true;
        }

        logMsg.append( "--> " );
        logMsg.append( response.code() ).append( " " );
        logMsg.append( response.message() ).append( " " );
        logMsg.append( response.protocol() ).append( " " );
        logMsg.append( response.request().url() ).append( "\r\n" );
        logMsg.append( "Response Content: " ).append( responseContent ).append( "\r\n" );
        logMsg.append( "Content-Type: " ).append( contentType ).append( ", " );
        logMsg.append( "Content-Length: " ).append( bodySize ).append( ", " );
        logMsg.append( " (" ).append( endTime ).append( "ms)" );
        logMsg.append( " <-- end http response" );

        if ( NetConfig.instance().isLoggable() ) {
            Log.d( TAG, logMsg.toString() );
        }

        return consumedResponse ?
                response.newBuilder().body( ResponseBody.create( contentType, responseContent ) ).build() :
                response;
    }
}