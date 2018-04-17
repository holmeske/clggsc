package com.sc.clgg.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sc.clgg.R;

public class HintSelectDialog extends Dialog {
    private View.OnClickListener onClickListener;
    private TextView mTvHint;

    public HintSelectDialog(Activity activity, View.OnClickListener onClickListener) {
        super(activity, R.style.dialog_base);
        this.onClickListener = onClickListener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_select_hint);
        setCanceledOnTouchOutside(true);

        mTvHint = (TextView) this.findViewById(R.id.tv_hint);

        findViewById(R.id.btn_yes).setOnClickListener(onClickListener);
        findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != this && isShowing()) dismiss();
            }
        });
    }

    public void setHint(String hint) {
        if (!TextUtils.isEmpty(hint) && mTvHint != null) {
            mTvHint.setText(hint);
        }
    }

}
