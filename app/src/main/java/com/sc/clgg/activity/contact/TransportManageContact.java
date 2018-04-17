package com.sc.clgg.activity.contact;

import com.sc.clgg.bean.TransportManageBean;

/**
 * 作者：lvke
 * 创建时间：2017/8/1 10:52
 */

public interface TransportManageContact {

    void requestStart();

    void requestSuccess(TransportManageBean bean);

    void requestFail(String msg);

    void requestFinish();
}
