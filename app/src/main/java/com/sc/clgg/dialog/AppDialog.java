package com.sc.clgg.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.sc.clgg.R;

import java.util.Objects;

public final class AppDialog extends Dialog {

    public AppDialog(Context context, int width, int height, int layout, int style) {
        super(context, style);
        setContentView(layout);
        Window window = getWindow();
        // 添加动画
        Objects.requireNonNull(window).setWindowAnimations(R.style.mystyle);
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = width;
        params.height = height;
        // 设置屏幕亮度
        params.dimAmount = 0.5f;
        params.gravity = Gravity.BOTTOM;
        window.setAttributes(params);
    }

}
