package com.sc.clgg.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.sc.clgg.R;

/**
 * @author：lvke
 * @date：2018/1/9 11:08
 */

public class LoadingDialog extends Dialog {

    public LoadingDialog(@NonNull Context context) {
        super(context, R.style.Theme_AppCompat_Dialog);
    }

    public LoadingDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected LoadingDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_loading);
    }

}
