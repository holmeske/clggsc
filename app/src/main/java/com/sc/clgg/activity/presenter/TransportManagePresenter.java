package com.sc.clgg.activity.presenter;

import com.google.gson.Gson;
import com.sc.clgg.activity.contact.OldTransportManageContact;
import com.sc.clgg.activity.contact.TransportManageContact;
import com.sc.clgg.bean.IorderListBean;
import com.sc.clgg.bean.TransportManageBean;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;

import tool.helper.LogHelper;

/**
 * 作者：lvke
 * 创建时间：2017/8/1 10:43
 */

public class TransportManagePresenter {
    private TransportManageContact mTransportManageContact;
    private OldTransportManageContact mOldTransportManageContact;

    public TransportManagePresenter(OldTransportManageContact mOldTransportManageContact) {
        this.mOldTransportManageContact = mOldTransportManageContact;
    }

    public TransportManagePresenter(TransportManageContact mTransportManageContact) {
        this.mTransportManageContact = mTransportManageContact;
    }

    public void getOrderList(String waybillStatus,String startDate,String endDate) {
        HttpRequestHelper.getOrderList(waybillStatus, startDate, endDate, new HttpCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                if (mTransportManageContact != null) {
                    mTransportManageContact.requestStart();
                }
            }

            @Override
            public void onSuccess(String body) {
                if (mOldTransportManageContact != null) {
                    try {
                        IorderListBean bean = new Gson().fromJson(body, IorderListBean.class);
                        mOldTransportManageContact.requestSuccess(bean);
                    } catch (Exception e) {
                        LogHelper.e(e);
                    }
                }

                if (mTransportManageContact != null) {
                    try {
                        TransportManageBean bean = new Gson().fromJson(body, TransportManageBean.class);
                        mTransportManageContact.requestSuccess(bean);
                    } catch (Exception e) {
                        LogHelper.e(e);
                    }
                }
            }

            @Override
            public void onError(String body) {
                super.onError(body);
                if (mOldTransportManageContact != null)
                    mOldTransportManageContact.requestFail(body);

                if (mTransportManageContact != null) mTransportManageContact.requestFail(body);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                if (mTransportManageContact != null) mTransportManageContact.requestFinish();
            }
        });
    }
}
