package com.sc.clgg.activity.presenter;

import android.os.Handler;
import android.text.TextUtils;

import com.sc.clgg.R;
import com.sc.clgg.activity.contact.LoginContact;
import com.sc.clgg.application.App;
import com.sc.clgg.bean.User;
import com.sc.clgg.bean.UserInfoBean;
import com.sc.clgg.retrofit.RetrofitHelper;
import com.sc.clgg.tool.helper.LogHelper;
import com.sc.clgg.tool.helper.SharedPreferencesHelper;
import com.sc.clgg.util.ConfigUtil;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * 创建时间：2017/7/11 10:22
 *
 * @author lvke
 */

public class LoginPresenter {
    private LoginContact mLoginContact;

    public LoginPresenter(LoginContact mLoginContact) {
        this.mLoginContact = mLoginContact;
    }

    public void loginToTXJ(final String username, final String password) {
        getUserInfo(username, password);
    }

    private void getUserInfo(final String username, String password) {
        mLoginContact.onStartLoading();
        new RetrofitHelper().login(username, password).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                User user = response.body();
                if (!user.getSuccess()) {
                    mLoginContact.onToast(user.getMsg());
                    mLoginContact.setButtonSuccess();
                    return;
                }

                UserInfoBean bean = new UserInfoBean();
                bean.setUserCode(user.getUserCode());
                bean.setUserName(user.getUserName());
                bean.setPersonalPhone(user.getPhone());
                bean.setPassword(user.getPassword());
                bean.setAccount(user.getUserName());

                if (null != bean) {

                    String historyAccount = SharedPreferencesHelper.SharedPreferences(App.instance).getString("history_account", "");

                    StringBuilder sb = new StringBuilder();
                    if (historyAccount.length() != 0 && sb.indexOf(username) == -1) {
                        sb.append(historyAccount).append(",").append(username);
                    } else {
                        sb.append(username);
                    }

                    SharedPreferencesHelper.editor("sp").putString("history_account", sb.toString()).apply();

                    LogHelper.e("history_account = " + SharedPreferencesHelper.SharedPreferences(App.instance).getString("history_account", ""));

                    requestSuccess(bean);
                } else {
                    requestFail(user.getMsg());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                mLoginContact.onError(t.getMessage());
            }
        });
    }

    private void requestFail(String s) {
        if (!TextUtils.isEmpty(s)) {
            mLoginContact.onToast(s);
        }
        mLoginContact.setButtonSuccess();
    }

    private void requestSuccess(UserInfoBean bean) {
        mLoginContact.setButtonSuccess();

        if (bean != null) {
            new ConfigUtil().setUserInfo(bean);
            /*new RetrofitHelper().checkUser(bean.getUserCode()).enqueue(new Callback<Check>() {
                @Override
                public void onResponse(Call<Check> call, Response<Check> response) {

                }

                @Override
                public void onFailure(Call<Check> call, Throwable t) {

                }
            });*/

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mLoginContact.jumpOtherActivity();
                }
            }, 400);

        } else {
            mLoginContact.onToast(App.instance.getString(R.string.network_anomaly));
        }
    }

}
