package com.sc.clgg.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.sc.clgg.R;

import androidx.annotation.NonNull;

/**
 * @author：lvke
 * @date：2018/11/16 09:50
 */
public class CarAttributeDialog extends Dialog {
    private String attribute;

    public CarAttributeDialog(@NonNull Context context, String attribute) {
        super(context, R.style.dialog_base);
        this.attribute = attribute;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (attribute.equals("cardType")) {
            setContentView(R.layout.dialog_car_type);
        } else if (attribute.equals("function")) {
            setContentView(R.layout.dialog_car_function);
        }

        findViewById(R.id.iv_close).setOnClickListener(v -> dismiss());
        findViewById(R.id.tv_i_know).setOnClickListener(v -> dismiss());

        Window window = getWindow();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(lp);
    }
}
