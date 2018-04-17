package com.sc.clgg.activity.register;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.sc.clgg.R;
import com.sc.clgg.activity.basic.WebActivity;
import com.sc.clgg.dialog.LoadingDialogHelper;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;
import com.sc.clgg.util.Tools;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import tool.helper.ActivityHelper;
import tool.helper.CheckHelper;

public class RegisterStartActivity extends AppCompatActivity {

    @BindView(R.id.et_phone) EditText mEtPhone;
    @BindView(R.id.tv_next) TextView mTvNext;
    @BindView(R.id.tv_next_hint) TextView mTvNextHint;
    private LoadingDialogHelper mLoadingDialogHelper;

    @OnClick({R.id.tv_next, R.id.iv_clear_phone})
    void a(View v) {
        switch (v.getId()) {
            case R.id.tv_next:
                final String phone = mEtPhone.getText().toString().trim();
                if (phone.length() < 11) {
                    return;
                }
                if (!CheckHelper.isCorrectPhone(phone)) {
                    Tools.Toast("请输入正确的手机号码");
                    return;
                }
                HttpRequestHelper.phoneNumberCheck(phone, new HttpCallBack() {
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
                            startActivity(new Intent(RegisterStartActivity.this, RegisterSecondActivity.class).putExtra("phone", phone));
                        } else {
                            Tools.Toast("账号已经存在");
                        }
                    }
                });
                break;

            case R.id.iv_clear_phone:
                mEtPhone.setText("");
                break;

            default:
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register_start);
        setTitle(getString(R.string.new_user_register));
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);

        mLoadingDialogHelper = new LoadingDialogHelper(this);

        String str = String.format(getString(R.string.click_next_step_hint), getString(R.string.service_agreement));

        Spannable ss = new SpannableString(str);
        ss.setSpan(new CustomClickableSpan(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((TextView) v).setHighlightColor(getResources().getColor(R.color.transparent));//方法重新设置文字背景为透明色。
                jumpToWeb();
            }
        }), str.length() - 5, str.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new StyleSpan(Typeface.BOLD), str.length() - 5, str.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        ss.setSpan(new ForegroundColorSpan(Color.BLACK), str.length() - 5, str.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

//       Spannable ss= TextHelper.instance.setSpannable(this,new String[]{"点击下一步,即表示你同意《","服务协议","》"},
//                new int[]{R.color.grey,R.color.black,R.color.grey},
//                new int[]{16,16,16});
//        ss.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(View widget) {
//
//            }
//        }, str.length() - 5, str.length() - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        mTvNextHint.setText(ss);
        mTvNextHint.setMovementMethod(LinkMovementMethod.getInstance());
        mEtPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    findViewById(R.id.iv_clear_phone).setVisibility(View.VISIBLE);
                } else {
                    findViewById(R.id.iv_clear_phone).setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void jumpToWeb() {
        ActivityHelper.startActivityScale(this, new Intent(this, WebActivity.class).putExtra("name", "服务协议").putExtra("url", "http://www.baidu.com"));
    }

    class CustomClickableSpan extends ClickableSpan {
        private final View.OnClickListener mListener;

        public CustomClickableSpan(View.OnClickListener l) {
            mListener = l;
        }

        /**
         * 重写父类点击事件
         */
        @Override
        public void onClick(View v) {
            mListener.onClick(v);
        }

        /**
         * 重写父类updateDrawState方法  我们可以给TextView设置字体颜色,背景颜色等等...
         */
        @Override
        public void updateDrawState(TextPaint ds) {
            ds.clearShadowLayer();
        }
    }

}
