package com.sc.clgg.activity.forgetpassword;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ForgetPasswordActivity extends AppCompatActivity {

    @BindView(R.id.et_account) EditText mEtAccount;
    @BindView(R.id.iv_clear_acount) ImageView mIvClearAcount;
    @BindView(R.id.et_verification_code) EditText mEtVerificationCode;
    @BindView(R.id.tv_get_verification_code) TextView mTvGetVerificationCode;
    @BindView(R.id.tv_next) TextView mTvNext;

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
    private LoadingDialogHelper mLoadingDialogHelper;

    @OnClick(R.id.iv_clear_acount)
    void a() {
        mEtAccount.setText("");
    }

    @OnClick(R.id.tv_get_verification_code)
    void b() {
        String phone=mEtAccount.getText().toString().trim();
        if (phone.length()<6)return;
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

    @OnClick(R.id.tv_next)
    void c() {
        mTvGetVerificationCode.setEnabled(false);

        final String phone=mEtAccount.getText().toString().trim();

        if (phone.length()<6)return;

        String code = mEtVerificationCode.getText().toString().trim();

        if (code.length() != 6) return;
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
                    startActivity(new Intent(ForgetPasswordActivity.this, InputNewPasswordActivity.class).putExtra("phone",phone));
                    finish();
                } else {
                    Tools.Toast(String.valueOf(map.get("msg")));
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCountDownTimer != null) mCountDownTimer.cancel();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);
        setTitle("忘记密码");
        super.onCreate(savedInstanceState);

        mLoadingDialogHelper = new LoadingDialogHelper(this);

        mEtAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
         if (!TextUtils.isEmpty(s)){
             mIvClearAcount.setVisibility(View.VISIBLE);
         }else {
             mIvClearAcount.setVisibility(View.GONE);
         }
            }
        });

        mEtVerificationCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!TextUtils.isEmpty(s) && !TextUtils.isEmpty(mEtAccount.getText().toString())) {
                    mTvNext.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_angle_red_50));
                }
            }
        });
    }

}
