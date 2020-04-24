package com.shumeipai.mobilecontroller.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.MyLocationStyle;
import com.shumeipai.mobilecontroller.R;

import java.util.List;

import static com.amap.api.maps.model.MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER;

public class MapStyleUtils {

    public static final int MARKER_LEVEL_HIGH = 10;
    public static final int MARKER_LEVEL_LOW = 100;

    public static LatLngBounds calculateBounds(List<LatLng> latLngs) {
        if (latLngs == null || latLngs.isEmpty()) {
            return null;
        }
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        for (LatLng latLng : latLngs) {
            builder = builder.include(latLng);
        }
        return builder.build();
    }

    public static LatLng newLatlng(LatLng latLng, double latOffset, double lngOffset) {
        return new LatLng(latLng.latitude + latOffset, latLng.longitude + lngOffset);
    }

    /**
     * {@link MyLocationStyle.LOCATION_TYPE_SHOW} 只定位一次。
     * {@link MyLocationStyle.LOCATION_TYPE_LOCATE} 定位一次，且将视角移动到地图中心点。
     * {@link MyLocationStyle.LOCATION_TYPE_FOLLOW} 连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
     * {@link MyLocationStyle.LOCATION_TYPE_MAP_ROTATE} 连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
     * {@link MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE} 连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
     * 以下三种模式从5.1.0版本开始提供
     * {@link MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER }连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
     * {@link MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER} 连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
     * {@link MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER} 连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。
     */

    /**
     * 定位图标样式
     *
     * @return
     */
    public static MyLocationStyle getMyLocationStyle(Context context) {
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_loc_person_marker);
        Bitmap marker = BitmapHelper.rotateBitmap(180, bitmap);
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(marker));// 设置小蓝点的图标
        myLocationStyle.myLocationType(LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        myLocationStyle.radiusFillColor(Color.parseColor("#3359D8FF"));
        myLocationStyle.strokeWidth(0);
        bitmap.recycle();
        marker.recycle();
        return myLocationStyle;
    }

}

