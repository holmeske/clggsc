package com.sc.clgg.activity.presenter;

import com.sc.clgg.activity.contact.TruckManageContact;
import com.sc.clgg.bean.VersionInfoBean;
import com.sc.clgg.retrofit.RetrofitHelper;
import com.sc.clgg.tool.helper.LogHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 作者：lvke
 * 创建时间：2017/7/25 10:49
 */

public class TruckManagePresenter {
    private TruckManageContact mTruckManageContact;

    public TruckManagePresenter(TruckManageContact mTruckManageContact) {
        this.mTruckManageContact = mTruckManageContact;
    }

    public void checkUpdate() {
        new RetrofitHelper().getVersionInfo().enqueue(new Callback<VersionInfoBean>() {
            @Override
            public void onResponse(Call<VersionInfoBean> call, Response<VersionInfoBean> response) {
                try {
                    VersionInfoBean bean = response.body();
                    mTruckManageContact.getVersionInfo(bean);
                } catch (Exception e) {
                    LogHelper.e(e);
                }
            }

            @Override
            public void onFailure(Call<VersionInfoBean> call, Throwable t) {
            }
        });
    }

}
