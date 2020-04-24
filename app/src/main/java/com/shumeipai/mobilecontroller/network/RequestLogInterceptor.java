package com.shumeipai.mobilecontroller.network;

import android.util.Log;

import com.shumeipai.mobilecontroller.network.constant.NetConfig;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

final class RequestLogInterceptor implements Interceptor {
    private static final String TAG = "RequestLogInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();
        boolean hasRequestBody = requestBody != null;

        String protocol = Protocol.HTTP_1_1.toString();
        if(chain.connection() != null && chain.connection().protocol() != null){
            protocol = chain.connection().protocol().toString();
        }

        StringBuilder logMsg = new StringBuilder();
        logMsg.append("--> ");
        logMsg.append(protocol).append(", ");
        logMsg.append(request.method()).append(", ");
        logMsg.append("Request Headers: ").append(request.headers()).append("\r\n");
        logMsg.append(request.url()).append("\r\n");
        if(hasRequestBody){
            logMsg.append("Content-Type: ").append(requestBody.contentType()).append(", ");
            logMsg.append("\r\nContent-Length: ").append(requestBody.contentLength());
            try {
                String body = null;
                Charset UTF8 = Charset.forName("UTF-8");
                Buffer buffer = new Buffer();
                requestBody.writeTo(buffer);
                Charset charset = UTF8;
                MediaType contentType = requestBody.contentType();
                if (contentType != null) {
                    charset = contentType.charset(UTF8);
                }
                if (charset != null) {
                    body = buffer.readString(charset);
                }
                logMsg.append("\r\nContent-body: ").append(body);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        logMsg.append("\r\n<-- end http request");

        if(NetConfig.instance().isLoggable()){
            Log.d(TAG, logMsg.toString());
        }

        return chain.proceed(request);
    }
}
