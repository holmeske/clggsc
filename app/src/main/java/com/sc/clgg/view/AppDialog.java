package com.sc.clgg.view;

import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.sc.clgg.R;
import com.sc.clgg.application.App;

/**
 * 
 * @Description描述:自定义弹出框
 * @Author作者:lip
 * @Date日期:2014-10-20 上午11:14:53
 */
public final class AppDialog extends Dialog {

	/** 获取屏幕宽度与密度系数壁纸 */
	public static float getDensity(Context context) {
		Resources resources = context.getResources();
		DisplayMetrics dm = resources.getDisplayMetrics();
		return App.screenWidth / dm.density;
	}

	public AppDialog(Context context, int width, int height, int layout, int style) {
		super(context, style);
		setContentView(layout);
		Window window = getWindow();
		window.setWindowAnimations(R.style.mystyle); // 添加动画
		WindowManager.LayoutParams params = window.getAttributes();
		params.width = width;
		params.height = height;
		params.dimAmount = 0.5f;// 设置屏幕亮度
		params.gravity = Gravity.BOTTOM;
		window.setAttributes(params);
	}

}
