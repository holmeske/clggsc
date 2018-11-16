package com.sc.clgg.base;

import com.sc.clgg.dialog.LoadingDialog;
import com.sc.clgg.tool.helper.LogHelper;

import androidx.appcompat.app.AppCompatActivity;


/**
 * @author lvke
 * CreateDate：2017/8/23 16:42
 */

public class BaseAppCompatActivity extends AppCompatActivity {
    private LoadingDialog dialog;

    protected void showProgressDialog() {
        show();
    }
    protected void showProgressDialog(String msg) {
        show(msg);
    }

    private void show(String msg) {
        if (dialog == null) {
            LogHelper.e("创建dialog");
            dialog = new LoadingDialog(this);
            dialog.show();
            dialog.setContent(msg);
        } else {
            LogHelper.e("复用dialog");
            if (!dialog.isShowing()) {
                dialog.show();
                dialog.setContent(msg);
            }
        }
    }

    private void show() {
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

