package com.shumeipai.mobilecontroller.network;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ContextWrapper;
import android.view.View;
import android.widget.PopupWindow;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention( RetentionPolicy.SOURCE )
@Target( ElementType.PARAMETER )
@CallerRestrictTo( {
        Activity.class,
        Fragment.class,
        androidx.fragment.app.Fragment.class,
        View.class,
        Dialog.class,
        PopupWindow.class,
        ContextWrapper.class,
        Object.class
} )
public @interface CallerType {
}
