package com.sc.clgg.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.base.BaseAdNet;
import com.sc.clgg.bean.ServiceBean;

/**
 * 
 * @Description描述:商户服务信息适配器
 * @Author作者:lip
 * @Date日期:2014-11-20 上午10:31:29
 */
public class ServiceInfoAdapter extends BaseAdNet<ServiceBean> {

	/**
	 * 
	 * @Description描述:静态缓存内部类
	 * @Author作者:lip
	 * @Date日期:2014-11-20 上午10:38:23
	 */
	final static class ViewHolder {

		ImageView tv_first_img;// 图片
		TextView tv_first_name;// 名称
	}

	public ServiceInfoAdapter(Activity activity) {
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
			convertView = mInflater.inflate(R.layout.item_service_layout, null);

			// 初始化子控件
			holder.tv_first_img = (ImageView) convertView.findViewById(R.id.tv_first_img);
			holder.tv_first_name = (TextView) convertView.findViewById(R.id.tv_first_name);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// 集合数据不为空
		if (mDatas != null && !mDatas.isEmpty()) {
			final ServiceBean sb = mDatas.get(position);
			// 优先加载图片
			// App.loadImage(sb.getICON(), holder.tv_first_img);
			String name = sb.getNAME();
			holder.tv_first_name.setText(name);
			if ("紧急救援服务".equals(name)) {
				holder.tv_first_img.setBackgroundResource(R.drawable.jinjijiuyuan);
			} else if ("现场抢修服务".equals(name)) {
				holder.tv_first_img.setBackgroundResource(R.drawable.xiulibaoyang);
			} else if ("轮胎更换服务".equals(name)) {
				holder.tv_first_img.setBackgroundResource(R.drawable.service_lun);
			} else if ("加油".equals(name)) {
				holder.tv_first_img.setBackgroundResource(R.drawable.service_gas);
			} else if ("加油加气".equals(name)) {
				holder.tv_first_img.setBackgroundResource(R.drawable.service_gas);
			} else if ("停车场".equals(name)) {
				holder.tv_first_img.setBackgroundResource(R.drawable.service_parking);
			}
		}

		return convertView;
	}

	/** 设置数据的方法 */
	public void setmDatas(ArrayList<ServiceBean> mDatas) {
		this.mDatas = mDatas;
	}

}
