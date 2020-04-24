package com.shumeipai.mobilecontroller.network;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.shumeipai.mobilecontroller.R;


public class ProgressDialogExt extends Dialog {

    public ProgressDialogExt(@NonNull Context context, CharSequence message, boolean cancle) {
        super(context);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        message = TextUtils.isEmpty(message) ? "正在加载..." : message;
        setContentView(R.layout.layout_progress_dialog);
        ((TextView) findViewById(R.id.message)).setText(message);
        setCancelable(false);
        setCanceledOnTouchOutside(cancle);
    }
}

