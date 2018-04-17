package com.sc.clgg.activity.presenter;

import com.google.gson.Gson;
import com.sc.clgg.R;
import com.sc.clgg.activity.contact.CommonContact;
import com.sc.clgg.application.App;
import com.sc.clgg.bean.ServiceSiteBean;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;

import tool.helper.LogHelper;

/**
 * Author：lvke
 * CreateDate：2017/8/11 13:51
 */

public class FuelGasPresenter {
    private CommonContact contact;

    public FuelGasPresenter(CommonContact contact) {
        this.contact = contact;
    }

    public void loadData(int pageno, int pageSize) {
        HttpRequestHelper.getMaintenanceData("5", pageno, pageSize, new HttpCallBack() {

            @Override
            public void onStart() {
                super.onStart();
                if (contact != null) contact.onStartRequest();
            }

            @Override
            public void onSuccess(String body) {

                ServiceSiteBean bean = new Gson().fromJson(body, ServiceSiteBean.class);

                if (bean != null) {
                    if (bean.getPage() != null) {
                        if (bean.getPage().getList() != null) {
                            if (contact != null) contact.onSuccess(bean.getPage().getList());
                        }
                    }
                }
            }

            @Override
            public void onError(String body) {
                super.onError(body);
                if (contact != null)
                    contact.onError(App.getInstance().getString(R.string.network_anomaly));
            }

            @Override
            public void onFinish() {
                super.onFinish();
                LogHelper.e("onFinish()");
                if (contact != null) contact.onFinish();
            }
        });
    }
}
