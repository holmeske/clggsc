package com.sc.clgg.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.tool.helper.DecimalFormatHelper;

/**
 * @author：lvke
 * @date：2018/9/26 18:04
 */
public class ConfirmCircleDialog extends Dialog {

    public ConfirmCircleDialog(@NonNull Context context) {
        super(context, R.style.dialog_base);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_confirm_circle);
        setCanceledOnTouchOutside(false);

        Window window = getWindow();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    public void setData(double v) {
        ((TextView) findViewById(R.id.tv_des_big)).setText(String.format(getContext().getString(R.string.confirm_circle), DecimalFormatHelper.formatTwo(v)));
        ((TextView) findViewById(R.id.tv_des_small)).setText(String.format(getContext().getString(R.string.can_write_str), DecimalFormatHelper.formatTwo(v)));
    }

    public void setConfirmListener(View.OnClickListener listener) {
        findViewById(R.id.tv_confirm).setOnClickListener(listener);
    }

    public void setCancelListener(View.OnClickListener listener) {
        findViewById(R.id.tv_cancel).setOnClickListener(listener);
    }

}
