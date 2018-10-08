package com.sc.clgg.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * @author lvke
 */
public class TimeHelper {
    /**
     * java中的时间格式字符串没有秒<br>
     * 值 : yyyy-MM-dd HH:mm
     */
    public final static String JAVA_TIME_FORAMTER_1 = "yyyy-MM-dd HH:mm";
    /**
     * java中的时间格式字符串精确到秒<br>
     * 值 : yyyy-MM-dd HH:mm:ss
     */
    public final static String JAVA_TIME_FORAMTER_2 = "yyyy-MM-dd HH:mm:ss";
    /**
     * java中的时间格式字符串没有秒<br>
     * 值 : yyyyMMddHHmm
     */
    public final static String JAVA_TIME_FORAMTER_3 = "yyyyMMddHHmm";
    /**
     * java中的时间格式字符串精确到秒<br>
     * 值 : yyyyMMddHHmmss
     */
    public final static String JAVA_TIME_FORAMTER_4 = "yyyyMMddHHmmss";
    /**
     * java中的时间格式字符串<br>
     * 值 : HH:mm
     */
    public final static String JAVA_TIME_FORAMTER_5 = "HH:mm";
    /**
     * java中的时间格式字符串<br>
     * 值 : HH:mm:ss
     */
    public final static String JAVA_TIME_FORAMTER_6 = "HH:mm:ss";
    /**
     * java中的时间格式字符串精确到秒<br>
     * 值 : yyyy-MM-dd@HH:mm
     */
    public final static String JAVA_TIME_FORAMTER_7 = "yyyy-MM-dd@HH:mm";
    /**
     * java中的时间格式字符串精确到秒<br>
     * 值 : yyyy-MM-dd-HHmmss
     */
    public final static String JAVA_TIME_FORAMTER_8 = "yyyy-MM-dd-HHmmss";
    /**
     * java中的时间格式字符串精确到秒<br>
     * 值 : yyMMddHHmmss
     */
    public final static String JAVA_TIME_FORAMTER_9 = "yyMMddHHmmss";

    /**
     * java中的时间格式字符串精确到秒<br>
     * 值 : yyyy-MM-dd-HHmmss
     */
    public final static String JAVA_TIME_FORAMTER_10 = "yyyy-MM-dd HH:mm";

    /**
     * java中的日期格式字符串<br>
     * 值 : yyyy-MM-dd
     */
    public final static String JAVA_DATE_FORAMTER_1 = "yyyy-MM-dd";
    /**
     * java中的日期格式字符串<br>
     * 值 : yyyy-MM
     */
    public final static String JAVA_DATE_FORAMTER_3 = "yyyy-MM";
    /**
     * java中的日期格式字符串<br>
     * 值 : yyyyMMdd
     */
    public final static String JAVA_DATE_FORAMTER_2 = "yyyyMMdd";
    /**
     * java中的日期格式字符串<br>
     * 值 : yyyyMMdd
     */
    public final static String JAVA_DATE_FORAMTER_4 = "yyyy.MM.dd";
    /**
     * java中的日期格式字符串<br>
     * 值 : yyyyMM
     */
    public final static String JAVA_DATE_FORAMTER_MONTH = "yyyyMM";
    /**
     * java中的日期格式字符串<br>
     * 值 : yyyyMM
     */
    public final static String JAVA_DATE_FORAMTER_5 = "yyyyMMddHHmm";

    /**
     * 数据库中的时间格式字符串没有秒<br>
     * 值 : yyyy-mm-dd hh24:mi
     */
    public final static String SQL_TIME_FORAMTER_1 = "yyyy-mm-dd hh24:mi";
    /**
     * 数据库中的时间格式字符串精确到秒<br>
     * 值 : yyyy-mm-dd hh24:mi:ss
     */
    public final static String SQL_TIME_FORAMTER_2 = "yyyy-mm-dd hh24:mi:ss";
    /**
     * 数据库中的时间格式字符串没有秒<br>
     * 值 : yyyymmddhh24mi
     */
    public final static String SQL_TIME_FORAMTER_3 = "yyyymmddhh24mi";
    /**
     * 数据库中的时间格式字符串精确到秒<br>
     * 值 : yyyymmddhh24miss
     */
    public final static String SQL_TIME_FORAMTER_4 = "yyyymmddhh24miss";
    /**
     * 数据库中的时间格式字符串<br>
     * 值 : hh24mi
     */
    public final static String SQL_TIME_FORAMTER_5 = "hh24mi";
    /**
     * 数据库中的时间格式字符串<br>
     * 值 : hh24miss
     */
    public final static String SQL_TIME_FORAMTER_6 = "hh24miss";
    /**
     * 数据库中的日期格式字符串<br>
     * 值 : yyyy-mm-dd
     */
    public final static String SQL_DATE_FORAMTER_1 = "yyyy-mm-dd";
    /**
     * 数据库中的日期格式字符串<br>
     * 值 : yyyymmdd
     */
    public final static String SQL_DATE_FORAMTER_2 = "yyyymmdd";

    /**
     * 得到一个格式化之后的时间
     *
     * @param formaterStr 格式字符串
     * @param date        时间
     */
    public static String date2str(String formaterStr, Date date) {
        DateFormat tf = new SimpleDateFormat(formaterStr, Locale.getDefault());
        return tf.format(date);
    }

    /**
     * 得到当前时间
     */
    public static Date getCurrentDate() {
        return Calendar.getInstance().getTime();
    }


    /**
     * 按格式字符串得到当前时间没有延时
     *
     */
    public static String getCurrentDateNotHasDelay(String formaterStr) {
        return date2str(formaterStr, getCurrentDate());
    }

    /**
     * 按格式字符串得到当前时间没有延时
     *
     */
    public static String getCurrentDateNotHasDelay() {
        return getCurrentDateNotHasDelay(JAVA_TIME_FORAMTER_2);
    }

    /**
     * 按格式字符串得到时间
     *
     */
    public static Date str2date(String formaterStr, String param) {
        DateFormat tf = new SimpleDateFormat(formaterStr, Locale.getDefault());
        Date date = null;
        try {
            date = tf.parse(param);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 按格式字符串得到时间的Long
     *
     * @param time     时间
     */
    public static long time2long(String formaterStr, String time) {
        DateFormat tf = new SimpleDateFormat(formaterStr, Locale.getDefault());
        long var = 0;
        try {
            var = tf.parse(time).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return var;
    }

    /**
     * 已Long长度得到按格式字符串时间
     *
     */
    public static String long2time(String formaterStr, long timeLong) {
        DateFormat tf = new SimpleDateFormat(formaterStr, Locale.getDefault());
        return tf.format(new Date(timeLong));
    }

    /**
     * 得到在一个时间之后的days天的时间
     *
     * @param time     时间
     * @param days     天数
     */
    public static String timeAfterDays(String formaterStr, String time, int days) {
        return timeAfterHours(formaterStr, time, 24 * days);
    }

    /**
     * 得到在一个时间之后的days天的时间
     *
     */
    public static String timeAfterHours(String formaterStr, String time,
                                        int hours) {
        return timeAfterMinute(formaterStr, time, 60 * hours);
    }

    /**
     * 得到在一个时间之后的days天的时间
     *
     */
    public static String timeAfterMinute(String formaterStr, String time,
                                         int minute) {
        long curTimeLong = time2long(formaterStr, time);
        long proTimeLong = ((curTimeLong / 1000L) + (60L * minute)) * 1000L;
        return long2time(formaterStr, proTimeLong);
    }

    /**
     * 根据日期字符串计算星期几
     */
    public static String calculateWeek(String formaterStr, String param) {
        Date date = str2date(formaterStr, param);
        DateFormat tf = new SimpleDateFormat("E");// E：代表是星期，F：代表周
        String weekStr = tf.format(date);// 格式：星期X
        return weekStr.substring(weekStr.length() - 1);// 返回:X
    }

    /**
     * 得到指定两日期相隔的天数
     */
    public static int calculateDays(String formaterStr1, String dateStr1,
                                    String formaterStr2, String dateStr2) {
        Date date1 = str2date(formaterStr1, dateStr1);
        Date date2 = str2date(formaterStr2, dateStr2);
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24)) + 1;
        return days;
    }

    /**
     * 得到指定两日期相隔的天数
     */
    public static int calculateDays(String formaterStr, String dateStr1,
                                    String dateStr2) {
        return calculateDays(formaterStr, dateStr1, formaterStr, dateStr2);
    }

    /**
     * 判断是否时间timeStr1小于时间timeStr2
     */
    public static boolean time1LTTime2(String formaterStr1, String timeStr1,
                                       String formaterStr2, String timeStr2) {
        Date date1 = str2date(formaterStr1, timeStr1);
        Date date2 = str2date(formaterStr2, timeStr2);
        return date2.getTime() - date1.getTime() > 0;
    }

    /**
     * 判断是否时间timeStr1小于等于时间timeStr2
     */
    public static boolean time1LTEQTime2(String formaterStr1, String timeStr1,
                                         String formaterStr2, String timeStr2) {
        Date date1 = str2date(formaterStr1, timeStr1);
        Date date2 = str2date(formaterStr2, timeStr2);
        return date2.getTime() - date1.getTime() >= 0;
    }

    /**
     * 判断是否时间timeStr1小于时间timeStr2
     */
    public static boolean time1LTTime2(String formaterStr, String timeStr1,
                                       String timeStr2) {
        return time1LTTime2(formaterStr, timeStr1, formaterStr, timeStr2);
    }


    /**
     * 判断是否时间timeStr1小于等于时间timeStr2
     */
    public static boolean time1LTEQTime2(String formaterStr, String timeStr1,
                                         String timeStr2) {
        return time1LTEQTime2(formaterStr, timeStr1, formaterStr, timeStr2);
    }

    /**
     * 时间格式的转换
     *
     * @param proFmtStr   原来的格式
     * @param time        2009-8-1@23:1
     * @param afterFmtStr 要转换的格式
     * @return 2009-08-01@23:01
     */
    public static String formaterTime(String proFmtStr, String time, String afterFmtStr) {
        return date2str(afterFmtStr, str2date(proFmtStr, time));
    }

    /**
     * 格式化时间
     *
     * @param time 2009-8-1@23:1
     * @return 2009-08-01@23:01
     */
    public static String formaterTime(String formaterStr, String time) {
        return formaterTime(formaterStr, time, formaterStr);
    }

    /**
     * 功能：设置日期
     *
     * @param date  Date 需要设置的日期
     * @param field int 需要设置的字段
     * @param value int 设置的内容
     * @return Date 设置后的日期
     */
    public static Date setDate(Date date, int field, int value) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.set(field, value);
        return calendar.getTime();
    }

    /**
     * 判断今天是否当前月的第一天
     *
     * @return 是true, 否false
     */
    public static boolean isFirstDay4Month() {
        String now = getCurrentDateNotHasDelay(JAVA_DATE_FORAMTER_1);
        return "01".equals(now.split("-")[2]);
    }

    /**
     * 判断今天是否是周日
     *
     * @return 是true, 否false
     */
    public static boolean isSunday() {
        return isSunday(JAVA_DATE_FORAMTER_1,
                getCurrentDateNotHasDelay(JAVA_DATE_FORAMTER_1));
    }

    /**
     * 判断今天是否是周日
     *
     * @return 是true, 否false
     */
    public static boolean isSunday(String formaterStr, String day) {
        String week = calculateWeek(formaterStr, day);
        return "日".equals(week);
    }

    /**
     * 判断是否是今天
     *
     * @return 是true, 否false
     */
    public static boolean isToday(String formaterStr, String time) {
        String day = formaterTime(formaterStr, time, JAVA_DATE_FORAMTER_1);
        String today = getCurrentDateNotHasDelay(JAVA_DATE_FORAMTER_1);
        return day.equals(today);
    }

    /**
     * 得到一个月的天数
     */
    public static int getDay4Month(String formaterStr, String time) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(str2date(formaterStr, time));
        return rightNow.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 得到两个月之间的天数
     */
    public static int getDay4TwainMonth(String formaterStr, String time,
                                        String formaterStr1, String time1) {
        int days = 0;
        String iTime = formaterTime(formaterStr, time, JAVA_DATE_FORAMTER_3);
        String endTime = formaterTime(formaterStr1, time1, JAVA_DATE_FORAMTER_3);
        if (!time1LTEQTime2(JAVA_DATE_FORAMTER_3, iTime, endTime)) {
            return days;
        }
        do {
            days += getDay4Month(JAVA_DATE_FORAMTER_3, iTime);
            iTime = getNextMonth(JAVA_DATE_FORAMTER_3, iTime);
        } while (iTime.equals(endTime));
        return days;
    }

    /**
     * 得到下一个月份
     *
     * @return 返回的下个月， 格式: yyyy-mm
     */
    public static String getNextMonth(String formaterStr, String time) {
        String m = formaterTime(formaterStr, time, JAVA_DATE_FORAMTER_3);
        int y = Integer.parseInt(m.split("-")[0]);
        int nm = Integer.parseInt(m.split("-")[1]) + 1;
        String nextMonth = nm + "";
        if (nm < 10) {
            nextMonth = "0" + nm;
        } else {
            if (nm == 13) {
                y++;
                nextMonth = "01";
            }
        }
        return y + "-" + nextMonth;
    }

    /**
     * 得到上一个月份
     *
     * @return 返回的上个月， 格式: yyyy-mm
     */
    public static String getPreMonth(String formaterStr, String time) {
        String m = formaterTime(formaterStr, time, JAVA_DATE_FORAMTER_3);
        int y = Integer.parseInt(m.split("-")[0]);
        int pm = Integer.parseInt(m.split("-")[1]) - 1;
        String nextMonth = pm + "";
        if (pm < 10) {
            nextMonth = "0" + pm;
        } else {
            if (pm == 0) {
                y--;
                nextMonth = "01";
            }
        }
        return y + "-" + nextMonth;
    }

    /**
     * 得到一个月的第一天日期
     *
     * @return 返回的下个月， 格式: yyyy-mm-01
     */
    public static String getMonthFirstDay(String formaterStr, String time) {
        String m = formaterTime(formaterStr, time, JAVA_DATE_FORAMTER_3);
        return m + "-01";
    }

    /**
     * 得到上一个月份最后一天
     *
     * @return 返回的上个月， 格式: yyyy-mm-dd
     */
    public static String getPreMonthLastDay(String formaterStr, String time) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTime(str2date(formaterStr, time));
        rightNow.set(Calendar.DAY_OF_MONTH, 1);
        rightNow.add(Calendar.DAY_OF_MONTH, -1);
        return date2str(JAVA_DATE_FORAMTER_1, rightNow.getTime());
    }

    // yyyy-mm-dd hh:mm:ss 转成yyyymmddhhmmss
    public static String datechange(String date) {
        return (date.replaceAll("-", "").replaceAll(":", "")
                .replaceAll(" ", "")).trim();
    }

    public static void main(String[] args) {
        System.err.println(isToday("yyyy-MM-dd", "2016-4-12"));
    }

    public static String calculateDaysToString(Date date1, Date date2) {
        long calculates = date2.getTime() - date1.getTime();
        return calculates / (1000 * 60 * 60) + "小时"
                + (calculates % (1000 * 60 * 60)) / (1000 * 60) + "分钟";
    }
}