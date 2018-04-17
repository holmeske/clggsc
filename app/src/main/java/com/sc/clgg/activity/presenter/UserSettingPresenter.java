package com.sc.clgg.activity.presenter;

import com.google.gson.Gson;
import com.sc.clgg.activity.contact.UserSettingContact;
import com.sc.clgg.bean.VersionInfoBean;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;

import tool.helper.LogHelper;

/**
 * 作者：lvke
 * 创建时间：2017/7/21 13:54
 */

public class UserSettingPresenter {

    private UserSettingContact mUserSettingContact;

    public UserSettingPresenter(UserSettingContact mUserSettingContact) {
        this.mUserSettingContact = mUserSettingContact;
    }

    public void checkUpdate() {
        HttpRequestHelper.checkUpdate(new HttpCallBack() {
            @Override
            public void onSuccess(String body) {

                try {
                    VersionInfoBean bean = new Gson().fromJson(body, VersionInfoBean.class);
                    mUserSettingContact.getVersionInfo(bean);
                } catch (Exception e) {
                    LogHelper.e(e);
                }

            }

            @Override
            public void onError(String body) {
                super.onError(body);
                mUserSettingContact.onError(body);
            }
        });
    }

}
