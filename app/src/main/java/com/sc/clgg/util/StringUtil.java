package com.sc.clgg.util;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

    public static String toBigDecimal(String str) {
        BigDecimal bigDecimal = new BigDecimal(str);
        return bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP).toString();
    }

    /**
     * 正则表达式，校验设置密码
     **/
    public static boolean isMatch(String str) {
        String marcher = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{8,8}$";
        Pattern p = Pattern.compile(marcher);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean isMathMoney(String value) {
//		String marcher = "^([0-9]*|d*.d{1}?d*)$";
        String marcher = "^\\d{0,10}\\.{0,1}(\\d{1,2})?$";
        Pattern p = Pattern.compile(marcher);
        Matcher m = p.matcher(value);
       /* String marcherTow = "(^d*.?d*[0-9]+d*$)|(^[0-9]+d*.d*$)";
        Pattern p2 = Pattern.compile(marcherTow);
        Matcher m2 = p2.matcher(value); */
        return m.matches();
    }

}
