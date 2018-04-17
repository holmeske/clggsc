package com.sc.clgg.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 自适应高度的ListView
 * 
 * @author ZhangYi 2013-10-18 17:29:02
 */
public class ExtraListView extends ListView {

	public ExtraListView(Context context) {
		super(context);
	}

	public ExtraListView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public ExtraListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
