package com.shumeipai.mobilecontroller.network;

import android.content.Context;
import android.view.KeyEvent;

import com.shumeipai.mobilecontroller.network.utils.Util;

import java.util.Collections;
import java.util.Map;


public class RequestOptions {
    private static final String DEFAULT_LOADING_MESSAGE = "正在加载中...";

    private Object caller;
    private Context context;
    private Map< String, Object > parameter;
    private CharSequence loadingMessage;
    private boolean loading = false;
    private boolean cancelable;
    private boolean cancelableOnTouchOutside;

    /**
     * Private constructor with some default initialization.
     */
    private RequestOptions( @CallerType Object caller ) {
        this.caller = caller;
        this.context = Util.getContext( caller );
        this.parameter = Collections.emptyMap();
        this.loadingMessage = DEFAULT_LOADING_MESSAGE;
        this.loading = false;
        this.cancelable = true;
        this.cancelableOnTouchOutside = false;
    }

    /**
     * Factory method to create an instance of {@link RequestOptions}
     *
     * <p>
     * Request caller can be any type of object. HOWEVER, if you want to show loading message
     * when request is ongoing or if you want to monitor caller's lifecycle in process of requesting
     * in order to avoid unexpected async exceptions, please use one of {@link android.app.Activity},
     * {@link android.app.Fragment},{@link androidx.fragment.app.Fragment},{@link android.view.View},
     * {@link android.app.Dialog} and {@link android.widget.PopupWindow} as caller<p/>
     *
     * <p>
     * Caller can be other types if loading message is not intended to be shown.<p/>
     *
     * <p>
     * It's strongly suggested that caller should have an context for better control of caller's lifecycle.<p/>
     */
    public static RequestOptions create( Object caller ) {
        return new RequestOptions( caller );
    }

    /**
     * Mutator for request parameters
     */
    public RequestOptions parameter( Map< String, Object > parameter ) {
        this.parameter = parameter;
        return this;
    }

    /**
     * Mutator for indicating whether loading message should be displayed while request is ongoing
     */
    public RequestOptions loading( boolean loading ) {
        this.loading = loading;
        return this;
    }

    /**
     * Mutator for loading message
     */
    public RequestOptions loadingMessage( CharSequence loadingMessage ) {
        this.loading = true;
        this.loadingMessage = loadingMessage;
        return this;
    }

    /**
     * Mutator for indicating that loading dialog is cancelable with the {@link KeyEvent#KEYCODE_BACK BACK} key.
     * Default value is true.
     */
    public RequestOptions cancelable( boolean cancelable ) {
        this.cancelable = cancelable;
        return this;
    }

    /**
     * Mutator for indicating that loading dialog is cancelable with touching outside of the dialog.
     * Default value is false.
     */
    public RequestOptions cancelableOnTouchOutside( boolean cancelableOnTouchOutside ) {
        this.cancelableOnTouchOutside = cancelableOnTouchOutside;
        return this;
    }

    public Object getCaller() {
        return caller;
    }

    public Context getContext() {
        return context;
    }

    public Map< String, Object > getParameter() {
        return parameter;
    }

    public CharSequence getLoadingMessage() {
        return loadingMessage;
    }

    public boolean isLoading() {
        return loading;
    }

    public boolean isCancelable() {
        return cancelable;
    }

    public boolean isCancelableOnTouchOutside() {
        return cancelableOnTouchOutside;
    }
}
