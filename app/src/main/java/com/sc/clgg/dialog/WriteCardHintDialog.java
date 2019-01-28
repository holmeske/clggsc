package com.sc.clgg.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.sc.clgg.R;

import androidx.annotation.NonNull;

/**
 * @author：lvke
 * @date：2018/9/26 18:04
 */
public class WriteCardHintDialog extends Dialog {

    public WriteCardHintDialog(@NonNull Context context) {
        super(context, R.style.dialog_base);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_write_card_hint);
        setCanceledOnTouchOutside(false);

        Window window = getWindow();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }

    public void setData(String before, String after) {
        ((TextView) findViewById(R.id.tv_card_before)).setText("目前连接卡号: " + before);
        ((TextView) findViewById(R.id.tv_card_after)).setText("即将圈存卡号: " + after);
    }

    public void setCancelListener(View.OnClickListener listener) {
        findViewById(R.id.tv_cancel).setOnClickListener(listener);
    }

}
