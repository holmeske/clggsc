package com.sc.clgg.activity.forgetpassword;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.EditText;
import android.widget.Toast;

import com.sc.clgg.R;
import com.sc.clgg.base.BaseImmersionActivity;
import com.sc.clgg.bean.StatusBean;
import com.sc.clgg.dialog.LoadingDialogHelper;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;
import com.sc.clgg.http.retrofit.RetrofitHelper;
import com.sc.clgg.tool.widget.ShapeTextView;
import com.sc.clgg.util.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author lvke
 */
public class ForgetPasswordActivity extends BaseImmersionActivity {

    @BindView(R.id.et_account) EditText mEtAccount;
    @BindView(R.id.et_verification_code) EditText mEtVerificationCode;
    @BindView(R.id.et_password) EditText mEtPassWord;

    @BindView(R.id.tv_get_verification_code) ShapeTextView mTvGetVerificationCode;

    private CountDownTimer mCountDownTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mTvGetVerificationCode.setText(String.format(getString(R.string.replace), millisUntilFinished / 1000));
        }

        @Override
        public void onFinish() {
            mTvGetVerificationCode.setText(getString(R.string.get_verification_code));
            mTvGetVerificationCode.setEnabled(true);
        }
    };
    private LoadingDialogHelper mLoadingDialogHelper;
    private Call<StatusBean> http;

    @OnClick(R.id.tv_get_verification_code)
    void b() {
        String phone = mEtAccount.getText().toString().trim();
        if (phone.length() < 6) {
            return;
        }
        HttpRequestHelper.sendVerificationCode(phone, new HttpCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                mLoadingDialogHelper.show();
            }

            @Override
            public void onSuccess(String body) {
                mCountDownTimer.start();
            }


            @Override
            public void onFinish() {
                super.onFinish();
                mLoadingDialogHelper.dismiss();
            }
        });
    }

    @OnClick(R.id.tv_ok)
    void c() {
        mTvGetVerificationCode.setEnabled(false);

        final String phone = mEtAccount.getText().toString().trim();

        if (phone.length() < 6) {
            return;
        }

        String code = mEtVerificationCode.getText().toString().trim();

        if (code.length() != 6) {
            return;
        }

        mLoadingDialogHelper.show();
        new RetrofitHelper().authCodeCheck(phone, code).enqueue(new Callback<StatusBean>() {
            @Override
            public void onResponse(Call<StatusBean> call, Response<StatusBean> response) {
                mLoadingDialogHelper.dismiss();

                if (response.body() != null) {
                    if (response.body().getStatus()) {
                        resetPwd(phone);
                    } else {
                        Tools.Toast(response.body().getMsg());
                    }
                }

            }

            @Override
            public void onFailure(Call<StatusBean> call, Throwable t) {
                mLoadingDialogHelper.dismiss();
            }
        });

//        HttpRequestHelper.verificationCodeCheck(phone, code, new HttpCallBack() {
//            @Override
//            public void onStart() {
//                super.onStart();
//                mLoadingDialogHelper.show();
//            }
//
//            @Override
//            public void onFinish() {
//                super.onFinish();
//                super.onStart();
//                mLoadingDialogHelper.dismiss();
//            }
//
//            @Override
//            public void onSuccess(String body) {
//                StatusBean statusBean = new Gson().fromJson(body, StatusBean.class);
//                if (statusBean.getStatus()) {
//                    resetPwd(phone);
//                } else {
//                    Tools.Toast(statusBean.getMsg());
//                }
//            }
//        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        if (http != null) {
            http.cancel();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pass);
        unbinder = ButterKnife.bind(this);
        initTitle("忘记密码");

        mLoadingDialogHelper = new LoadingDialogHelper(this);

    }

    private void resetPwd(String phone) {
        String newPassword = mEtPassWord.getText().toString().trim();
        if (newPassword.length() < 6) {
            Toast.makeText(this, "密码不能少于6位", Toast.LENGTH_SHORT).show();
            return;
        }
        http = new RetrofitHelper().resetPassword(phone, newPassword);
        mLoadingDialogHelper.show();
        http.enqueue(new Callback<StatusBean>() {
            @Override
            public void onResponse(Call<StatusBean> call, Response<StatusBean> response) {
                mLoadingDialogHelper.dismiss();

                StatusBean statusBean = response.body();
                if (statusBean.getStatus()) {
                    Toast.makeText(ForgetPasswordActivity.this, "密码重置成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Tools.Toast(statusBean.getMsg());
                }
            }

            @Override
            public void onFailure(Call<StatusBean> call, Throwable t) {
                mLoadingDialogHelper.dismiss();

                mCountDownTimer.cancel();
                mTvGetVerificationCode.setText(getString(R.string.get_verification_code));
                mTvGetVerificationCode.setEnabled(true);
            }
        });


        /*HttpRequestHelper.resetPassword(phone, newPassword, new HttpCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                mLoadingDialogHelper.show();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                super.onStart();
                mLoadingDialogHelper.dismiss();
            }

            @Override
            public void onSuccess(String body) {
                StatusBean statusBean = new Gson().fromJson(body, StatusBean.class);
                if (statusBean.getStatus()) {
                    Toast.makeText(ForgetPasswordActivity.this, "密码重置成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Tools.Toast(statusBean.getMsg());
                }
            }

            @Override
            public void onError(String body) {
                super.onError(body);
                mCountDownTimer.cancel();
                mTvGetVerificationCode.setText(getString(R.string.get_verification_code));
                mTvGetVerificationCode.setEnabled(true);
            }
        });*/
    }
}
