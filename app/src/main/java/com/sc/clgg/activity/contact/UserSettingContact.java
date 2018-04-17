package com.sc.clgg.activity.contact;

import com.sc.clgg.bean.VersionInfoBean;

/**
 * 作者：lvke
 * 创建时间：2017/7/21 14:06
 */

public interface UserSettingContact {
    void getVersionInfo(VersionInfoBean bean);

    void onError(String msg);
}
