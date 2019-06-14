package com.sc.clgg.tool.helper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Author：lvke
 * CreateDate：2017/10/11 11:07
 * @author lvke
 */

public class CheckHelper {

    /**
     * 校验手机号是否合法
     */
    public static boolean isCorrectPhone(String phone) {
        boolean b = true;
        String reg = "^(1[3-8])[0-9]{9}$";
        final Pattern pattern = Pattern.compile(reg);
        final Matcher mat = pattern.matcher(phone);
        if (!mat.find()) {
            b = false;
        }
        return b;
    }

    /**
     * 判断字符串是否是json结构
     */
    public static boolean isJson(String value) {
        try {
            new JSONObject(value);
        } catch (JSONException e) {
            return false;
        }
        return true;
    }
}
