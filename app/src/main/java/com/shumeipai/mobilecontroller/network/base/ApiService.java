package com.shumeipai.mobilecontroller.network.base;

import com.shumeipai.mobilecontroller.model.BaseData;

import java.util.Map;

import io.reactivex.Observable;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ApiService {
    @GET("https://api.myjson.com/bins/e7kem")
    Observable<BaseData> getAddOilCardList(@QueryMap Map<String, Object> params);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/sunflower/listen/new/car/v1/getNews")
    Observable<BaseData> likeShare(@Body RequestBody body);

    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/SendCoordinate")
    Observable<BaseData> putSelectedPoints(@Body RequestBody body);


    @Headers({"Content-type:application/json;charset=UTF-8"})
    @POST("/GetLocationByLastUploadTime")
    Observable<BaseData> getCurrentPositon(@Body RequestBody body);
}


