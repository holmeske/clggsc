package com.sc.clgg.activity.register;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sc.clgg.R;
import com.sc.clgg.dialog.LoadingDialogHelper;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;
import com.sc.clgg.util.Tools;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterSecondActivity extends AppCompatActivity {

    @BindView(R.id.et_verification_code) EditText mEtVerificationCode;
    @BindView(R.id.tv_get_verification_code) TextView mTvGetVerificationCode;
    @BindView(R.id.tv_next) TextView mTvNext;
    private LoadingDialogHelper mLoadingDialogHelper;
    private String phone;
    private CountDownTimer mCountDownTimer = new CountDownTimer(60000, 1000) {
        @Override
        public void onTick(long millisUntilFinished) {
            mTvGetVerificationCode.setText(millisUntilFinished / 1000 + "s");
        }

        @Override
        public void onFinish() {
            mTvGetVerificationCode.setText(getString(R.string.get_verification_code));
            mTvGetVerificationCode.setEnabled(true);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register_second);
        setTitle(getString(R.string.new_user_register));
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        phone = getIntent().getStringExtra("phone");

        mLoadingDialogHelper = new LoadingDialogHelper(this);

        HttpRequestHelper.sendVerificationCode(phone, new HttpCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                mLoadingDialogHelper.show();
            }

            @Override
            public void onSuccess(String body) {

                Map<String, Object> map = (Map<String, Object>) JSON.parse(body);
                if ((boolean) map.get("status")) {
                    mCountDownTimer.start();
                } else {
                    Tools.Toast(getString(R.string.network_anomaly));
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mLoadingDialogHelper.dismiss();
            }
        });
    }


    @OnClick(R.id.tv_next)
    void c() {
        mTvGetVerificationCode.setEnabled(false);
        String code = mEtVerificationCode.getText().toString().trim();
        if (code.length() != 6) {
            return;
        }
        HttpRequestHelper.verificationCodeCheck(phone, code, new HttpCallBack() {
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
                Map<String, Object> map = (Map<String, Object>) JSON.parse(body);
                if ((boolean) map.get("status")) {
                    mCountDownTimer.cancel();
                    startActivity(new Intent(RegisterSecondActivity.this, RegisterThirdActivity.class).putExtra("phone", phone));
                } else {
                    Tools.Toast(String.valueOf(map.get("msg")));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
}
