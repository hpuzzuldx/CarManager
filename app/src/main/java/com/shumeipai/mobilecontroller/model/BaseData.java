package com.shumeipai.mobilecontroller.model;

import java.io.Serializable;

/**
 * 网络请求基类
 */
public class BaseData implements Serializable, Cloneable {
    public int code = -1;
    public String msg;
}
