package com.sc.clgg.activity.presenter;

import com.google.gson.Gson;
import com.sc.clgg.activity.contact.BusinessInfoContact;
import com.sc.clgg.bean.StoreInfoDetailBean;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;

/**
 * Author：lvke
 * CreateDate：2017/8/16 13:38
 */

public class BusinessInfoPresenter {
    private BusinessInfoContact contact;

    public BusinessInfoPresenter(BusinessInfoContact contact) {
        this.contact = contact;
    }

    public void loadData(String userid, String storeid) {
        HttpRequestHelper.getBusinessInfo(userid, storeid, new HttpCallBack() {
            @Override
            public void onSuccess(String body) {
                StoreInfoDetailBean bean = new Gson().fromJson(body, StoreInfoDetailBean.class);
                if (bean != null) {
                    if (contact != null) {
                        contact.onSuccess(bean);
                    }
                }
            }

            @Override
            public void onError(String body) {
                super.onError(body);
                if (contact != null) {
                    contact.onError(body);
                }
            }
        });
    }
}
