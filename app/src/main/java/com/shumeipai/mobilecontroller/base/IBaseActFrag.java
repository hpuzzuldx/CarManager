package com.shumeipai.mobilecontroller.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;


public interface IBaseActFrag {

    void openActivity(Class<? extends Activity> cls);

    void openActivity(Class<? extends Activity> cls, Bundle bundle);

    void openActivity(String url, Bundle bundle);

    void openActivityForResult(Class<? extends Activity> cls, Bundle bundle, int requestCode);

    void backForResult(int resultCode, Bundle bundle);

    void backToActivity(Class<? extends Activity> cls, Bundle bundle);

    Context getContext();

    boolean isVisibleToUser();

    boolean isKilled();

    void showToast(CharSequence toast);

    void hideInputMethod(View view);

    void showInputMethod(View view);

}
