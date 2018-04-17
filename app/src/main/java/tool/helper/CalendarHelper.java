package tool.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Author：lvke
 * CreateDate：2017/9/26 14:09
 */

public class CalendarHelper {

    /**
     * @return 获取当天是周几
     */
    public static String getDayOfWeek(Calendar c) {
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            return "星期天";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            return "星期一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            return "星期二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            return "星期三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            return "星期四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            return "星期五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            return "星期六";
        }
        return "";
    }

    /**
     * @param date 日期
     * @return 年月日字符串
     */
    public static String getDateString(Date date) {
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
    }

    /**
     * @return 当月
     */
    public static int getCurrentMonth() {
        return Calendar.getInstance().get(Calendar.MONTH) + 1;
    }

    /**
     * @return 当年
     */
    public static int getCurrentYear() {
        return Calendar.getInstance().get(Calendar.YEAR);
    }

    /**
     * @return 当日
     */
    public static int getCurrentDay() {
        return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    }

    /**
     * @param amount 正数：后几天    负数：前几天
     * @return 格式为yyyy-MM-dd的日期
     */

    public static String getTime(int amount) {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        //得到当前时间，+3天
        //rightNow.add(java.util.Calendar.DAY_OF_MONTH, 3);
        //如果是后退几天，就写 -天数 例如：
        calendar.add(Calendar.DAY_OF_MONTH, amount);
        //进行时间转换
        return simpleDateFormat.format(calendar.getTime());
    }

    //获取当前时间 格式：yyyy-MM-dd
    public static String getCurrentTime() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return simpleDateFormat.format(date);
    }

    public static String getLastDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime());
    }

    public static String getLastDayOfMonth(Calendar calendar) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DATE));
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime());
    }


    public static String getFirstDayOfMonth(Calendar calendar) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
        cal.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime());
    }

    public static String getFirstDayOfMonth(int year, int month) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month - 1);
        cal.set(Calendar.DAY_OF_MONTH, cal.getMinimum(Calendar.DATE));
        return new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.getTime());
    }


}
