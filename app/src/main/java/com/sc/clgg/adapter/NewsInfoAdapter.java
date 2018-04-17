package com.sc.clgg.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.base.BaseAdNet;
import com.sc.clgg.bean.NewsBean;

import java.util.ArrayList;

/**
 * 
 * @Description描述:商户促销新闻适配器
 * @Author作者:lip
 * @Date日期:2014-11-20 上午10:31:29
 */
public class NewsInfoAdapter extends BaseAdNet<NewsBean> {

	/**
	 * 
	 * @Description描述:静态缓存内部类
	 * @Author作者:lip
	 * @Date日期:2014-11-20 上午10:38:23
	 */
	final static class ViewHolder {
		TextView tv_first_name;// 名称
	}

	public NewsInfoAdapter(Activity activity) {
		super(activity);
	}

	/**
	 * 
	 * @Description描述:设置布局数据
	 * @param convertView
	 * @param position
	 * @return
	 */
	@Override
	protected View setConvertView(View convertView, int position) {

		ViewHolder holder = null;

		if (convertView == null) {

			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.item_news_layout, null);

			// 初始化子控件
			holder.tv_first_name = (TextView) convertView.findViewById(R.id.tv_first_name);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 集合数据不为空
		if (mDatas != null && !mDatas.isEmpty()) {
			final NewsBean sb = mDatas.get(position);
			holder.tv_first_name.setText(sb.getNEWSCONTENT());
		}

		return convertView;
	}

	/** 设置数据的方法 */
	public void setmDatas(ArrayList<NewsBean> mDatas) {
		this.mDatas = mDatas;
	}

}
