package com.shumeipai.mobilecontroller.utils;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.shumeipai.mobilecontroller.R;

import java.text.DecimalFormat;

import static com.amap.api.maps.model.MyLocationStyle.LOCATION_TYPE_LOCATE;

/**
 * Created by generalsma on 2018/5/9 下午8:01.
 */
public class MapUtils {

    public static final float SCALE_500M_ZOOM = 15.31662f;
    public static final int SCALE_1KM_ZOOM = 14;
    public static final int SCALE_2KM_ZOOM = 13;
    public static final int SCALE_5KM_ZOOM = 12;
    public static final int SCALE_10KM_ZOOM = 11;
    public static final int SCALE_20KM_ZOOM = 10;
    public static final int SCALE_30KM_ZOOM = 9;

    public static boolean isRightLatLng(double lat, double lng) {
        return lat > 0 && lat < 90 && lng > 0 && lng < 180;
    }

    public static boolean isRightLatLng(LatLng latLng) {
        if (latLng == null) {
            return false;
        }
        return isRightLatLng(latLng.latitude, latLng.longitude);
    }

    /**
     * 计算地图上两点之间的直线距离，单位米
     *
     * @param from
     * @param to
     * @return
     */
    public static double getDistance(LatLng from, LatLng to) {
        if (from == null || to == null) {
            return 0;
        }
        return AMapUtils.calculateLineDistance(from, to);

    }

    /**
     * 计算地图上两点之间的绝对直线距离，单位米
     *
     * @param from
     * @param to
     * @return
     */
    public static double getAbsDistance(LatLng from, LatLng to) {
        if (from == null || to == null) {
            return 0;
        }
        return Math.abs(AMapUtils.calculateLineDistance(from, to));

    }

    public static double getDistanceToCurrentLocation(LatLng from) {
        return getDistance(NewbeeLocationClient.getLastKnowPoint(), from);
    }

    public static int strategyConvert(boolean congestion,
                                      boolean avoidspeed,
                                      boolean cost,
                                      boolean hightspeed,
                                      boolean multipleRoute) {
        byte var6;
        try {
            switch (Integer.parseInt((congestion ? "1" : "0") + (avoidspeed ? "1" : "0") + (cost ? "1" : "0") + (hightspeed ? "1" : "0"))) {
                case 0:
                    var6 = 10;
                    break;
                case 1:
                    var6 = 19;
                    break;
                case 10:
                    var6 = 14;
                    break;
                case 11:
                    throw new IllegalArgumentException("高速优先与避免收费不能同时为true");
                case 100:
                    var6 = 13;
                    break;
                case 101:
                    throw new IllegalArgumentException("高速优先与不走高速不能同时为true");
                case 110:
                    var6 = 16;
                    break;
                case 1000:
                    var6 = 12;
                    break;
                case 1001:
                    var6 = 20;
                    break;
                case 1010:
                    var6 = 17;
                    break;
                case 1100:
                    var6 = 15;
                    break;
                case 1110:
                    var6 = 18;
                    break;
                default:
                    var6 = 0;
            }
        } catch (Exception var5) {
            var5.printStackTrace();
            var6 = 0;
        }
        if (!multipleRoute) {
            var6 += 50;
        }
        return var6;
    }

    public static Point covertLatLngToScreenPoint(AMap aMap, LatLng latLng) {
        try {
            return aMap.getProjection().toScreenLocation(latLng);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Point();
    }

    public static MyLocationStyle getMainMyselfMakerLocationStyle(Context context) {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromView(generateMarkerView(context));
        myLocationStyle.myLocationIcon(bitmapDescriptor);// 设置小蓝点的图标
        myLocationStyle.myLocationType(LOCATION_TYPE_LOCATE);
        myLocationStyle.radiusFillColor(context.getResources().getColor( R.color.transparent));
        myLocationStyle.anchor(0.5f, 1.0f);
        myLocationStyle.strokeWidth(0);
        return myLocationStyle;
    }

    private static View generateMarkerView(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.my_self_marker, null);
        return view;
    }

    public static String formatDis(int mStr){
        String resultStr = "";
        try {
            if (mStr < 1000){
                resultStr = mStr + "M";
            }else{
                resultStr = formatPrice((double)mStr/((double) 1000)) + "KM";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultStr;
    }

    public static String formatPrice(double price){
        try {
            DecimalFormat df = new DecimalFormat("0.0");
            String forData = df.format(price);
            return forData;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return price+"";
    }

}
