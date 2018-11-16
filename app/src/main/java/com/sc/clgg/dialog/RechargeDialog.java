package com.sc.clgg.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Toast;

import com.sc.clgg.R;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * @author：lvke
 * @date：2018/9/27 14:14
 */
public class RechargeDialog extends Dialog implements View.OnClickListener {
    private ImageView iv_alipay_state, iv_wechat_state;
    private View v_alipay, v_wechat,v_instant_recharge;
    private RadioButton rb_1, rb_2, rb_3, rb_4, rb_5, rb_6;
    private List<RadioButton> mRadioButtons = new ArrayList<>();
    private EditText et_amount;

    public RechargeDialog(@NonNull Context context) {
        super(context, R.style.dialog_base);
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
        v_instant_recharge= findViewById(R.id.v_instant_recharge);
        rb_1 = findViewById(R.id.rb_1);
        rb_2 = findViewById(R.id.rb_2);
        rb_3 = findViewById(R.id.rb_3);
        rb_4 = findViewById(R.id.rb_4);
        rb_5 = findViewById(R.id.rb_5);
        rb_6 = findViewById(R.id.rb_6);
        et_amount=findViewById(R.id.et_amount);

        mRadioButtons.add(rb_1);
        mRadioButtons.add(rb_2);
        mRadioButtons.add(rb_3);
        mRadioButtons.add(rb_4);
        mRadioButtons.add(rb_5);
        mRadioButtons.add(rb_6);
        for (RadioButton rb : mRadioButtons) {
            rb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (RadioButton rb : mRadioButtons) {
                        rb.setChecked(false);
                    }
                    rb.setChecked(true);
                    String text=rb.getText().toString();
                    et_amount.setText(text.subSequence(0,text.length()-1));
                }
            });
        }

        v_alipay.setOnClickListener(this);
        v_wechat.setOnClickListener(this);
        v_instant_recharge.setOnClickListener(this);

        iv_alipay_state.setSelected(true);
        et_amount.setText("500");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v_alipay:
                iv_alipay_state.setSelected(true);
                iv_wechat_state.setSelected(false);
                break;
            case R.id.v_wechat:
                iv_alipay_state.setSelected(false);
                iv_wechat_state.setSelected(true);
                break;
            case R.id.v_instant_recharge:
                Toast.makeText(getContext(), "立即充值", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
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
