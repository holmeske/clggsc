package com.lvke.tools.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

/**
 * @author：lvke
 * @date：2017/11/24 11:21
 */

public class PopupWindowHelper {
    private PopupWindow mPopupWindow = new PopupWindow();

    public View init(Context context, @LayoutRes int resource, ViewGroup root) {
        View view = View.inflate(context, resource, root);
        mPopupWindow.setContentView(view);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mPopupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        return view;
    }

    public void showAsDropDown(View anchor, int xoff, int yoff) {
        mPopupWindow.showAsDropDown(anchor, xoff, yoff);
    }

    public void dismiss() {
        if (mPopupWindow != null && mPopupWindow.isShowing()) {
            mPopupWindow.dismiss();
        }
    }

}
