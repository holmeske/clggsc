package com.sc.clgg.dialog;

import android.app.Activity;

/**
 * Author：lvke
 * CreateDate：2017/10/9 20:23
 */

public class LoadingDialogHelper {
    private LoadingDialog mLoadingDialog;

    public LoadingDialogHelper(Activity activity) {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(activity);
            mLoadingDialog.setCanceledOnTouchOutside(true);
        }
    }

    public void show() {
        if (mLoadingDialog != null && !mLoadingDialog.isShowing()) {
            mLoadingDialog.show();
        }
    }

    public void dismiss() {
        if (null != mLoadingDialog && mLoadingDialog.isShowing()) {
            mLoadingDialog.dismiss();
        }
    }
}
