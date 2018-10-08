package com.sc.clgg.http;

import com.sc.clgg.config.NetField;

import java.util.HashMap;
import java.util.Map;


/**
 * @author lvke
 */
public class HttpRequestHelper {

    /**
     * 注册接口
     */
    public static void register(String userName, String password, String personalPhone, HttpCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("userName", userName);
        params.put("userType", "11");
        params.put("password", password);
        params.put("nickName", "");
        params.put("personalPhone", personalPhone);
        new HttpManager().post(NetField.SSO_SITE + "user/appRegister", params, callback);
    }

    /**
     * 发送验证码
     */
    public static void sendVerificationCode(String phone, HttpCallBack callback) {
        Map<String, String> params = new HashMap<>();
        new HttpManager().get(NetField.SSO_SITE + "user/sendCheckCode/" + phone, params, callback);
    }

    /**
     * 验证码校验
     */
    public static void verificationCodeCheck(String phone, String code, HttpCallBack callback) {
        Map<String, String> params = new HashMap<>();
        new HttpManager().get(NetField.SSO_SITE + "user/validateCode/" + phone + "/" + code, params, callback);
    }

}
