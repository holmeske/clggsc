package com.sc.clgg.activity.forgetpassword;

import android.os.Bundle;
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

public class InputNewPasswordActivity extends AppCompatActivity {

    @BindView(R.id.et_new_password) EditText mEtNewPassword;
    @BindView(R.id.tv_complete) TextView mTvComplete;
    private LoadingDialogHelper mLoadingDialogHelper;

    @OnClick(R.id.tv_complete)
    void a() {
        String newPassword=mEtNewPassword.getText().toString().trim();
        if (newPassword.length()<6)return;

        HttpRequestHelper.resetPassword(getIntent().getStringExtra("phone"), newPassword, new HttpCallBack() {
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
                    finish();
                } else {
                    Tools.Toast(String.valueOf(map.get("msg")));
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_input_new_password);
        setTitle("忘记密码");
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mLoadingDialogHelper =new LoadingDialogHelper(this);
    }
}
