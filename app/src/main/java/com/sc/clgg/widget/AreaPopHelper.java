package com.sc.clgg.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.sc.clgg.R;
import com.sc.clgg.activity.contact.ItemClickListener;
import com.sc.clgg.adapter.AreaAdapter;

import java.util.Arrays;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author：lvke
 * @date：2018/5/11 11:28
 */
public class AreaPopHelper {
    public static String[] areas_net;

    public static String[] areas = new String[]{
            "北京", "天津", "河北", "山西", "内蒙", "辽宁", "吉林", "黑龙江", "上海", "江苏",
            "浙江", "安徽", "福建", "江西", "山东", "河南", "湖北", "湖南", "广东", "广西",
            "海南", "重庆", "四川", "贵州", "云南", "西藏", "陕西", "甘肃", "青海", "宁夏",
            "新疆"};

    private PopupWindow mPopupWindow = new PopupWindow();
    private AreaAdapter mAreaAdapter;

    public PopupWindow init(Context context) {
        View view = View.inflate(context, R.layout.pop_area, null);
        RecyclerView mRecyclerView = view.findViewById(R.id.rv_area);
        mRecyclerView.setLayoutManager(new GridLayoutManager(context, 4, RecyclerView.VERTICAL, false));
        mAreaAdapter = new AreaAdapter(Arrays.asList(areas_net == null ? areas : areas_net));
        mRecyclerView.setAdapter(mAreaAdapter);
        mRecyclerView.setHasFixedSize(true);

        mPopupWindow.setContentView(view);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        mPopupWindow.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        return mPopupWindow;
    }


    public void setItemClickListener(ItemClickListener itemClickListener) {
        mAreaAdapter.setItemClickListener(itemClickListener);
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
