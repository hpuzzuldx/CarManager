package com.shumeipai.mobilecontroller.network;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

/**
 * @author congtaowang
 * @since 2019-08-30
 * <p>
 * 信任所有域名
 */
public class AllAllowedHostnameVerifier implements HostnameVerifier {

    @Override
    public boolean verify( String hostname, SSLSession session ) {
        return true;
    }
}
