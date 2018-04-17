package com.sc.clgg.util;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.sc.clgg.R;
import com.sc.clgg.adapter.MapInfoAdapter;
import com.sc.clgg.bean.ServiceBean;
import com.sc.clgg.bean.StoreInfoBean;
import com.sc.clgg.view.AppDialog;

import java.util.ArrayList;

/**
 * @Description描述:Dialog对话框工具类
 * @Author作者:lip
 * @Date日期:2014-12-16 上午10:07:56
 */
public final class DialogUtil {

	/**
	 * @Description描述: 导航地图CustomDialog
	 * @param mActivity
	 * @param width
	 * @param height
	 * @param style
	 * @param list
	 */
	public static void showCustomDialog(final Activity mActivity, final StoreInfoBean bean, int width, int height, int style, final ArrayList<ServiceBean> list) {
		final AppDialog dialog = new AppDialog(mActivity, width, height, R.layout.view_map_list, style);
		// 获取window中的子控件
		LinearLayout line_cancle = (LinearLayout) dialog.findViewById(R.id.line_cancle);
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
		ListView map_lv = (ListView) dialog.findViewById(R.id.map_lv);
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
					sb.append(bean.getLng());
					sb.append("&title=");
					sb.append(bean.getName());
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
					sb.append(bean.getName());
					sb.append("&lat=");
					sb.append(bean.getLat());
					sb.append("&lon=");
					sb.append(bean.getLng());
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

}
