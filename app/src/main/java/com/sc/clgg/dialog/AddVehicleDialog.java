package com.sc.clgg.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;

import com.sc.clgg.R;

/**
 * 添加车辆 提示dialog
 *
 * @author ZhangYi 2015-1-8 14:34:23
 */
public class AddVehicleDialog extends Dialog {
    private View.OnClickListener onClickListener;

    public AddVehicleDialog(Activity activity, View.OnClickListener onClickListener) {
        super(activity, R.style.dialog_base);
        this.onClickListener = onClickListener;
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_add_truck_hint);
        findViewById(R.id.btn_yes).setOnClickListener(onClickListener);
        findViewById(R.id.btn_no).setOnClickListener(onClickListener);
    }

}
