package com.shumeipai.mobilecontroller.base;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.CoordinateConverter;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.shumeipai.mobilecontroller.R;
import com.shumeipai.mobilecontroller.dialog.ConnectIpPortSettingsFragment;
import com.shumeipai.mobilecontroller.model.BaseData;
import com.shumeipai.mobilecontroller.model.CMarkerData;
import com.shumeipai.mobilecontroller.model.UpLoadPointData;
import com.shumeipai.mobilecontroller.model.UpPoint;
import com.shumeipai.mobilecontroller.network.RequestOptions;
import com.shumeipai.mobilecontroller.network.ServiceFactoryHelper;
import com.shumeipai.mobilecontroller.network.SubscribeImpl;
import com.shumeipai.mobilecontroller.utils.ILocationChangedListener;
import com.shumeipai.mobilecontroller.utils.MapUtils;
import com.shumeipai.mobilecontroller.utils.MarkerFactory;
import com.shumeipai.mobilecontroller.utils.NewbeeLocation;
import com.shumeipai.mobilecontroller.utils.NewbeeLocationClient;
import com.shumeipai.mobilecontroller.utils.TipToast;
import com.shumeipai.mobilecontroller.utils.UiThreadHandler;
import com.shumeipai.mobilecontroller.widget.ClickNumberMarker;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * 右侧地图Fragment
 *
 * @author lidongxiu
 */
public class BaseMapFragment extends BaseFragment implements
        AMap.OnMarkerClickListener,
        ILocationChangedListener,
        AMap.OnCameraChangeListener,
        AMap.OnMapClickListener,
        NavigationView.OnNavigationItemSelectedListener {
    public static final String TAG = "BaseMapFragment";
    private MapView textureMapView;
    private TextView openNaviMode;
    private LinearLayout mNaviMenu;
    private NavigationView mNavigationView;
    private boolean openNaviModeState = false;
    private boolean showNaviState = false;

    private int defaultZoom = 18;
    private Context mContext;
    private AMap aMap;
    private double currentZoom = defaultZoom;
    private boolean mMapLoadSuccess = false;
    //private InfoWinAdapter mInfoWinAdapter;
    private ArrayList<Disposable> subscriptions;
    private boolean initSuccess = false;
    private int mapType = -1;
    private ArrayList<ClickNumberMarker> mNumberMarkerList = new ArrayList<>();
    List<LatLng> mLatLngs = new ArrayList<LatLng>();//数字点
    List<LatLng> mRunLatLngs = new ArrayList<LatLng>();//绘制的路径点
    private TextView mClearAllMarker;
    private TextView mUploadPoint;

    private final long LOOP_REDPAPER_INTERVAL = 10000;
    Runnable mCurrentLocationLoopRunnable = new Runnable() {
        @Override
        public void run() {
            getNewsCarPosition();
        }
    };
    private DrawerLayout drawerLayout;
    private View mShowAllMarker;
    private NewbeeLocation currentLocation;
    private int mCurrentMode = AMap.MAP_TYPE_NORMAL;
    private View naviCloseView;
    private boolean getPositonRe = false;

    private void getNewsCarPosition() {
        if (!getPositonRe)return;
        getCurrentPosition("sdfsldfjsd");
        UiThreadHandler.removeCallbacks(mCurrentLocationLoopRunnable);
        UiThreadHandler.postDelayed(mCurrentLocationLoopRunnable, LOOP_REDPAPER_INTERVAL);
    }

    private void getCurrentPosition(String time) {
        //处理upPoints
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(JSON,"{\"lastUploadTime\":"+time+"}");

        ServiceFactoryHelper.newCarLifeApiService().getCurrentPositon(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    new SubscribeImpl<BaseData>(RequestOptions.create(getContext()).loading(false)){
                        @Override
                        public void onSubscribe(Disposable d) {
                            super.onSubscribe(d);
                        }

                        @Override
                        public void onSuccess(BaseData o) {
                            super.onSuccess(o);
                        }

                        @Override
                        public void onError(String message, int code) {
                            super.onError(message, code);
                        }
                    }
                );
    }

    public static BaseMapFragment newInstance() {
        Bundle args = new Bundle();
        // args.putString( Constants.xxx, parmater);
        BaseMapFragment fragment = new BaseMapFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map_layout_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        initEvent();
        if (textureMapView != null) {
            textureMapView.onCreate(savedInstanceState);
        }

        initMap();
    }

    private void initMap() {
        if (textureMapView != null) {
            textureMapView.getMap().setOnMapLoadedListener(new AMap.OnMapLoadedListener() {
                @Override
                public void onMapLoaded() {
                    mMapLoadSuccess = true;
                }
            });

            aMap = textureMapView.getMap();
           // setMapMode(); 可以根据时间设置夜间模式
            aMap.setTrafficEnabled(false);
            aMap.setOnMapClickListener(this);
            aMap.setOnCameraChangeListener(BaseMapFragment.this);
            aMap.setOnMarkerClickListener(BaseMapFragment.this);

            UiSettings uiSettings = aMap.getUiSettings();
            uiSettings.setRotateGesturesEnabled(true);
            uiSettings.setTiltGesturesEnabled(false);
            uiSettings.setMyLocationButtonEnabled(false);
            uiSettings.setZoomControlsEnabled(true);
            uiSettings.setZoomPosition(defaultZoom);
            uiSettings.setCompassEnabled(true);
            uiSettings.setScaleControlsEnabled(true);

            MyLocationStyle myLocationStyle = new MyLocationStyle();
            myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
            aMap.setMyLocationStyle(myLocationStyle);
            aMap.getUiSettings().setMyLocationButtonEnabled(true);// 设置默认定位按钮是否显示
            aMap.getUiSettings().setGestureScaleByMapCenter(true);
            aMap.setMyLocationEnabled(true);//
            startReceiveLocation();
        }

    }

    public void reLoadMap(){
        if (aMap != null){
            aMap.clear();
        }

        if (textureMapView != null){
            textureMapView.invalidate();
        }
    }

    /**
     * 初始化
     */
    private void initView() {
        textureMapView = getView().findViewById(R.id.map_view);
        openNaviMode = getView().findViewById(R.id.open_close_navi_mode);
        naviCloseView = getView().findViewById(R.id.open_close_navi_show_mode);
        mNaviMenu = getView().findViewById(R.id.open_navi_mode_menu);
        mClearAllMarker = getView().findViewById(R.id.clear_all_marker);
        mUploadPoint = getView().findViewById(R.id.up_load_navi_point);
        mNavigationView = getView().findViewById(R.id.nav_view);
        mShowAllMarker = getView().findViewById(R.id.show_all_marker);
        mNavigationView.setItemIconTintList (null);
        View headerView = mNavigationView.getHeaderView (0);//获取头布局
        drawerLayout = getView().findViewById( R.id.drawer_layout );
        mNavigationView.setNavigationItemSelectedListener(this);

        naviCloseView.setOnClickListener(view -> {
            showNaviState = false;
            getPositonRe = false;
            naviCloseView.setVisibility(View.GONE);
            if (!openNaviModeState){
                openNaviMode.setText("打开航点选择");
                mNaviMenu.setVisibility(View.GONE);
            }else{
                openNaviMode.setText("关闭航点选择");
                mNaviMenu.setVisibility(View.VISIBLE);
            }
        });

        if (!openNaviModeState){
            openNaviMode.setText("打开航点选择");
            mNaviMenu.setVisibility(View.GONE);
        }else{
            openNaviMode.setText("关闭航点选择");
            mNaviMenu.setVisibility(View.VISIBLE);
        }

        mClearAllMarker.setOnClickListener(view -> {
            if (showNaviState)return;
            clearAllPointRequest();
        });

        mShowAllMarker.setOnClickListener(view -> {
            if (showNaviState)return;
            zoomToSpanWithCenter();
        });

        openNaviMode.setOnClickListener(view -> {
            if (showNaviState)return;
            if (openNaviModeState){
                clearAllPoint();
            }

            openNaviModeState = !openNaviModeState;
            if (!openNaviModeState){
                openNaviMode.setText("打开航点选择");
                mNavigationView.getMenu().findItem(R.id.nav_count).setTitle("打开航点选择");
                mNaviMenu.setVisibility(View.GONE);
            }else{
                openNaviMode.setText("关闭航点选择");
                mNavigationView.getMenu().findItem(R.id.nav_count).setTitle("关闭航点选择");
                mNaviMenu.setVisibility(View.VISIBLE);
            }
        });

        mUploadPoint.setOnClickListener(view -> {
            if (showNaviState)return;
            if (mLatLngs == null || mLatLngs.size() <= 1){
                TipToast.shortTip("选择航点不符合规则");
                return;
            }else{
             uploadPoint();
            }
        });
    }

    //隐藏显示menu
    //navigationView.getMenu ().setGroupEnabled (R.id.g1, true); 是否可以点击
     /* for (int i = 0; i < navigationView.getMenu ().size (); i++) {
        int id = navigationView.getMenu ().getItem (i).getItemId ();
        switch (id) {
            case R.id.favorite:
                Log.e (TAG, "initView: " + "favorite");
                navigationView.getMenu ().getItem (i).setVisible (false);
                break;
        }
    }*/

    public void clearAllPointRequest() {
        if (mNumberMarkerList == null || mNumberMarkerList.size() < 1){
            TipToast.shortTip("没有航点可以删除！");
            return;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("删除所有航点");
        builder.setMessage("确认删除吗？");
        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                clearAllPoint();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void initEvent() {

    }

    private void closeDrawerLayout(boolean close){
        if (drawerLayout != null){
            if (close && drawerLayout.isDrawerOpen(mNavigationView)){
                drawerLayout.closeDrawer(mNavigationView);
            }

            if (!close && !drawerLayout.isDrawerOpen(mNavigationView)){
                drawerLayout.openDrawer(mNavigationView);
            }
        }
    }

    /**
     * 清除所有绘制的点和线
     */
    private void clearAllPoint() {
        aMap.clear();
        mNumberMarkerList.clear();
        mLatLngs.clear();
        mRunLatLngs.clear();
    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        textureMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        textureMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        textureMapView.onSaveInstanceState(outState);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        textureMapView.onDestroy();
    }

    /* 定位 */
    protected void startReceiveLocation() {
        NewbeeLocationClient.getInstance(getContext()).addLocationListener(this);
        NewbeeLocationClient.getInstance(getContext()).start(2000L);
    }

    //只是不再监听，其实还是可以获取到最新的地理位置的
    protected void stopReceiveLocation() {
        NewbeeLocationClient.getInstance(getContext()).removeLocationListener(this);
    }

    @Override
    public void onLocationChanged(NewbeeLocation location) {
        if (location != null){
            Log.d(TAG,"==onLocationChanged===" +location.getLatitude()+"  "+location.getLongitude());
        }else{
            Log.d(TAG,"==onLocationChanged===" +" null ");
        }

        if (mMapLoadSuccess) {
            if (location != null && MapUtils.isRightLatLng(new LatLng(location.getLatitude(), location.getLongitude()))) {
                currentLocation = location;
            }
        }
    }

    private void moveLocatedPositionToCenter(double lat, double lng) {
        if (MapUtils.isRightLatLng(lat, lng)) {
            movePointToCenterWithZoom(new LatLng(lat, lng), defaultZoom);
        }
    }

    /**
     * 是否持续请求定位
     *
     * @return
     */
    protected boolean receiveLocationContinuous() {
        return true;
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        return true; //返回 “false”，除定义的操作之外，默认操作也将会被执行
    }

    protected void zoomTo(int zoomFactor) {
        if (textureMapView != null) {
            animateCameraUpdate(CameraUpdateFactory.zoomTo(zoomFactor));
        }
    }

    protected void movePointToCenter(LatLng latLng) {
        if (textureMapView != null) {
            animateCameraUpdate(CameraUpdateFactory.newLatLng(latLng));
        }
    }

    protected void movePointToCenterWithZoom(LatLng latLng, float zoom) {
        if (textureMapView != null) {
            animateCameraUpdate(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
            currentZoom = zoom;
        }
    }

    protected void showAllMarker(LatLngBounds latLngBounds, int marginLeft, int marginRight, int marginTop, int marginBottom) {
        if (textureMapView != null) {
            animateCameraUpdate(CameraUpdateFactory.newLatLngBoundsRect(latLngBounds, marginLeft, marginRight, marginTop, marginBottom));
        }
    }

    public void animateCameraUpdate(CameraUpdate cameraUpdate) {
        if (textureMapView != null && !isDetached()) {
            aMap.animateCamera(cameraUpdate);
        }
    }

    private void uploadPoint() {
        if (mLatLngs == null && mLatLngs.size() <= 1){
            return;
        }

        ArrayList<UpPoint> upPoints = new ArrayList<>();
        for (LatLng latLng : mLatLngs){
            LatLng tlatlng = coverLatlng(latLng);
            if (tlatlng != null){
                upPoints.add(new UpPoint(tlatlng.latitude, tlatlng.longitude));
            }
        }

        //处理upPoints
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        UpLoadPointData upLoadPointData = new UpLoadPointData();
        upLoadPointData.setPointList((ArrayList<LatLng>) mLatLngs);
        RequestBody body = RequestBody.create(JSON,new Gson().toJson(upLoadPointData));

        ServiceFactoryHelper.newCarLifeApiService().putSelectedPoints(body)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        new SubscribeImpl<BaseData>(RequestOptions.create(getContext()).loadingMessage("正在上传。。。").cancelable(false)){
                            @Override
                            public void onSubscribe(Disposable d) {
                                super.onSubscribe(d);
                            }

                            @Override
                            public void onSuccess(BaseData o) {
                                super.onSuccess(o);
                                showNaviState = true;
                                openNaviMode.setVisibility(View.GONE);
                                mNaviMenu.setVisibility(View.GONE);
                                naviCloseView.setVisibility(View.VISIBLE);
                                openNaviModeState = false;
                                getNewsCarPosition();
                                getPositonRe = true;
                                aMap.clear();
                                mLatLngs.clear();
                                mRunLatLngs.clear();
                                TipToast.shortTip("上传成功,即将进入轨迹展示。");
                            }

                            @Override
                            public void onError(String message, int code) {
                                super.onError(message, code);
                                TipToast.shortTip("上传失败");

                                //todo Test
                                showNaviState = true;
                                openNaviMode.setVisibility(View.GONE);
                                mNaviMenu.setVisibility(View.GONE);
                                naviCloseView.setVisibility(View.VISIBLE);
                                openNaviModeState = false;
                                getNewsCarPosition();
                                getPositonRe = true;
                                aMap.clear();
                                mLatLngs.clear();
                                mRunLatLngs.clear();
                                TipToast.shortTip("上传成功,即将进入轨迹展示。");
                            }
                        }

                );
    }

    public void addSubscription(Disposable subscription){
        if (subscription != null){
            if (subscriptions == null) subscriptions = new ArrayList<>();
            subscriptions.add(subscription);
        }
    }

    public boolean getInitSuccess(){
        return initSuccess;
    }

    @Override
    public void onMapClick(LatLng latLng) {
        if (!openNaviModeState)return;
        if (mNumberMarkerList == null) mNumberMarkerList = new ArrayList<>();
        CMarkerData cMarkerData = new CMarkerData(latLng.latitude, latLng.longitude);
        cMarkerData.setNumberIndex(mNumberMarkerList.size() + 1);
        ClickNumberMarker clickNumberMarker = MarkerFactory.getClickNumberMarker(textureMapView, cMarkerData);
        clickNumberMarker.addToMap();
        clickNumberMarker.setObject(cMarkerData);
        mNumberMarkerList.add(clickNumberMarker);
        addMapLine(latLng);
    }

    private void addMapLines(){
        Polyline polyline =aMap.addPolyline(new PolylineOptions().
                addAll(mLatLngs).width(10).color(Color.argb(255, 1, 1, 1)));
    }

    private void addMapLine(LatLng latLng){
        if (mLatLngs == null) mLatLngs = new ArrayList<>();
        mLatLngs.add(latLng);
        if (mLatLngs.size() <= 1){
            return;
        }

        List<LatLng> tLatLngs = new ArrayList<LatLng>();
        LatLng lastLatLng = mLatLngs.get(mLatLngs.size() -2);
        PolylineOptions options = new PolylineOptions();
        tLatLngs.add(lastLatLng);
        tLatLngs.add(latLng);
        options.addAll(tLatLngs);
        options.width(10).color(Color.BLACK);
        aMap.addPolyline(options);
    }

    private void addRunLines(LatLng latLng){
        if (mRunLatLngs == null) mRunLatLngs = new ArrayList<>();
        mRunLatLngs.add(latLng);
        if (mRunLatLngs.size() <= 1){
            return;
        }

        LatLng lastLatLng = mRunLatLngs.get(mRunLatLngs.size() -1);
        PolylineOptions options = new PolylineOptions();
        //上一个点的经纬度
        options.add(new LatLng(lastLatLng.latitude, lastLatLng.longitude));
        //当前的经纬度
        options.add(new LatLng(latLng.latitude, latLng.longitude));
        options.width(10).geodesic(true).color(Color.GREEN);
        aMap.addPolyline(options);
    }

    public void setMapMode(){
        if (aMap != null){
            if (TimeUtils.isDayOrNight()){
                if (mapType == 0)return;
                mapType = 0;
                aMap.setMapType(AMap.MAP_TYPE_NORMAL);
            }else{
                if (mapType == 1)return;
                aMap.setMapType(AMap.MAP_TYPE_NIGHT);
                mapType = 1;
            }
        }
    }

    //根据中心点和自定义内容获取缩放bounds
    private LatLngBounds getLatLngBounds(LatLng centerpoint, List<LatLng> pointList) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        if (centerpoint != null){
            for (int i = 0; i < pointList.size(); i++) {
                LatLng p = pointList.get(i);
                LatLng p1 = new LatLng((centerpoint.latitude * 2) - p.latitude, (centerpoint.longitude * 2) - p.longitude);
                b.include(p);
                b.include(p1);
            }
        }
        return b.build();
    }

    /**
     * 根据自定义内容获取缩放bounds
     */
    private LatLngBounds getLatLngBounds( List<LatLng> pointList) {
        LatLngBounds.Builder b = LatLngBounds.builder();
        for (int i = 0; i < pointList.size(); i++) {
            LatLng p = pointList.get(i);
            b.include(p);
        }
        return b.build();
    }

    private LatLng coverLatlng(LatLng sourceLatLng){
        try {
            CoordinateConverter converter  = new CoordinateConverter(mContext);
          // CoordType.GPS 待转换坐标类型
            converter.from(CoordinateConverter.CoordType.GPS);
          // sourceLatLng待转换坐标点 LatLng类型
            converter.coord(sourceLatLng);
          // 执行转换操作
            LatLng desLatLng = converter.convert();
            return desLatLng;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_count:
                if (showNaviState){
                    TipToast.shortTip("正在进行路径绘制，功能暂停使用！");
                    break;
                }
                if (openNaviModeState){
                    clearAllPoint();
                }

                openNaviModeState = !openNaviModeState;
                if (!openNaviModeState){
                    openNaviMode.setText("打开航点选择");
                    item.setTitle("打开航点选择");
                    mNaviMenu.setVisibility(View.GONE);

                }else{
                    openNaviMode.setText("关闭航点选择");
                    item.setTitle("关闭航点选择");
                    mNaviMenu.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.nav_tv:
                ConnectIpPortSettingsFragment mShareDilogFragment = ConnectIpPortSettingsFragment.newInstance();
                mShareDilogFragment.show(getChildFragmentManager(), "ConnectIpPortSettingsFragment");
                closeDrawerLayout(true);
                break;

            case R.id.nav_clear:
                if (showNaviState){
                    TipToast.shortTip("正在进行路径绘制，功能暂停使用！");
                    break;
                }
                clearAllPoint();
                closeDrawerLayout(true);
                break;

            case R.id.nav_mode:
                changeMode();
                break;
        }
        return false;
    }

    private void changeMode() {
        if (mCurrentMode == AMap.MAP_TYPE_NORMAL){
            mCurrentMode = AMap.MAP_TYPE_SATELLITE;
        }else if (mCurrentMode == AMap.MAP_TYPE_SATELLITE){
            mCurrentMode = AMap.MAP_TYPE_NORMAL;
        }

        aMap.setMapType(mCurrentMode);
    }

    /**
     * 缩放移动地图，保证所有自定义marker在可视范围中，且地图中心点不变。
     */
    public void zoomToSpanWithCenter() {
        if (mNumberMarkerList == null || mNumberMarkerList.size() < 2 || currentLocation == null){
            return;
        }
        List<LatLng> pointList = new ArrayList<>();
        for (ClickNumberMarker marker : mNumberMarkerList) {
            CMarkerData tData = marker.<CMarkerData>getObject();
            pointList.add(new LatLng(tData.poiLat, tData.poiLng));
        }
        if (pointList.size() > 0) {
            if (aMap == null)
                return;
            LatLngBounds bounds = getLatLngBounds(new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude()), pointList);
            aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 50));
        }
    }
}

