package com.shumeipai.mobilecontroller.network;

import com.shumeipai.mobilecontroller.network.constant.NetConfig;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpParams extends BaseParams {
    public HttpParams() {
    }

    public HttpParams( Map< String, Object > source) {
        super(source);
    }

    public HttpParams( Object... keysAndValues) {
        super(keysAndValues);
    }

    public HttpParams( String key, Object value) {
        super(key, value);
    }

    public List<BasicNameValuePair> getParamList() {
        List<BasicNameValuePair> list = getParamsList();
        Collections.sort(list, new KVPairComparator());
        return list;
    }

    public String getSortedParamsString() {
        List<BasicNameValuePair> list = getParamList();
        StringBuilder sb = new StringBuilder(NetConfig.instance().getSignaturePrefix());
        for (BasicNameValuePair basicNameValuePair : list) {
            sb.append(basicNameValuePair.getName());
            sb.append(basicNameValuePair.getValue());
        }
        return sb.toString();
    }

    public Map< String, Object > getParamsMap() {
        List<BasicNameValuePair> list = getParamList();
        HashMap< String, Object > map = new HashMap< String, Object >();
        for (BasicNameValuePair basicNameValuePair : list) {
            map.put(basicNameValuePair.getName(), basicNameValuePair.getValue());
        }
        return map;
    }

    public String getSortedUrlParamsString() {
        List<BasicNameValuePair> list = getParamList();
        StringBuilder sb = new StringBuilder();
        for (BasicNameValuePair basicNameValuePair : list) {
            String key = basicNameValuePair.getName();
            Object value = basicNameValuePair.getValue();
            if (sb.length() > 0)
                sb.append("&");
            sb.append(key);
            sb.append("=");
            sb.append(value);
        }
        return sb.toString();
    }

    private class KVPairComparator implements Comparator<BasicNameValuePair> {
        public int compare(BasicNameValuePair pairA, BasicNameValuePair pairB) {
            return pairA.getName().compareTo(pairB.getName());
        }
    }
}
