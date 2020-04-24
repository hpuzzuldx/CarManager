package com.shumeipai.mobilecontroller.utils;

/**
 * Created by wanghuateng on 2018/7/9.
 */

public interface OnCompressListener {
    void onCompressSuccess(byte[] data);

    void onCompressFailed(String msg);

    void onBeforeCompress();
}
