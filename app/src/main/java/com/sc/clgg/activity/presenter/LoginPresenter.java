package com.sc.clgg.activity.presenter;

import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sc.clgg.activity.contact.LoginContact;
import com.sc.clgg.application.App;
import com.sc.clgg.bean.UserInfoBean;
import com.sc.clgg.config.Method;
import com.sc.clgg.config.NetField;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;
import com.sc.clgg.util.ConfigUtil;

import org.json.JSONObject;

import tool.helper.LogHelper;
import tool.helper.SharedPreferencesHelper;

/**
 * 创建时间：2017/7/11 10:22
 *
 * @author lvke
 */

public class LoginPresenter {
    private LoginContact mLoginContact;

    public LoginPresenter(LoginContact mLoginContact) {
        this.mLoginContact = mLoginContact;
    }

    public void loginToTXJ(final String username, final String password) {
        getUserInfo(username, password);
    }

    private void getUserInfo(final String username, String password) {
        mLoginContact.onStartLoading();
        HttpRequestHelper.getUserInfo(username, password, new HttpCallBack() {
            @Override
            public void onSuccess(String body) {
                UserInfoBean bean = null;

                try {
                    bean = new Gson().fromJson(new JSONObject(body).getString("data"), new TypeToken<UserInfoBean>() {
                    }.getType());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Bundle bundle = new Bundle();
                if (null != bean) {
                    bundle.putParcelable(NetField.RES, bean);
                    bundle.putString(NetField.MSG, bean.getMessage());

                    if (!TextUtils.equals(username, SharedPreferencesHelper.SharedPreferences(App.getInstance()).getString("account", ""))) {
                        SharedPreferencesHelper.SharedPreferencesEditor(App.getInstance(), "sp").putString("account", username).commit();
                    }

                    String history_account = SharedPreferencesHelper.SharedPreferences(App.getInstance()).getString("history_account", "");

                    StringBuilder sb = new StringBuilder();
                    if (history_account.length() != 0) {
                        sb.append(history_account).append(",").append(username);
                    } else {
                        sb.append(username);
                    }

                    SharedPreferencesHelper.SharedPreferencesEditor(App.getInstance(), "sp").putString("history_account", sb.toString()).apply();

                    LogHelper.e("history_account = " + SharedPreferencesHelper.SharedPreferences(App.getInstance()).getString("history_account", ""));

                    requestSuccess(Method.USER_LOGIN_METHOD, bundle);
                } else {
                    bundle.putString(NetField.MSG, "网络异常，请稍后再试");

                    requestFail(Method.USER_LOGIN_METHOD);
                }

            }

        });
    }

    protected void requestFail(String sign) {
        mLoginContact.setButtonSuccess();
        if (Method.USER_LOGIN_METHOD.equals(sign)) {
            mLoginContact.onToast("用户名或密码错误");
            mLoginContact.setButtonSuccess();
        } else if (Method.USER_ADD_METHOD.equals(sign)) {
            /* 注册失败 */
            mLoginContact.onToast("登录失败");
        }
    }

    protected void requestSuccess(final String sign, final Bundle bundle) {
        if (Method.USER_LOGIN_METHOD.equals(sign) || Method.USER_ADD_METHOD.equals(sign)) {
            mLoginContact.setButtonSuccess();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    UserInfoBean bean;
                    try {
                        bean = (UserInfoBean) bundle.getParcelable(NetField.RES);
                    } catch (ClassCastException e) {
                        e.printStackTrace();
                        bean = null;
                    }
                    if (bean != null) {
                        new ConfigUtil().setUserInfo(bean);
                        mLoginContact.jumpOtherActivity();
                    } else if (bean != null && !bean.getSuccess() && bean.getErrorCode().equals("user.0020")) {
                        mLoginContact.onToast("用户名或密码错误，请重新输入");
                    } else {
                        mLoginContact.onToast(bundle.getString(NetField.MSG));
                    }
                }
            }, 300);
        }
    }

}
