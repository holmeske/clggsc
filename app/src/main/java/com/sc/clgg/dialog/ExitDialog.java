package com.sc.clgg.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;

/**
 * 作者：lvke
 * 创建时间：2017/7/24 13:38
 */

public class ExitDialog extends CLGGDialogBuilder {
    private Activity activity;

    public ExitDialog(Context context) {
        super(context);
        if (context instanceof Activity) {
            activity = (Activity) context;
        }
    }

    public void show(String title, String msg, DialogInterface.OnClickListener listener) {
        if (activity != null && !activity.isFinishing()) {
            new CLGGDialogBuilder(activity)
                    .setTitle(title)
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setMessage(msg)
                    .setPositiveButton("确定", listener)
                    .setNegativeButton("取消", null)
                    .show();
        }
    }
}
