package com.lvke.tools;

import android.content.Context;
import android.widget.Toast;

import java.math.BigDecimal;

/**
 * Author：lvke
 * CreateDate：2017/10/30 15:23
 */

public class BigDecimalHelper {

    public static void log(Context context) {
        Toast.makeText(context, ""+BuildConfig.DEBUG, Toast.LENGTH_SHORT).show();
    }

    /**
     * @param newScale 保修小数点后几位
     * @param val      要转化的数值
     */
    public double up(String val, int newScale) {
        return new BigDecimal(val).setScale(newScale, BigDecimal.ROUND_UP).doubleValue();
    }

    public String formatString(double val, int newScale) {
        return new BigDecimal(val).setScale(newScale, BigDecimal.ROUND_DOWN).toString();
    }

}
