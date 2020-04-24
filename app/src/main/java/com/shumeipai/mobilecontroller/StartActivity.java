package com.shumeipai.mobilecontroller;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.shumeipai.mobilecontroller.utils.SpStorage;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;

public class StartActivity extends AppCompatActivity {
    private String host="192.168.137.1";
    private int port=50;

    private TextView textViewIP;
    private TextView textViewPort;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        //Get the last IP and port value, and show them
        getInfoFromSharedPreference();
        textViewIP=(TextView)findViewById(R.id.carIP);
        textViewPort=(TextView)findViewById(R.id.carPort);
        textViewIP.setText(host);
        textViewPort.setText(String.valueOf(port));
        Button buttonEnter=(Button)findViewById(R.id.enter);
        buttonEnter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RxPermissions permissions = new RxPermissions(StartActivity.this);
                if (!permissions.isGranted(Manifest.permission.ACCESS_COARSE_LOCATION)){
                    requestNeedPermission();
                }
                host=textViewIP.getText().toString();
                port=Integer.parseInt(textViewPort.getText().toString());
                Intent startMainActivityIntent=new Intent(StartActivity.this,MainActivity2.class);
                startMainActivityIntent.putExtra("tcpHost",host);
                startMainActivityIntent.putExtra("tcpPort",port);
                startActivity(startMainActivityIntent);
                //Save the ip and port values
                setInfoFromSharedPreference();
                finish();
            }
        });

       requestNeedPermission();
    }

    private void requestNeedPermission() {
        RxPermissions permissions = new RxPermissions(StartActivity.this);
        permissions.setLogging(true);
        permissions.requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_COARSE_LOCATION)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.name.equalsIgnoreCase(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                            if (permission.granted) {//同意后调用

                            } else if (permission.shouldShowRequestPermissionRationale){//禁止，但没有选择“以后不再询问”，以后申请权限，会继续弹出提示

                            }else {//禁止，但选择“以后不再询问”，以后申请权限，不会继续弹出提示

                            }
                        }
                        if (permission.name.equalsIgnoreCase(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                            if (permission.granted) {

                            }else if (permission.shouldShowRequestPermissionRationale){

                            } else {

                            }
                        }
                        if (permission.name.equalsIgnoreCase(Manifest.permission.ACCESS_COARSE_LOCATION)) {
                            if (permission.granted) {

                            }else if (permission.shouldShowRequestPermissionRationale){

                            } else {

                            }
                        }
                    }
                });
    }

    private void getInfoFromSharedPreference(){
        SharedPreferences preferences=getSharedPreferences("connectPara",MODE_PRIVATE);
        host=preferences.getString("IP","192.168.1.1");
        port=preferences.getInt("Port",50);

    }

    private void setInfoFromSharedPreference(){
        SharedPreferences.Editor editor=getSharedPreferences("connectPara",MODE_PRIVATE).edit();
        editor.putString("IP",host);
        editor.putInt("Port",port);
        editor.apply();
        SpStorage.setIP(host+"");
        SpStorage.setPort(port+"");

    }
}
