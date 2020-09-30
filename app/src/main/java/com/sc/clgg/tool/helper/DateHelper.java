package com.sc.clgg.tool.helper;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Author：lvke
 * CreateDate：2017/10/19 14:41
 */

public class DateHelper {

    /**
     * 将秒数转换为日时分秒，
     *
     * @param ms 毫秒
     * @ret urn
     */
    public static String secondToTime(long ms) {
        StringBuilder sb = new StringBuilder();
        sb.append(ms / (24 * 60 * 60000)).append("天");
        ms = ms % (24 * 60 * 60000);
        sb.append(ms / (60 * 60000)).append("时");
        ms = ms % (60 * 60000);
        sb.append(ms / (60000)).append("分");
        ms = ms % (60000);
        sb.append(ms / (1000)).append("秒");

        return sb.toString();
    }

    public static String getWeek(String sdate) {
        // 再转换为时间
        Date date = StringToDate(sdate);
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        // int hour=c.get(Calendar.DAY_OF_WEEK);
        // hour中存的就是星期几了，其范围 1~7
        // 1=星期日 7=星期六，其他类推
        return new SimpleDateFormat("EEEE", Locale.getDefault()).format(c.getTime());
    }

    public static String getDay(String dateStr) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(StringToDate(dateStr));
        return String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
    }

    public static Date StringToDate(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        ParsePosition pos = new ParsePosition(0);
        return format.parse(dateStr, pos);
    }

    public static String format(Long time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        Date date = new Date(time);
        return format.format(date);
    }

    public static String format(String time, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        Date date = new Date(Long.parseLong(time));
        return format.format(date);
    }

    public static String formatCurrentTime(String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    public static String formatCurrentTime(Calendar calendar, String pattern) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        Date date = new Date(calendar.getTimeInMillis());
        return format.format(date);
    }
}
