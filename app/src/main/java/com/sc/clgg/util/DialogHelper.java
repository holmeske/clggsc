package com.sc.clgg.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.sc.clgg.R;
import com.sc.clgg.application.App;
import com.sc.clgg.bean.Service;
import com.sc.clgg.bean.ServiceStation;
import com.sc.clgg.tool.helper.LogHelper;
import com.sc.clgg.tool.helper.MeasureHelper;
import com.sc.clgg.dialog.AppDialog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


/**
 * @author lvke
 */
public final class DialogHelper {
    public static void showCustomDialog(final Activity mActivity,
                                        final ServiceStation.Page.Station bean,
                                        int width,
                                        int height,
                                        int style,
                                        final ArrayList<Service> list) {

        AppDialog dialog = new AppDialog(mActivity, width, height, R.layout.view_map_list, style);

        dialog.findViewById(R.id.line_cancle).setOnClickListener((v -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog.cancel();
            }
        }));

        ListView mapLv = dialog.findViewById(R.id.map_lv);

        List<Map<String, Object>> data = new ArrayList<>();
        for (Service service : list) {
            Map<String, Object> map = new HashMap<>(2);
            map.put("id", service.getId());
            map.put("name", Objects.requireNonNull(service.getName()));
            data.add(map);
        }
        mapLv.setAdapter(new SimpleAdapter(mActivity, data, R.layout.item_map, new String[]{"id", "name"}, new int[]{R.id.tv_mapImg, R.id.tv_mapName}));
        mapLv.setOnItemClickListener(((parent, view, position, id) -> {
            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog.cancel();
            }

            if (mActivity.getString(R.string.baidu_map).equals(list.get(position).getName())) {
                StringBuilder sb = new StringBuilder();
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
                    mActivity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString())));
                } catch (Exception e) {
                    LogHelper.e(e);
                }

            } else if (mActivity.getString(R.string.amap).equals(list.get(position).getName())) {
                StringBuilder sb = new StringBuilder();
                sb.append("androidamap://viewMap?sourceApplication=appname&poiname=");
                sb.append(bean.getStationName());
                sb.append("&lat=");
                sb.append(bean.getLat());
                sb.append("&lon=");
                sb.append(bean.getLon());
                sb.append("&dev=0");

                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(sb.toString()));
                    intent.setPackage("com.autonavi.minimap");
                    mActivity.startActivity(intent);
                } catch (Exception e) {
                    LogHelper.e(e);
                }
            }
        }));

        dialog.show();
    }

    public static void showTelDialog(final Activity mActivity, List<String> list) {
        AppDialog dialog = new AppDialog(mActivity,
                MeasureHelper.getScreenWidth(App.instance),
                ViewGroup.LayoutParams.WRAP_CONTENT,
                R.layout.view_tel_list,
                R.style.Theme_dialog);
        dialog.setCanceledOnTouchOutside(true);

        ListView mapLv = dialog.findViewById(R.id.map_lv);
        mapLv.setAdapter(new ArrayAdapter<>(mActivity, R.layout.item_tel, R.id.tv_mapName, list));
        mapLv.setOnItemClickListener((parent, view, position, id) -> {

            if (dialog.isShowing()) {
                dialog.dismiss();
                dialog.cancel();
            }

            Tools.callPhone(list.get(position), mActivity);
        });

        dialog.show();
    }
}
