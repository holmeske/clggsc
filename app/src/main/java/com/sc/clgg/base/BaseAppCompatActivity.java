package com.sc.clgg.base;

import android.support.v7.app.AppCompatActivity;

import com.sc.clgg.dialog.LoadingDialog;

import tool.helper.LogHelper;

/**
 * @author lvke
 *         CreateDate：2017/8/23 16:42
 */

public class BaseAppCompatActivity extends AppCompatActivity {
    private LoadingDialog dialog;

    protected void showProgressDialog() {
        show();
    }

    protected void show() {
        if (dialog == null) {
            LogHelper.e("创建dialog");
            dialog = new LoadingDialog(this);
            dialog.show();
        } else {
            LogHelper.e("复用dialog");
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }
    }

    protected void hideProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }
}

