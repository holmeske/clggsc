package com.sc.clgg.activity.register;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sc.clgg.R;
import com.sc.clgg.application.App;
import com.sc.clgg.dialog.LoadingDialogHelper;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;
import com.sc.clgg.util.Tools;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tool.helper.SharedPreferencesHelper;

public class RegisterThirdActivity extends AppCompatActivity {

    @BindView(R.id.et_password) EditText mEtPassword;
    @BindView(R.id.tv_next) TextView mTvNext;
    @BindView(R.id.iv_delete_password) ImageButton mIvDeletePassword;
    private LoadingDialogHelper mLoadingDialogHelper;
    private String phone;

    @OnClick({R.id.tv_next, R.id.iv_delete_password})
    void a(View v) {
        switch (v.getId()) {
            case R.id.tv_next:
                String password = mEtPassword.getText().toString().trim();
                if (password.length() < 6) {
                    return;
                }

                HttpRequestHelper.register(phone, password, phone, new HttpCallBack() {
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
                            SharedPreferencesHelper.SharedPreferencesEditor(App.getInstance(), "sp").putString("account", phone).commit();
                            startActivity(new Intent(RegisterThirdActivity.this, RegisterSucceedActivity.class));
                        } else {
                            Tools.Toast(String.valueOf(map.get("msg")));
                        }
                    }
                });
                break;

            case R.id.iv_delete_password:
                mEtPassword.setText("");
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register_third);
        setTitle(getString(R.string.new_user_register));
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        mLoadingDialogHelper = new LoadingDialogHelper(this);
        phone = getIntent().getStringExtra("phone");
        mEtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    mIvDeletePassword.setVisibility(View.VISIBLE);
                } else {
                    mIvDeletePassword.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
