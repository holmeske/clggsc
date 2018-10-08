package com.sc.clgg.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sc.clgg.R;
import com.sc.clgg.adapter.MapInfoAdapter;
import com.sc.clgg.adapter.TelAdapter;
import com.sc.clgg.application.App;
import com.sc.clgg.bean.ServiceBean;
import com.sc.clgg.bean.ServiceStation;
import com.sc.clgg.tool.helper.MeasureHelper;
import com.sc.clgg.view.AppDialog;

import java.util.ArrayList;
import java.util.List;


public final class DialogUtil {
    public static void showCustomDialog(final Activity mActivity, final ServiceStation.Page.Station bean, int width, int height, int style, final ArrayList<ServiceBean> list) {
        final AppDialog dialog = new AppDialog(mActivity, width, height, R.layout.view_map_list, style);
        // 获取window中的子控件
        LinearLayout line_cancle = dialog.findViewById(R.id.line_cancle);
        line_cancle.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // Dialog消失
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();
                }
            }
        });

        // 导航地图列表
        ListView map_lv = dialog.findViewById(R.id.map_lv);
        MapInfoAdapter adapter = new MapInfoAdapter(mActivity);
        adapter.setmDatas(list);
        map_lv.setAdapter(adapter);
        map_lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Dialog消失
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();
                }

                if ("百度地图".equals(list.get(position).getNAME())) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("intent://map/marker?location=");
                    sb.append(bean.getLat());
                    sb.append(",");
                    sb.append(bean.getLon());
                    sb.append("&title=");
                    sb.append(bean.getStationName());
                    sb.append("&&content=");
                    sb.append(bean.getAddress());
                    sb.append("&src=yourCompanyName|yourAppName#Intent;scheme=bdapp;package=com.baidu.BaiduMap;end");

                    try {
                        // 移动APP调起Android百度地图方式举例
                        Intent intent = Intent.getIntent(sb.toString());
                        mActivity.startActivity(intent); // 启动调用
                    } catch (Exception e) {
                        return;
                    }

                } else if ("高德地图".equals(list.get(position).getNAME())) {
                    StringBuffer sb = new StringBuffer();
                    sb.append("androidamap://viewMap?sourceApplication=appname&poiname=");
                    sb.append(bean.getStationName());
                    sb.append("&lat=");
                    sb.append(bean.getLat());
                    sb.append("&lon=");
                    sb.append(bean.getLon());
                    sb.append("&dev=0");

                    try {
                        Intent intent = new Intent("android.intent.action.VIEW", Uri.parse(sb.toString()));
                        intent.setPackage("com.autonavi.minimap");
                        mActivity.startActivity(intent);
                    } catch (Exception e) {
                        return;
                    }
                }
            }
        });

        // 显示CustomDialog
        dialog.show();
    }

    public static void showTelDialog(final Activity mActivity, List<String> list) {
        final AppDialog dialog = new AppDialog(mActivity, MeasureHelper.getScreenWidth(App.instance), ViewGroup.LayoutParams.WRAP_CONTENT, R.layout.view_tel_list, R.style.Theme_dialog);
        dialog.setCanceledOnTouchOutside(true);

        // 导航地图列表
        ListView map_lv = dialog.findViewById(R.id.map_lv);
        TelAdapter adapter = new TelAdapter(mActivity);
        adapter.setData(list);
        map_lv.setAdapter(adapter);
        map_lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // Dialog消失
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                    dialog.cancel();
                }

                Tools.callPhone(list.get(position), mActivity);
            }
        });

        // 显示CustomDialog
        dialog.show();
    }
}
