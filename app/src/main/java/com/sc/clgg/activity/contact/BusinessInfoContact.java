package com.sc.clgg.activity.contact;

import com.sc.clgg.bean.StoreInfoDetailBean;

/**
 * Author：lvke
 * CreateDate：2017/8/16 13:47
 */

public interface BusinessInfoContact {
    void onSuccess(StoreInfoDetailBean bean);

    void onError(String msg);
}
