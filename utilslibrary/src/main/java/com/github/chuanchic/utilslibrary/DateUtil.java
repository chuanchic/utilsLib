package com.github.chuanchic.utilslibrary;

import android.content.ContentResolver;
import android.content.Context;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * 日期工具类
 */
public class DateUtil {

    private static SimpleDateFormat minDateFormat;
    private static SimpleDateFormat hourDateFormat;

    public static String min2String(long time) {
        if(minDateFormat == null){
            minDateFormat = new SimpleDateFormat("mm:ss");
        }
        return minDateFormat.format(new Date(time));
    }

    public static String hour2String(long time) {
        if(hourDateFormat == null){
            hourDateFormat = new SimpleDateFormat("HH:mm:ss");
        }
        return hourDateFormat.format(new Date(time));
    }

    public static String date2String(long time, String match) {
        return new SimpleDateFormat(match).format(new Date(time));
    }

    public static String date2String(Date date, String match) {
        return new SimpleDateFormat(match).format(date);
    }

    public static String date2StringForSecond(Date date) {
        return date2String(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String date2StringForMills(Date date) {
        return date2String(date, "yyyy-MM-dd HH:mm:ss.SSS");
    }

    /**
     * string型日期比较
     */
    public static boolean compareDate(String match, String date1, String date2) {
        try {
            SimpleDateFormat df = new SimpleDateFormat(match, Locale.getDefault());
            return df.parse(date1).compareTo(df.parse(date2)) >= 0;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean compareDate(String date1, String date2) {
        return compareDate("yyyy-MM-dd HH:mm:ss", date1, date2);
    }

    /**
     * 获取当前时间
     */
    public static String getCurTime(){
        Calendar mCalendar = Calendar.getInstance();
        int year = mCalendar.get(Calendar.YEAR);
        int month = mCalendar.get(Calendar.MONTH) + 1;
        int day = mCalendar.get(Calendar.DAY_OF_MONTH);
        int hour = mCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = mCalendar.get(Calendar.MINUTE);
        int second = mCalendar.get(Calendar.SECOND);
        if(mCalendar.get(Calendar.AM_PM) == Calendar.AM){//上午
        }else{//下午
            if(hour < 12){//12小时制
                hour = hour + 12;//转成24小时制
            }
        }
        return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    }

    /**
     * 是不是同一周
     * @param oneDate 某一天
     * @return true是，false不是
     */
    public static boolean isSameWeek(Date oneDate) {
        if (oneDate != null) {
            Calendar oneCalendar = Calendar.getInstance();
            Calendar curCalendar = Calendar.getInstance();
            oneCalendar.setTime(oneDate);
            curCalendar.setTime(new Date());
            if (oneCalendar.get(Calendar.WEEK_OF_YEAR) == curCalendar.get(Calendar.WEEK_OF_YEAR)) {
                return true;
            }
        }
        return false;
    }

    /**
     * true：12小时制，false：24小时制
     */
    public static boolean hourIs12(Context context){
        ContentResolver mResolver= context.getContentResolver();
        String key = android.provider.Settings.System.TIME_12_24;
        if("12".equals(android.provider.Settings.System.getString(mResolver,key))) {//12小时制
            return true;
        }else {//24小时制
            return false;
        }
    }

    /**
     * 获取当前日期
     */
    public static String getCurrentDate(String connector){
        String currentDate = null;
        try {
            Calendar mCalendar = Calendar.getInstance();
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH) + 1;
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);
            currentDate = formateDate(year, month, day, connector);
        }catch (Exception e){
            e.printStackTrace();
        }
        return currentDate;
    }

    /**
     * 获取最近日期
     */
    public static String[] getRecentDate(int dayCount, String connector){
        String[] date = new String[2];
        try {
            Calendar mCalendar = Calendar.getInstance();
            //结束日期：当前日期
            int endYear = mCalendar.get(Calendar.YEAR);
            int endMonth = mCalendar.get(Calendar.MONTH) + 1;
            int endDay = mCalendar.get(Calendar.DAY_OF_MONTH);
            date[1] = formateDate(endYear, endMonth, endDay, connector);
            //开始日期：往前推多少天
            mCalendar.add(Calendar.DAY_OF_MONTH, -dayCount);
            int startYear = mCalendar.get(Calendar.YEAR);
            int startMonth = mCalendar.get(Calendar.MONTH) + 1;
            int startDay = mCalendar.get(Calendar.DAY_OF_MONTH);
            date[0] = formateDate(startYear, startMonth, startDay, connector);
        }catch (Exception e){
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 格式化日期
     */
    public static String formateDate(int year, int month, int day, String connector){
        String date = null;
        if(month < 10){
            date = year + connector + "0" + month;
        }else{
            date = year + connector + month;
        }
        if(day < 10){
            date = date + connector + "0" + day;
        }else{
            date = date + connector + day;
        }
        return date;
    }

}
