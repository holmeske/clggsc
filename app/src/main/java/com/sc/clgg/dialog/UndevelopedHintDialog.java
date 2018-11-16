package com.sc.clgg.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import com.sc.clgg.R;

import androidx.annotation.NonNull;

/**
 * @author：lvke
 * @date：2018/10/30 18:32
 */
public class UndevelopedHintDialog extends Dialog {
    public UndevelopedHintDialog(@NonNull Context context) {
        super(context,R.style.dialog_base);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_undeveloped_hint);
    }
}
