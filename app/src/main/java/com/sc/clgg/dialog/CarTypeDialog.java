package com.sc.clgg.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.sc.clgg.R;

import androidx.annotation.NonNull;

/**
 * @author：lvke
 * @date：2018/11/16 09:50
 */
public class CarTypeDialog extends Dialog {
    public CarTypeDialog(@NonNull Context context) {
        super(context,R.style.dialog_base);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
}
