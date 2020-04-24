package com.shumeipai.mobilecontroller.network.ui;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class ProgressDialogFragment extends DialogFragment {

    /**
     * Progress显示的文字
     */
    private CharSequence mMessage;

    private boolean mCancelableOnTouchOutSide;

    /**
     * 设置dialog内容
     *
     * @param message    显示的文字
     * @param cancelable 是否可以被取消 例如back键取消
     */
    public void setContent( CharSequence message, boolean cancelable, boolean cancelableOnTouchOutSide) {
        mMessage = message;
        mCancelableOnTouchOutSide = cancelableOnTouchOutSide;
        setCancelable(cancelable);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState) {
        ProgressDialog dialog = new ProgressDialog(getActivity());
        dialog.setMessage(mMessage);
        dialog.setCanceledOnTouchOutside(mCancelableOnTouchOutSide);
        return dialog;
    }
}
