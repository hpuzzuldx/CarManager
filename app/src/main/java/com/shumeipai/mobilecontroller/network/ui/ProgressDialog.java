package com.shumeipai.mobilecontroller.network.ui;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


public class ProgressDialog {

    private static final String TAG = "ProgressDialog";

    public static final String LOADING_DIALOG_TAG = "loading_dialog_tag";

    private ProgressDialogFragment mProgressDialogFragment;
    private FragmentManager mFragmentManager;

    public void showLoadingDialog( @NonNull Context context, CharSequence loadingMassage) {
        showLoadingDialog(context, loadingMassage, true);
    }

    public void showLoadingDialog( @NonNull Context context, CharSequence loadingMassage, boolean cancelable) {
        showLoadingDialog(context, loadingMassage, cancelable, false);
    }

    public void showLoadingDialog( Context context, CharSequence loadingMassage, boolean cancelable, boolean cancelableOnTouchOutSide) {
        if (context != null && context instanceof FragmentActivity )
            mFragmentManager = ((FragmentActivity) context).getSupportFragmentManager();

        if (mFragmentManager == null)
            return;

        mProgressDialogFragment = new ProgressDialogFragment();
        mProgressDialogFragment.setContent(loadingMassage, cancelable, cancelableOnTouchOutSide);

        try {
            FragmentTransaction fragmentTransaction = mFragmentManager.beginTransaction();
            fragmentTransaction.add( mProgressDialogFragment, LOADING_DIALOG_TAG );
            fragmentTransaction.add(mProgressDialogFragment, LOADING_DIALOG_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        } catch ( IllegalStateException e) {
            //Logger.e(TAG, e, e.getMessage());
            mFragmentManager = null;
            mProgressDialogFragment = null;
        }
    }

    public void removeLoadingDialog() {
        if (mProgressDialogFragment != null) {
            try {
                mProgressDialogFragment.dismissAllowingStateLoss();
            } catch ( Exception e) {
               // Logger.e(TAG, "dialog 取消异常");
            } finally {
                mProgressDialogFragment = null;
                mFragmentManager = null;
            }
        }
    }

}

