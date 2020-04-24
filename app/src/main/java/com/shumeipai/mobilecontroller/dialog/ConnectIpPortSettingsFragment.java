package com.shumeipai.mobilecontroller.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.shumeipai.mobilecontroller.R;
import com.shumeipai.mobilecontroller.base.BaseDialogFragment;
import com.shumeipai.mobilecontroller.utils.SpStorage;

public class ConnectIpPortSettingsFragment extends BaseDialogFragment {

    private Context context;
    private TextView textViewIP;
    private TextView textViewPort;

    public static final String TAG = "MediaShareDialogFragment";

    public static ConnectIpPortSettingsFragment newInstance() {
        Bundle args = new Bundle();
        ConnectIpPortSettingsFragment fragment = new ConnectIpPortSettingsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.setCanceledOnTouchOutside(false);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            if (dialog.getWindow() != null) {
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
        }

        return inflater.inflate(R.layout.setting_fragment_view, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews();
    }

    private void initViews() {
        textViewIP = getView().findViewById(R.id.carIP);
        textViewPort = getView().findViewById(R.id.carPort);

        String port = SpStorage.getPort();
        String ip = SpStorage.getIP();
        textViewIP.setText(ip);
        textViewPort.setText(port);

        getView().findViewById(R.id.enter).setOnClickListener(view -> {
            SpStorage.setIP(textViewIP.getText()+"");
            SpStorage.setPort(textViewPort.getText()+"");
            dissMisDialog();
        });

        getView().findViewById(R.id.out).setOnClickListener(view -> {
            dissMisDialog();
        });
    }

    private void dissMisDialog() {
        dismissAllowingStateLoss();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }
}
