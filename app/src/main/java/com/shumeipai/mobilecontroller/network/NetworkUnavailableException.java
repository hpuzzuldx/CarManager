package com.shumeipai.mobilecontroller.network;

import java.io.IOException;

import okhttp3.Interceptor;

/**
 * An exception occurs whenever network is not reachable, it's threw by {@link NetworkMonitorInterceptor}
 * during {@link NetworkMonitorInterceptor#intercept(Interceptor.Chain)} method.
 *
 */

public class NetworkUnavailableException extends IOException {

    public NetworkUnavailableException( String message) {
        super(message);
    }
}
