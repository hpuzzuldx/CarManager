package com.shumeipai.mobilecontroller.network;

import android.util.Log;

import com.shumeipai.mobilecontroller.model.BaseData;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * Observer 的实现类
 * @param <T>
 */
public abstract class SubscribeImpl< T extends BaseData> implements Observer<T> {

    protected final RequestOptions mRequestOptions;

    private static final String TAG = "SubscribeImpl";
    private boolean mAutoTipMsg = true;
    private ProgressDialogExt mProgressDialog;

    public SubscribeImpl(RequestOptions requestOptions ) {
        mRequestOptions = requestOptions;
        if ( requestOptions != null && requestOptions.isLoading() ) {
            mProgressDialog = new ProgressDialogExt( requestOptions.getContext(), requestOptions.getLoadingMessage() ,mRequestOptions.isCancelable());
        }
    }

    public SubscribeImpl(RequestOptions requestOptions, boolean autoTipMsg ) {
        this( requestOptions );
        mAutoTipMsg = autoTipMsg;
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        if ( mRequestOptions.isLoading() && mProgressDialog != null ) {
            mProgressDialog.show();
        }
    }

    @Override
    public void onComplete() {
        onFinish();
    }

    private void onFinish() {
        if ( mProgressDialog != null && mRequestOptions.isLoading() ) {
            mProgressDialog.dismiss();
            mProgressDialog = null;
        }
    }

    @Override
    public void onError( Throwable e ) {
        onFinish();
        onError("error",-1);
        Log.e( TAG, "occur when net request." );
    }

    @Override
    public void onNext( T o ) {
        if ( o != null ) {
            if ( o.code != 0 ) {
                onError( o.msg, o.code );
            } else {
                onSuccess( o );
            }
        } else {
            onError( "error", -1 );
        }
    }

    public void onSuccess( T o ) {

    }

    public void onError( String message, int code ) {

    }
}
