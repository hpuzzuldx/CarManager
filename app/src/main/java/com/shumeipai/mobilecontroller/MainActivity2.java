package com.shumeipai.mobilecontroller;


import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.shumeipai.mobilecontroller.statusbar.*;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.shumeipai.mobilecontroller.base.BaseMapFragment;
import com.shumeipai.mobilecontroller.utils.TipToast;
import com.shumeipai.mobilecontroller.utils.UiThreadHandler;

public class MainActivity2 extends AppCompatActivity {
    private String host;
    private int port;
    private BaseMapFragment mBaseMapFragment;
    private int mQuitClickCount = 0;

    public static void start(Context context) {
        Intent intent = new Intent(context, MainActivity2.class);
        if (!(context instanceof Activity)) {
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Eyes.setStatusBarStyle( this, true,Color.WHITE, false );
        setContentView(R.layout.activity_main2);
        mBaseMapFragment = BaseMapFragment.newInstance();
        addFragment(R.id.fragment_container,mBaseMapFragment,"mapFragment");
        initViews();
    }

    public void initViews(){

    }

    private void addFragment(int containerId, Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(containerId, fragment, tag)
                .commitAllowingStateLoss();
    }

    @Override
    protected void onDestroy() {
        try {
            super.onDestroy();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fullscreen(boolean enable) {

        if (enable) { //显示状态栏
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags |= WindowManager.LayoutParams.FLAG_FULLSCREEN;
            getWindow().setAttributes(lp);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        } else { //隐藏状态栏
            WindowManager.LayoutParams lp = getWindow().getAttributes();
            lp.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
            getWindow().setAttributes(lp);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

        }

    }

    @Override
    public void onBackPressed() {
        mQuitClickCount++;
        if (mQuitClickCount == 1) {
            TipToast.shortTip("再按一次退出应用");
            UiThreadHandler.postDelayed(() -> {
                mQuitClickCount = 0;
            }, 2000L);
        } else if (mQuitClickCount >= 2) {
            finish();
        } else {
            UiThreadHandler.postDelayed(() -> {
                mQuitClickCount = 0;
            }, 2000L);
        }
    }

}