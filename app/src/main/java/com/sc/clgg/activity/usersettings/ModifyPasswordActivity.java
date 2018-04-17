//package com.sc.clgg.activity.usersettings;
//
//import android.content.Intent;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageButton;
//
//import com.alibaba.fastjson.JSON;
//import com.sc.clgg.R;
//import com.sc.clgg.activity.basic.LoginActivity;
//import com.sc.clgg.base.BaseActivity;
//import com.sc.clgg.dialog.LoadingDialogHelper;
//import com.sc.clgg.http.HttpCallBack;
//import com.sc.clgg.http.HttpRequestHelper;
//import com.sc.clgg.util.ConfigUtil;
//import com.sc.clgg.util.Tools;
//
//import java.util.Map;
//
//import butterknife.BindView;
//import butterknife.OnClick;
//
//public class ModifyPasswordActivity extends BaseActivity {
//    @BindView(R.id.et_old_password) EditText mEtOldPassword;
//    @BindView(R.id.et_new_password) EditText mEtNewPassword;
//    @BindView(R.id.et_again_new_password) EditText mEtAgainNewPassword;
//    @BindView(R.id.iv_delete_old_password) ImageButton mIvDeleteOldPassword;
//    @BindView(R.id.iv_delete_new_password) ImageButton mIvDeleteNewPassword;
//    @BindView(R.id.iv_delete_again_new_password) ImageButton mIvDeleteAgainNewPassword;
//    private LoadingDialogHelper mLoadingDialogHelper;
//
//    @Override
//    public void initTitle() {
//        setTitle(getString(R.string.modify_password));
//    }
//
//    @Override
//    protected int layoutRes() {
//        return R.layout.activity_modify_password;
//    }
//
//    @Override
//    protected void init() {
//        mLoadingDialogHelper = new LoadingDialogHelper(this);
//        initListener();
//    }
//
//    @OnClick({R.id.iv_delete_old_password, R.id.iv_delete_new_password, R.id.iv_delete_again_new_password})
//    void b(View v) {
//        switch (v.getId()) {
//            case R.id.iv_delete_old_password:
//                mEtOldPassword.setText("");
//                break;
//
//            case R.id.iv_delete_new_password:
//                mEtNewPassword.setText("");
//                break;
//
//            case R.id.iv_delete_again_new_password:
//                mEtAgainNewPassword.setText("");
//                break;
//
//            default:
//                break;
//        }
//    }
//
//    private void initListener() {
//        mEtOldPassword.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() > 0) {
//                    mIvDeleteOldPassword.setVisibility(View.VISIBLE);
//                } else {
//                    mIvDeleteOldPassword.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
//        mEtNewPassword.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() > 0) {
//                    mIvDeleteNewPassword.setVisibility(View.VISIBLE);
//                } else {
//                    mIvDeleteNewPassword.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
//        mEtAgainNewPassword.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//                if (s.length() > 0) {
//                    mIvDeleteAgainNewPassword.setVisibility(View.VISIBLE);
//                } else {
//                    mIvDeleteAgainNewPassword.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
//    }
//
//    @OnClick(R.id.confirm_btn)
//    void a() {
//        resetPassword();
//    }
//
//    public void resetPassword() {
//        String newPassword = mEtNewPassword.getText().toString();
//        String oldPassword = mEtOldPassword.getText().toString();
//        String againNewPassword = mEtAgainNewPassword.getText().toString();
//        if (TextUtils.isEmpty(oldPassword)) {
//            Tools.Toast("原始密码不能为空");
//            return;
//        }
//        if (newPassword.length() < 6) {
//            Tools.Toast("密码长度应在6-16位，当前" + newPassword.length() + "位！");
//            return;
//        }
//        if (TextUtils.isEmpty(newPassword)) {
//            Tools.Toast("新密码不能为空");
//            return;
//        }
//        if (TextUtils.isEmpty(againNewPassword)) {
//            Tools.Toast("第二次输入新密码不能为空");
//            return;
//        }
//        if (!newPassword.equals(againNewPassword)) {
//            Tools.Toast("两次密码不一致");
//            return;
//        }
//
//        if (newPassword.equals(oldPassword)) {
//            Tools.Toast("新旧密码不可相同");
//            return;
//        }
//
//        HttpRequestHelper.modifyPassword(new ConfigUtil().getUsername(), oldPassword, newPassword, new HttpCallBack() {
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
//                Map<String, Object> map = (Map<String, Object>) JSON.parse(body);
//                if ((boolean) map.get("status")) {
//                    Tools.Toast("密码修改成功");
//                    startActivity(new Intent(ModifyPasswordActivity.this, LoginActivity.class));
//                    finish();
//                } else {
//                    Tools.Toast(String.valueOf(map.get("msg")));
//                }
//            }
//        });
//    }
//}
