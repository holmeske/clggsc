package com.sc.clgg.activity.presenter;

import android.content.Context;

import com.sc.clgg.R;
import com.sc.clgg.activity.contact.TruckManageContact;
import com.sc.clgg.bean.VersionInfoBean;
import com.sc.clgg.config.ConstantValue;
import com.sc.clgg.http.retrofit.RetrofitHelper;
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

    public void addressArrange(final Context context) {
        String[] regions = context.getResources().getStringArray(R.array.regions);
        for (String s : regions) {
            String[] codeRegion = s.split(",");
            ConstantValue.REGIONS.put(codeRegion[0].trim(), codeRegion[1].trim());
        }

        String[] citis = context.getResources().getStringArray(R.array.cities);
        for (String s : citis) {
            String[] codeCity = s.split(",");
            ConstantValue.CITIES.put(codeCity[0].trim(), codeCity[1].trim());
        }
    }
}
