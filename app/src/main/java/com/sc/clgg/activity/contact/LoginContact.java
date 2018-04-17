package com.sc.clgg.activity.contact;

/**
 * 作者：lvke
 * 创建时间：2017/7/11 12:13
 */

public interface LoginContact {

    void onSuccess(String body);

    void onError(String msg);

    void onToast(String msg);

    void onStartLoading();//按下登录按钮时调用

    void setButtonSuccess();

    void jumpOtherActivity();

}
