package com.sc.clgg.tool.helper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Author：lvke
 * CreateDate：2017/8/23 17:10
 */

public class DateUtil {
    /**
     * @param date long类型的时间戳
     * @return 格式化的时间
     */
    public static String format(long date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        return format.format(new Date(date));
    }

    public static String format(String pattern, long date) {
        SimpleDateFormat format = new SimpleDateFormat(pattern, Locale.getDefault());
        return format.format(new Date(date));
    }
}
