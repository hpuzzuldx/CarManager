package com.shumeipai.mobilecontroller.network;

import com.shumeipai.mobilecontroller.network.base.ApiService;
import com.shumeipai.mobilecontroller.utils.SpStorage;

public final class ServiceFactoryHelper {

    public static synchronized ApiService newCarLifeApiService() {
        return RetrofitFactory.getInstance("http://"+SpStorage.getIP()+":"+SpStorage.getPort()+"/").create(ApiService.class);
    }

}
