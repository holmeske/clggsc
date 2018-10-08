package com.sc.clgg.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

/**
 * @author：lvke
 * @date：2017/11/15 11:38
 */

public class AlertDialogHelper {

    public void show(Activity activity, String message,
                     DialogInterface.OnClickListener ok,
                     DialogInterface.OnClickListener cancel,
                     DialogInterface.OnDismissListener onDismissListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setMessage(message + "")
                .setPositiveButton("确定", ok)
                .setNegativeButton("取消", cancel);
        builder.show();
    }

    public void show(Activity activity, String message, final DialogInterface.OnClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setMessage(message + "")
                .setCancelable(true)
                .setPositiveButton("确定", listener)
                .setNegativeButton("取消", null);
        builder.show();
    }

}
