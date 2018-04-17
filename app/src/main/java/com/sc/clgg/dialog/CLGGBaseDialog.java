package com.sc.clgg.dialog;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public abstract class CLGGBaseDialog {

    //有一个dialog
    protected CLGGDialogBuilder mBuilder;
    protected LayoutInflater mInflater;
    //有一个view
    protected View mView;
    //有一个context
    protected Context mContext;

    public CLGGBaseDialog(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        if (mBuilder == null) {
            mBuilder = new CLGGDialogBuilder(mContext);
        }
        mBuilder.create();
    }

    public CLGGDialogBuilder.QDAlertDialog getDialog() {
        return mBuilder.mDialog;
    }

    protected abstract View getView();

    public void show() {
        if (mBuilder != null) {
            if (mView == null) {
                mView = getView();
                mBuilder.setViewNoPadding(mView);
            }
            mBuilder.show();
        }
    }

    public void dismiss() {
        if (mBuilder != null) {
            mBuilder.dismiss();
        }
    }

}
