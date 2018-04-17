package com.sc.clgg.http;


/**
 * 作者：lvke
 * 创建时间：2017/6/27
 * 描述：
 */

public abstract class HttpCallBack {
    public abstract void onSuccess(String body);

    public void onStart() {
    }


    public void onError(String body) {
    }

    public void onCacheSuccess(String body) {
    }

    public void onFinish() {
    }
}
