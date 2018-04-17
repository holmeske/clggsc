package com.sc.clgg.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtils {

    /**
     * 中文姓名判断
     *
     * @param source
     * @return
     * @author ZhangYi 2014年11月19日 下午5:45:41
     */
    public static boolean isChineseName(String source) {
        String reg_charset = "([\\u4E00-\\u9FA5]+)";
        Pattern p = Pattern.compile(reg_charset);
        Matcher m = p.matcher(source);
        return m.matches();
    }

    /**
     * 身份证
     */
    public static boolean isIDCard(String code) {
        boolean tag = true;
        String reg = "(^\\d{15}$)|(^\\d{17}([0-9]|X)$)";
        final Pattern pattern = Pattern.compile(reg);
        final Matcher mat = pattern.matcher(code);
        if (!mat.find()) {
            tag = false;
        }
        return tag;
    }

    /**
     * 用户名
     */
    public static boolean isUserName(String code) {
        boolean tag = true;
        String reg = "^([a-zA-Z]+)[a-zA-Z0-9_]{4,15}$";
        final Pattern pattern = Pattern.compile(reg);
        final Matcher mat = pattern.matcher(code);
        if (!mat.find()) {
            tag = false;
        }
        return tag;
    }

    /**
     * 银行卡位数判断
     *
     * @param confirmNewPwd
     * @return
     * @author ZhangYi 2014年8月19日 下午3:46:45
     */
    public static boolean isValidBankCard(String card) {
        if (card == null) {
            return false;
        }
        if (card.length() < 14 || card.length() > 18) {
            return false;
        }

        return card.matches("[0-9]+");
    }

    /**
     * 密码
     *
     * @param confirmNewPwd
     * @return
     * @author ZhangYi 2014年8月19日 下午3:46:45
     */
    public static String isValidPassword(String pwd) {
        String res = "Y";
        if (pwd.length() < 6 || pwd.length() > 18) {
            res = "密码长度应在6-18位，当前" + pwd.length() + "位！";
        }

        if (pwd.matches("[0-9]+")) {
            res = "密码不能全部为数字";
        }

        if (pwd.matches("[a-zA-Z]+")) {
            res = "密码不能全部为字母";
        }

        return res;
    }
}
