package com.sc.clgg.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sc.clgg.R;
import com.sc.clgg.activity.contact.ItemClickListener;
import com.sc.clgg.activity.etc.ResultNoticeActivity;
import com.sc.clgg.bean.StatusBean;
import com.sc.clgg.retrofit.PayhelperKt;
import com.sc.clgg.retrofit.RetrofitHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * @author：lvke
 * @date：2018/9/27 14:14
 */
public class RechargeDialog extends Dialog implements View.OnClickListener {
    private ImageView iv_alipay_state, iv_wechat_state;
    private View v_alipay, v_wechat, v_instant_recharge;
    private TextView rb_1, rb_2, rb_3, rb_4, rb_5, rb_6;
    private List<TextView> mRadioButtons = new ArrayList<>();
    private EditText et_amount;
    private TextView tv_recharge_amount;

    private String cardNumber;

    private Activity mActivity;
    private ItemClickListener mItemClickListener;
    private ItemClickListener mWasteSnListener;

    public RechargeDialog(@NonNull Context context) {
        super(context, R.style.dialog_base);
        mActivity = (Activity) context;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_recharge);
        setCanceledOnTouchOutside(true);
        init();
        initStyle();
    }

    private void init() {
        iv_alipay_state = findViewById(R.id.iv_alipay_state);
        iv_wechat_state = findViewById(R.id.iv_wechat_state);

        v_alipay = findViewById(R.id.v_alipay);
        v_wechat = findViewById(R.id.v_wechat);
        v_instant_recharge = findViewById(R.id.v_instant_recharge);
        rb_1 = findViewById(R.id.rb_1);
        rb_2 = findViewById(R.id.rb_2);
        rb_3 = findViewById(R.id.rb_3);
        rb_4 = findViewById(R.id.rb_4);
        rb_5 = findViewById(R.id.rb_5);
        rb_6 = findViewById(R.id.rb_6);
        et_amount = findViewById(R.id.et_amount);
        tv_recharge_amount = findViewById(R.id.tv_recharge_amount);

        mRadioButtons.add(rb_1);
        mRadioButtons.add(rb_2);
        mRadioButtons.add(rb_3);
        mRadioButtons.add(rb_4);
        mRadioButtons.add(rb_5);
        mRadioButtons.add(rb_6);
        for (TextView rb : mRadioButtons) {
            rb.setOnClickListener(v -> {
                for (TextView rb1 : mRadioButtons) {
                    rb1.setSelected(false);
                }
                rb.setSelected(true);
                String text = rb.getText().toString();
                et_amount.setText(text.subSequence(0, text.length() - 1));
                tv_recharge_amount.setText(text.subSequence(0, text.length() - 1));
            });
        }

        v_alipay.setOnClickListener(this);
        v_wechat.setOnClickListener(this);
        v_instant_recharge.setOnClickListener(this);

        rb_2.setSelected(true);
        iv_wechat_state.setSelected(true);
        et_amount.setText("500");
        tv_recharge_amount.setText("500");

        et_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                tv_recharge_amount.setText(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_alipay:
                //iv_alipay_state.setSelected(true);
                //iv_wechat_state.setSelected(false);
                break;
            case R.id.v_wechat:
                iv_alipay_state.setSelected(false);
                iv_wechat_state.setSelected(true);
                break;
            case R.id.v_instant_recharge:
                String money = tv_recharge_amount.getText().toString();
                if (TextUtils.isEmpty(money.trim())){
                    Toast.makeText(mActivity, "请输入充值金额", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (mItemClickListener != null) {
                    mItemClickListener.click(money);
                }
                int amount = Integer.parseInt(money);
                /*if (amount < 100 || amount > 1000000) {
                    Toast.makeText(mActivity.getApplication(), "请输入大于等于100小于1000000的整数", Toast.LENGTH_SHORT).show();
                    return;
                }*/

                LoadingDialog dialog = new LoadingDialog(mActivity);
                dialog.show();
                new RetrofitHelper().payMoney(cardNumber, money).enqueue(new Callback<StatusBean>() {
                    @Override
                    public void onResponse(Call<StatusBean> call, Response<StatusBean> response) {
                        dismiss();
                        if (dialog!=null){
                            dialog.dismiss();
                        }
                        if (response.body().getSuccess()) {
                            if (mWasteSnListener != null) {
                                mWasteSnListener.click(Objects.requireNonNull(response.body()).getWasteSn());
                            }
                            PayhelperKt.wxPay(mActivity, cardNumber, money, response.body().getWasteSn());
                        } else {
                            mActivity.startActivity(new Intent(mActivity, ResultNoticeActivity.class).putExtra("msg", response.body().getMsg()));
                        }
                    }

                    @Override
                    public void onFailure(Call<StatusBean> call, Throwable t) {
                        dismiss();
                        if (dialog!=null){
                            dialog.dismiss();
                        }
                        mActivity.startActivity(new Intent(mActivity, ResultNoticeActivity.class).putExtra("msg", "系统延迟，请稍后再试。"));
                    }
                });
                break;

            default:
                break;
        }
    }

    public void setWasteSnListener(ItemClickListener wasteSnListener) {
        mWasteSnListener = wasteSnListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    private void initStyle() {
        Window window = getWindow();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
    }
}
