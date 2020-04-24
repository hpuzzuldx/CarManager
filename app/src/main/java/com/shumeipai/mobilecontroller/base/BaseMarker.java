package com.shumeipai.mobilecontroller.base;

import android.view.View;

import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.animation.Animation;

public class BaseMarker {
    protected MapView mMapView;
    protected MarkerOptions markerOptions = null;
    protected Marker mMarker;
    private LatLng lastLatLng;

    public BaseMarker(MapView mapView, LatLng latLng, int resId) {
        this.mMapView = mapView;
        initOptions(latLng, resId);
    }

    public BaseMarker(MapView mapView) {
        this.mMapView = mapView;
    }

    private void initOptions(LatLng latLng, int resId) {
        initOptions(latLng, BitmapDescriptorFactory.fromResource(resId));
    }

    protected void initOptions(LatLng latLng, BitmapDescriptor bitmapDescriptor) {
        if (latLng == null) {
            markerOptions = new MarkerOptions().icon(bitmapDescriptor).anchor(getAnchorX(), getAnchorY());
        } else {
            markerOptions = new MarkerOptions().icon(bitmapDescriptor).position(latLng).anchor(getAnchorX(), getAnchorY());
        }
        //设置infoWindow的位置偏移
        markerOptions.setInfoWindowOffset(0, -6);
    }

    public Marker getMarker() {
        return mMarker;
    }

    public void setMarker(Marker mMarker) {
        this.mMarker = mMarker;
    }

    /**
     * 将对象添加到地图
     */
    public void addToMap() {
        if (mMarker == null && markerOptions != null && mMapView != null && markerOptions.getPosition() != null
                && markerOptions.getPosition().latitude != 0) {
            markerOptions.position(markerOptions.getPosition());
            mMarker = mMapView.getMap().addMarker(markerOptions);
        }
    }

    public void addRestToMap() {
        if (markerOptions != null && mMapView != null && markerOptions.getPosition() != null
                && markerOptions.getPosition().latitude != 0) {
            markerOptions.position(markerOptions.getPosition());
            mMarker = mMapView.getMap().addMarker(markerOptions);
        }
    }

    public void addToMap(String title, String snippet) {
        if (mMarker == null && markerOptions != null && mMapView != null && markerOptions.getPosition() != null
                && markerOptions.getPosition().latitude != 0) {
            markerOptions.position(markerOptions.getPosition()).title(title).snippet(snippet);
            mMarker = mMapView.getMap().addMarker(markerOptions);
        }
    }

    public LatLng getPosition() {
        if (mMarker != null) {
            return mMarker.getPosition();
        }
        return null;
    }

    public MarkerOptions getMarkerOptions() {
        return markerOptions;
    }

    public void setMarkerOptions(MarkerOptions markerOptions1) {
        markerOptions = markerOptions1;
    }

    /**
     * 更新Marker坐标
     *
     * @param latLng 更新的坐标
     */
    public void updatePosition(LatLng latLng) {
        if (lastLatLng != null
                && Math.abs(lastLatLng.latitude - latLng.latitude) < 0.00001
                && Math.abs(lastLatLng.longitude - latLng.longitude) < 0.00001) {
            // 优化，避免不必要的 Marker 更新
            lastLatLng = latLng;
            return;
        }

        if (mMarker == null) {
            initOptions(latLng, markerOptions.getIcon());
            addToMap();
        } else {
            mMarker.setPosition(latLng);
        }

        lastLatLng = latLng;
    }

    public void removeSelf() {
        if (mMarker != null) {
            mMarker.remove();
            mMarker = null;
        }
    }

    /**
     * 设置图标正北角度
     *
     * @param angle
     */
    public void setRotate(float angle) {
        if (mMarker != null) {
            mMarker.setRotateAngle(angle);
        }
    }

    /**
     * 中心点X方向偏移比例
     *
     * @return
     */
    protected float getAnchorX() {
        return 0.5f;
    }

    /**
     * 中心点Y方向偏移比例
     *
     * @return
     */
    protected float getAnchorY() {
        return 0.5f;
    }


    /**
     * 判断与点击的marker是否相等
     *
     * @param marker
     * @return
     */
    public boolean isSameMarker(Marker marker) {
        if (null != marker && null != mMarker && mMarker.equals(marker)) {
            return true;
        }
        return false;
    }

    public void showInfoWindow() {
        if (mMarker != null) {
            mMarker.showInfoWindow();
        }
    }

    public void hideInfoWindow() {
        if (mMarker != null) {
            mMarker.hideInfoWindow();
        }
    }

    /**
     * 获取marker当前坐标
     *
     * @return
     */
    protected LatLng getMarkerLatLng() {
        if (null != mMarker) {
            return mMarker.getPosition();
        }
        return null;
    }

    public void setObject(Object object) {
        if (mMapView != null && mMarker != null) {
            mMarker.setObject(object);
        }
    }

    public <T> T getObject() {
        if (mMarker != null) {
            return ((T) mMarker.getObject());
        }
        return null;
    }

    /**
     * 设置marker是否显示
     *
     * @param isShow
     */
    public void setVisible(boolean isShow) {
        if (mMarker != null) {
            mMarker.setVisible(isShow);
        }
    }

    /**
     * 设置marker层级
     */
    public void setToTop() {
        if (mMarker != null) {
            mMarker.setToTop();
        }
    }

    public Marker getOriginMarker() {
        if (mMarker != null) {
            return mMarker;
        }
        return null;
    }

    public void setIcon(View view) {
        if (mMarker != null) {
            mMarker.setIcon(BitmapDescriptorFactory.fromView(view));
        }
    }

    public void setAnimation(Animation animation) {
        if (getOriginMarker() != null) {
            getOriginMarker().setAnimation(animation);
        }

    }

    public void startAnimation() {
        if (getOriginMarker() != null) {
            getOriginMarker().startAnimation();
        }
    }
}

