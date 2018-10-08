package com.sc.clgg.tool.helper;

import android.text.TextUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author：lvke
 * @date：2018/6/26 16:46
 */
public class DecimalFormatHelper {
    public static void main(String[] args) {
        BigDecimal bg = new BigDecimal(1.55);
        double value = bg.setScale(0, BigDecimal.ROUND_HALF_UP).doubleValue();
        System.out.print("最后时间 = " + value + "\n");
        System.out.print("最后时间 = " + bg.setScale(1, BigDecimal.ROUND_HALF_DOWN).doubleValue() + "\n");

        DecimalFormat format = new DecimalFormat("#.00");
        System.out.print("最后时间 = " + format.format(value) + "\n");

        System.out.print("最后时间 = " + formatTwo("") + "\n");
    }

    /**
     * @param doubleStr 小数字符串
     * @return
     */
    public static String formatTwo(String doubleStr) {
        try {
            if (Double.parseDouble(doubleStr) == 0) {
                return "0.00";
            }
            if (!TextUtils.isEmpty(doubleStr)) {
                BigDecimal bg = new BigDecimal(doubleStr);
                double value = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
                return new DecimalFormat("#.00").format(value);
            } else {
                return "";
            }
        } catch (Exception e) {
            return "";
        }
    }


    /**
     * @param d 小数
     * @return
     */
    public static String formatTwo(Double d) {
        if (d == 0) {
            return "0.00";
        }
        BigDecimal bg = new BigDecimal(d);
        double value = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return new DecimalFormat("#.00").format(value);
    }
}
