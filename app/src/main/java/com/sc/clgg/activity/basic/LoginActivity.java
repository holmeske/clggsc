//package com.sc.clgg.activity.basic;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.text.Editable;
//import android.text.TextUtils;
//import android.text.TextWatcher;
//import android.view.View;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.lvke.tools.widget.PopupWindowHelper;
//import com.sc.clgg.R;
//import com.sc.clgg.activity.contact.LoginContact;
//import com.sc.clgg.activity.forgetpassword.ForgetPasswordActivity;
//import com.sc.clgg.activity.presenter.LoginPresenter;
//import com.sc.clgg.activity.register.RegisterStartActivity;
//import com.sc.clgg.adapter.RecyclerAdapter;
//import com.sc.clgg.application.App;
//import com.sc.clgg.util.ConfigUtil;
//import com.sc.clgg.util.Tools;
//import com.sc.clgg.widget.loadbutton.CircularProgressButton;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.Unbinder;
//import toolbox.helper.LogHelper;
//import toolbox.helper.SharedPreferencesHelper;
//import toolbox.widget.ShapeTextView;
//
//public class LoginActivity extends AppCompatActivity implements LoginContact {
//
//    @BindView(R.id.et_account) EditText et_account;
//    @BindView(R.id.et_pwd) EditText et_pwd;
//    @BindView(R.id.btn_login) CircularProgressButton btn_login;
//    @BindView(R.id.ll_account_number) LinearLayout ll_account_number;
//    @BindView(R.id.ll_head) LinearLayout ll_head;
//    @BindView(R.id.tv_name) TextView tvName;
//    @BindView(R.id.tv_register) ShapeTextView mTvRegister;
//    @BindView(R.id.tv_switch_account) ShapeTextView mTvSwitchAccount;
//    @BindView(R.id.iv_delete_account) ImageButton mIvDeleteAccount;
//    @BindView(R.id.iv_delete_password) ImageButton mIvDeletePassword;
//
//    private Unbinder mUnbinder;
//    private LoginPresenter mLoginPresenter;
//    private PopupWindowHelper mPopupWindowHelper = new PopupWindowHelper();
//    private RecyclerAdapter adapter;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_login);
//        super.onCreate(savedInstanceState);
//        mUnbinder = ButterKnife.bind(this);
//
//        mLoginPresenter = new LoginPresenter(this, new ConfigUtil());
//
//        String account = SharedPreferencesHelper.SharedPreferences(App.getInstance()).getString("account", "");
//
//        String str = SharedPreferencesHelper.SharedPreferences(App.getInstance()).getString("history_account", "");
//        LogHelper.e("history_account = ：" + str);
//        String[] strings = str.split(",");
//        List<String> list = Arrays.asList(strings);
//        for (String s : list) {
//            LogHelper.e(s + "\n");
//        }
//
//        if (!TextUtils.isEmpty(account)) {
//            ll_head.setVisibility(View.VISIBLE);
//            mTvSwitchAccount.setVisibility(View.VISIBLE);
//            ll_account_number.setVisibility(View.GONE);
//            findViewById(R.id.space).setVisibility(View.GONE);
//            tvName.setText(account);
//            et_account.setText(account);
//        } else {
//            ll_head.setVisibility(View.GONE);
//            mTvSwitchAccount.setVisibility(View.GONE);
//            findViewById(R.id.space).setVisibility(View.VISIBLE);
//            ll_account_number.setVisibility(View.VISIBLE);
//            et_account.setText("");
//        }
//
//        et_pwd.addTextChangedListener(new TextWatcher() {
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
//                    mIvDeletePassword.setVisibility(View.VISIBLE);
//                } else {
//                    mIvDeletePassword.setVisibility(View.INVISIBLE);
//                }
//            }
//        });
//
//        initPopupWindow();
//    }
//
//    @Override
//    protected void onRestart() {
//        super.onRestart();
//        et_pwd.setText("");
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mUnbinder != null) {
//            mUnbinder.unbind();
//        }
//    }
//
//    private void initPopupWindow() {
//        adapter = new RecyclerAdapter();
//        adapter.setStringContact(s1 -> {
//            et_account.setText(s1);
//            et_account.setSelection(s1.length());
//            mPopupWindowHelper.dismiss();
//        });
//
//        View view = mPopupWindowHelper.init(LoginActivity.this, R.layout.pop_input_associate, null);
//        RecyclerView recyclerView = view.findViewById(R.id.rv);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(LoginActivity.this));
//    }
//
//    @OnClick({R.id.iv_delete_account, R.id.iv_delete_password, R.id.tv_register, R.id.btn_login, R.id.tv_forget_password, R.id.tv_switch_account})
//    void click(View v) {
//        switch (v.getId()) {
//            case R.id.iv_delete_account:
//                et_account.setText("");
//                break;
//
//            case R.id.iv_delete_password:
//                et_pwd.setText("");
//                break;
//
//            case R.id.tv_register:
//                startActivity(new Intent(this, RegisterStartActivity.class));
//                break;
//
//            case R.id.btn_login:
//                String userName = et_account.getText().toString().trim();
//                String password = et_pwd.getText().toString().trim();
//                if (TextUtils.isEmpty(userName)) {
//                    Tools.Toast(this, getString(R.string.username_cannot_empty));
//                    return;
//                }
//                if (TextUtils.isEmpty(password)) {
//                    Tools.Toast(this, getString(R.string.password_cannot_empty));
//                    return;
//                }
//                mLoginPresenter.loginToTXJ(userName, password);
//
//                break;
//
//            case R.id.tv_forget_password:
//                startActivity(new Intent(this, ForgetPasswordActivity.class));
//                break;
//
//            case R.id.tv_switch_account:
//                ll_head.setVisibility(View.GONE);
//                mTvSwitchAccount.setVisibility(View.GONE);
//                ll_account_number.setVisibility(View.VISIBLE);
//                findViewById(R.id.space).setVisibility(View.VISIBLE);
//                et_account.setText("");
//                et_account.requestFocus();
//                et_pwd.setText("");
//
//                et_account.addTextChangedListener(new TextWatcher() {
//                    @Override
//                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//                    }
//
//                    @Override
//                    public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                    }
//
//                    @Override
//                    public void afterTextChanged(Editable s) {
//                        if (s.length() > 0) {
//                            mIvDeleteAccount.setVisibility(View.VISIBLE);
//                        } else {
//                            mIvDeleteAccount.setVisibility(View.INVISIBLE);
//                        }
//
//                        String accounts = SharedPreferencesHelper.SharedPreferences(App.getInstance()).getString("history_account", "");
//                        LogHelper.e("history_account = ：" + accounts);
//
//                        List<String> list = Arrays.asList(accounts.split(","));
//
//                        List<String> newList = new ArrayList<>();
//                        int length = s.length();
//                        for (String str : list) {
//                            LogHelper.e("Editable = " + s);
//                            if (length > 0 && length < str.length() && TextUtils.equals(s, str.substring(0, length))) {
//                                newList.add(str);
//                            }
//                        }
//
//                        adapter.refresh(newList);
//                        mPopupWindowHelper.showAsDropDown(et_account, 0, 0);
//                    }
//                });
//                break;
//
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        System.exit(0);
//    }
//
//    @Override
//    public void onSuccess(String body) {
//    }
//
//    @Override
//    public void onError(String msg) {
//    }
//
//    @Override
//    public void onToast(String msg) {
//        Tools.Toast(this, msg);
//    }
//
//    @Override
//    public void onStartLoading() {
//        btn_login.startLoading();
//    }
//
//    @Override
//    public void setButtonSuccess() {
//        btn_login.setButtonSuccess();
//    }
//
//    @Override
//    public void JumpOtherActivity() {
//        startActivity(new Intent(this, MainActivity.class));
//        finish();
//        overridePendingTransition(0, R.anim.scale_out);
//    }
//
//}
