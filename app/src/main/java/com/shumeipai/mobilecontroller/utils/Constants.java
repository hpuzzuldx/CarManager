package com.shumeipai.mobilecontroller.utils;

public interface Constants {

    int ONE_KB = 1024;
    int ONE_MB = ONE_KB * 1024;

    int SIZE_DEFAULT = 2048;
    int SIZE_LIMIT = 2048;

    String KEY_USER_ID = "userId";
    String KEY_SOURCE = "source";

    String PLATE_CODE_HEART = "casualNews";//随心听
    String PLATE_CODE_WAY = "alongRoadNews";//沿路听
    String PLATE_CODE_LOCAL = "localNews";//本地新闻

    String KEY_FAILURE_DURATIONS = "save_failure_listened_durations";
    String KEY_FAILURE_DURATIONS_FILE = "save_fail_time_file";


    String ZD_DOWNLOADS_BASE_PATH = "/EnjoyMapDownloads/";
    String ZD_DOWNLOADS_VIDEO_PATH = ZD_DOWNLOADS_BASE_PATH + "video/";
    String ZD_DOWNLOADS_IMAGE_PATH = ZD_DOWNLOADS_BASE_PATH + "image/";
    String IMAGE_COMPRESS_PATH = "image/compress/";

    String KEY_SPLASH_AD_URL = "splash_ad_url";
    String KEY_ENJOY_MAP_DEFAULT = "enjoymap_type";
}
