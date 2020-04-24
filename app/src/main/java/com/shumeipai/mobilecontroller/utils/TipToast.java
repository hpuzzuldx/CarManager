package com.shumeipai.mobilecontroller.utils;

/**
 * 2016/1/1 by congtaowang
 *
 * @Version 1.0
 */

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

/**
 * Init TipToast in your Application
 */
public final class TipToast {

    private static final String TAG = "TipToast";

    private static Toast sToast = null;
    private static final Object sSyncObject = new Object();
    private static Handler sHandler = null;
    private static Context sContext;
    private static ToastViewGenerator sGenerator;

    public static void init( Context context, ToastViewGenerator generator ) {
        TipToast.sContext = context;
        sHandler = new Handler( context.getMainLooper() );
        sGenerator = generator;
    }

    public static void recycle() {
        sContext = null;
        sHandler = null;
        if ( sToast != null ) {
            sToast.cancel();
            sToast = null;
        }
        sGenerator = null;
    }

    private static void tip( final String message, int duration ) {
        if ( !checkParams() ) {
            return;
        }
        if ( TextUtils.isEmpty( message ) ) {
            return;
        }
        new ToastThread( new StringToastRunnable( sContext, message, duration ) ).start();
    }

    private static void tip( final int msgId, int duration ) {
        if ( !checkParams() ) {
            return;
        }
        try {
            if ( TextUtils.isEmpty( ResourcesHelper.getString( sContext, msgId ) ) ) {
                return;
            }
        } catch ( Exception e ) {
            return;
        }
        tip( ResourcesHelper.getString( sContext, msgId ), duration );
    }

    private static boolean checkParams() {
        if ( sContext == null ) {
            Log.e( TAG, "context can't be null." );
            return false;
        }
        if ( sHandler == null ) {
            Log.e( TAG, "sHandler can't be null." );
            return false;
        }
        return true;
    }

    public static void tip( final String message ) {
        tip( message, Toast.LENGTH_SHORT );
    }

    public static void tip( final int msgId ) {
        tip( msgId, Toast.LENGTH_SHORT );
    }

    public static void longTip( String message ) {
        tip( message, Toast.LENGTH_LONG );
    }

    public static void longTip( int msgId ) {
        tip( msgId, Toast.LENGTH_LONG );
    }

    public static void shortTip( String message ) {
        tip( message, Toast.LENGTH_SHORT );
    }

    public static void shortTip( int msgId ) {
        tip( msgId, Toast.LENGTH_SHORT );
    }

    static class ToastThread extends Thread {
        public ToastThread( Runnable runnable ) {
            super( runnable );
        }

    }

    static class StringToastRunnable implements Runnable {

        Context context;
        String msg;
        int duration;

        public StringToastRunnable( Context context, String msg, int duration ) {
            this.context = context;
            this.msg = msg;
            this.duration = duration;
        }

        @Override
        public void run() {

            if ( sHandler == null ) {
                return;
            }

            sHandler.post( new Runnable() {

                @Override
                public void run() {
                    synchronized ( sSyncObject ) {

                        if ( context == null ) {
                            return;
                        }

                        if ( sToast != null ) {
                            sToast.cancel();
                        }

                        if ( sGenerator == null ) {
                            sToast = Toast.makeText( context, msg, duration );
                        }

                        if ( sGenerator == null ) {
                            sToast = Toast.makeText( context, msg, duration );
                        } else {
                            sToast = new Toast( context );
                            final View view = sGenerator.make( context, msg );
                            if ( view != null ) {
                                sToast.setView( view );
                                sToast.setDuration( duration );
                            } else {
                                sToast = Toast.makeText( context, msg, duration );
                            }
                        }
                        if ( sToast != null ) {
                            sToast.show();
                        }
                    }
                }
            } );
        }
    }

    public interface ToastViewGenerator {
        View make(Context context, String message);
    }

}
