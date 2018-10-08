package com.sc.clgg.tool.refresh;

import android.content.Context;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.sc.clgg.R;
import com.sc.clgg.tool.helper.MeasureHelper;

/**
 * 作者：lvke
 * 创建时间：2017/5/25 13:49
 * 描述：
 */

public class RefreshViewManager {
    private TwinklingRefreshLayout refreshLayout;

    public RefreshViewManager(TwinklingRefreshLayout refreshLayout) {
        this.refreshLayout = refreshLayout;
    }

    /*自定义刷新头部和底部的设置*/
    public void initSettings(Context context, boolean showFootView) {
        SinaRefreshView headerView = new SinaRefreshView(context);
        headerView.getView().setBackgroundColor(ContextCompat.getColor(context, R.color.gray_bg));
        headerView.setArrowResource(R.drawable.ic_pulltorefresh_arrow);
        headerView.setTextColor(ContextCompat.getColor(context, R.color.text_refresh));
        headerView.setRefreshingStr("加载中");

        ImageView refreshArrow = headerView.getView().findViewById(R.id.iv_arrow);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) refreshArrow.getLayoutParams();
        layoutParams.width = MeasureHelper.dp2px(context, 18);
        layoutParams.height = MeasureHelper.dp2px(context, 18);

        TextView tv = headerView.getView().findViewById(R.id.tv);
        tv.setTextSize(13);

        refreshLayout.setHeaderView(headerView);
        refreshLayout.setHeaderHeight(45);
        refreshLayout.setMaxHeadHeight(100);

        if (showFootView) {
            refreshLayout.setBottomView(new CustomFootview(context));
            refreshLayout.setBottomHeight(45);
            refreshLayout.setMaxBottomHeight(100);
        } else {
            refreshLayout.setEnableLoadmore(false);
        }

    }

    /*下拉超时处理*/
    public void pullRefreshTimeout() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (refreshLayout != null) refreshLayout.finishRefreshing();
            }
        }, 6000);
    }

    /*上拉超时处理*/
    public void loadMoreTimeout() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (refreshLayout != null) refreshLayout.finishLoadmore();
            }
        }, 6000);
    }
}
