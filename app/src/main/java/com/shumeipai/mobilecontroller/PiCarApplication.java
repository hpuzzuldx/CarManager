package com.shumeipai.mobilecontroller;

import android.app.Application;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.shumeipai.mobilecontroller.network.AllAllowedHostnameVerifier;
import com.shumeipai.mobilecontroller.network.constant.NetConfig;
import com.shumeipai.mobilecontroller.network.utils.X509TrustManagerImpl;
import com.shumeipai.mobilecontroller.utils.Logger;
import com.shumeipai.mobilecontroller.utils.LogLevel;
import com.shumeipai.mobilecontroller.utils.NewbeeLocationClient;
import com.shumeipai.mobilecontroller.utils.TipToast;

import java.security.SecureRandom;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;

public class PiCarApplication extends Application {
    private static Application mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        Logger.init( LogLevel.VERBOSE );
        TipToast.init( mContext, ( context, message ) -> {
            if ( context == null ) {
                return null;
            }
            if ( TextUtils.isEmpty( message ) ) {
                return null;
            }
            LayoutInflater inflate = ( LayoutInflater ) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            View v = inflate.inflate( R.layout.toast_simple, null );
            TextView tv = v.findViewById( R.id.message );
            tv.setText( message );
            return v;
        } );
        NewbeeLocationClient.getInstance(this).start(2000L);
        try {
            SSLContext sc = getSslContext();
            NetConfig.instance().setSslContext( sc );
        } catch ( Exception e ) {
        }

        NetConfig.instance()
                .setHostnameVerifier( new AllAllowedHostnameVerifier() )
                .setLoggable(true);
    }

    public static Application getContext() {
        return mContext;
    }

    public static void setmContext(Application mContext) {
        PiCarApplication.mContext = mContext;
    }


    /**
     * 忽略 https 验证
     *
     * @return
     * @throws Exception
     */
    private static SSLContext getSslContext() throws Exception {
        SSLContext sc = null;
        sc = SSLContext.getInstance( "SSL" );
        sc.init( null, new TrustManager[]{new X509TrustManagerImpl()}, new SecureRandom() );
        return sc;
    }
}
