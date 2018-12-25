package com.sc.clgg;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * @author：lvke
 * @date：2018/9/14 14:55
 */
public class a {
    public static void main(String[] args) {
        System.out.println(String.format("%.2f", 0.4f));
    }
    public static String formatTwo(Double d) {
        if (d == 0) {
            return "0.00";
        }

        BigDecimal bg = new BigDecimal(d);
        double value = bg.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return new DecimalFormat("#.00").format(value);
    }
}
