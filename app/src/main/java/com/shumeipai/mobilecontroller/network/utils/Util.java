package com.shumeipai.mobilecontroller.network.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;
import android.os.Looper;
import android.view.View;
import android.widget.PopupWindow;

import com.shumeipai.mobilecontroller.network.CallerNotAliveException;

public class Util {

    public static boolean checkAlive( Object caller ) {
        if ( caller instanceof Activity ) {
            return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 ? !( ( Activity ) caller ).isDestroyed() : !( ( Activity ) caller ).isFinishing();
        } else if ( caller instanceof Fragment ) {
            return ( ( Fragment ) caller ).isAdded();
        } else if ( caller instanceof androidx.fragment.app.Fragment ) {
            return ( ( androidx.fragment.app.Fragment ) caller ).isAdded();
        } else if ( caller instanceof View ) {
            return true;
        } else if ( caller instanceof Dialog ) {
            return ( ( Dialog ) caller ).getWindow() != null;
        } else if ( caller instanceof PopupWindow ) {
            return ( ( PopupWindow ) caller ).getContentView() != null;
        }
        return caller != null;
    }

    public static void assertCallerAlive( Object caller ) throws CallerNotAliveException {
        if ( !checkAlive( caller ) ) {
            throw new CallerNotAliveException( "Caller is not alive any more" );
        }
    }

    public static Context getContext( Object object ) {
        if ( object instanceof Activity ) {
            return ( Activity ) object;
        } else if ( object instanceof Fragment ) {
            return ( ( Fragment ) object ).getActivity();
        } else if ( object instanceof androidx.fragment.app.Fragment ) {
            return ( ( androidx.fragment.app.Fragment ) object ).getActivity();
        } else if ( object instanceof View ) {
            return ( ( View ) object ).getContext();
        } else if ( object instanceof Dialog ) {
            return ( ( Dialog ) object ).getContext();
        } else if ( object instanceof PopupWindow ) {
            if ( ( ( PopupWindow ) object ).getContentView() != null )
                return ( ( PopupWindow ) object ).getContentView().getContext();
            else return null;
        } else if ( object instanceof ContextWrapper ) {
            return ( ( ContextWrapper ) object ).getBaseContext();
        } else {
            return null;
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} if called on a thread other than the main
     * thread.
     */
    public static void assertMainThread() {
        if ( !isOnMainThread() ) {
            throw new IllegalArgumentException( "You must call this method on the main thread" );
        }
    }

    /**
     * Throws an {@link IllegalArgumentException} if called on the main thread.
     */
    public static void assertBackgroundThread() {
        if ( !isOnBackgroundThread() ) {
            throw new IllegalArgumentException( "You must call this method on a background thread" );
        }
    }

    /**
     * Returns {@code true} if called on the main thread, {@code false} otherwise.
     */
    public static boolean isOnMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * Returns {@code true} if called on a background thread, {@code false} otherwise.
     */
    public static boolean isOnBackgroundThread() {
        return !isOnMainThread();
    }
}
