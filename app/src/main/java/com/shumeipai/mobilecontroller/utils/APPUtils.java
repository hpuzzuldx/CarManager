package com.shumeipai.mobilecontroller.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.lang.reflect.Method;
import java.util.List;

public class APPUtils {
    public static final String TAG = "APPUtils";

    public static int getApplicationVersionCode(Context context, String packageName) {
        try {
            List<PackageInfo> packageInfos = context.getPackageManager().getInstalledPackages(0);
            for (int i = 0; i < packageInfos.size(); i++) {
                PackageInfo packageInfo = packageInfos.get(i);
                if (packageInfo.packageName.equals(packageName)) {
                    return packageInfo.versionCode;
                }
            }
        } catch (Exception e) {
            //Logger.w(TAG, "Unable to find info for package: " + packageName);
        }
        return 0;
    }

    /**
     * 获取应用程序名称
     */
    public static synchronized String getAppName(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //获取fota version
   public static  String  getSystemVersion(Context context){
        try {
            ClassLoader cls = context.getClassLoader();
            Class systemProperties = cls.loadClass("android.os.SystemProperties");
            Method get = systemProperties.getMethod("get", String.class);
            String version = (String) get.invoke(systemProperties, "ro.fota.version");
            if (version != null){
                return version.trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static boolean getSystemIsDDevices() {
        String version = "";

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");

            Method get = c.getMethod("get", String.class);
            version=(String)get.invoke(c, "ro.fota.device");
            if (version.contains("D801-802") || version.contains("D821-822")){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean getIsD1_16Devices() {
        String version = "";

        try {
            Class<?> c = Class.forName("android.os.SystemProperties");

            Method get = c.getMethod("get", String.class);
            version=(String)get.invoke(c, "ro.fota.device");
            if (version.contains("D821-822")){
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

}
