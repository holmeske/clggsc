package com.sc.clgg.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * @Description描述:Scrollview潜逃ListView
 * @Author作者:lip
 * @Date日期:2014-12-2 下午4:57:00
 */
public class ScrollViewListView extends ListView {
	public ScrollViewListView(Context context) {
		super(context);
	}

	public ScrollViewListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ScrollViewListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}

}
