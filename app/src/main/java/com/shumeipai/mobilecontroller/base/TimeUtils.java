package com.shumeipai.mobilecontroller.base;

import android.content.Context;
import android.text.TextUtils;

import com.shumeipai.mobilecontroller.PiCarApplication;
import com.shumeipai.mobilecontroller.utils.DateTimeUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by congtaowang on 2018/10/19.
 */
public class TimeUtils {

    public static String parseYMD( long timestamp ) {
        return parse( DateTimeUtils.yyyyMMdd, timestamp );
    }

    public static String parse( String pattern, long timestamp ) {
        if ( TextUtils.isEmpty( pattern ) ) {
            pattern = DateTimeUtils.yyyyMMdd;
        }
        try {
            return new SimpleDateFormat( pattern ).format( new Date( timestamp ) );
        } catch ( Exception e ) {
            return "";
        }
    }

    //两个时间戳是否是同一天
    public static boolean isSameData(String currentTime,String lastTime) {
        try {
            Calendar nowCal = Calendar.getInstance();
            Calendar dataCal = Calendar.getInstance();
            SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
            Long nowLong = new Long(currentTime);
            Long dataLong = new Long(lastTime);
            String data1 = df1.format(nowLong);
            String data2 = df2.format(dataLong);
            Date now = df1.parse(data1);
            Date date = df2.parse(data2);
            nowCal.setTime(now);
            dataCal.setTime(date);
            return isSameDay(nowCal, dataCal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isSameDay(Calendar cal1, Calendar cal2) {
        if(cal1 != null && cal2 != null) {
            return cal1.get(Calendar.ERA) == cal2.get(Calendar.ERA)
                    && cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
                    && cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
        } else {
           return false;
        }
    }

    /**
     * true day
     * @return
     */
    public static boolean isDayOrNight() {
        if (get24HourMode(PiCarApplication.getContext())) {
            //24小时制
            Calendar c = Calendar.getInstance();
           int currHour =  c.get(c.HOUR_OF_DAY);
           if (currHour >= 6 && currHour < 18){
               return true;
           }
        } else {
            //12小时制
            Calendar c = Calendar.getInstance();
            int currHour = c.get(c.HOUR);
            if (c.get(Calendar.AM_PM) == 0) {
                //上午
                if (currHour >=6 && currHour <= 12){
                    return true;
                }
            } else {
                //下午
                if (currHour >=0 && currHour < 6){
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean get24HourMode(Context context) {
        return android.text.format.DateFormat.is24HourFormat(context);
    }

}
