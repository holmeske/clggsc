package com.sc.clgg.activity.contact;

import com.sc.clgg.bean.StoreInfoBean;

import java.util.List;

/**
 * Author：lvke
 * CreateDate：2017/8/11 14:00
 * 通用接口(维修保养，加油加气，物流园)
 */

public interface CommonContact {
    void onSuccess(List<StoreInfoBean> list);

    void onError(String msg);

    void onFinish();

    void onStartRequest();
}
