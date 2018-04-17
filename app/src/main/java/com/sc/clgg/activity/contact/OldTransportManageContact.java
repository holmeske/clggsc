package com.sc.clgg.activity.contact;

import com.sc.clgg.bean.IorderListBean;

/**
 * 作者：lvke
 * 创建时间：2017/8/1 10:52
 */

public interface OldTransportManageContact {
    void requestSuccess(IorderListBean bean);


    void requestFail(String msg);

}
