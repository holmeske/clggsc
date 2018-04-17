package com.sc.clgg.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.base.BaseAdNet;
import com.sc.clgg.bean.ServiceBean;

import java.util.ArrayList;

/**
 * @Description描述:导航信息适配器
 * @Author作者:lip
 * @Date日期:2014-11-20 上午10:31:29
 */
public class MapInfoAdapter extends BaseAdNet<ServiceBean> {

    public MapInfoAdapter(Activity activity) {
        super(activity);
    }

    /**
     * @param convertView
     * @param position
     * @return
     * @Description描述:设置布局数据
     */
    @Override
    protected View setConvertView(View convertView, int position) {

        ViewHolder holder = null;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_map, null);

            // 初始化子控件
            holder.tv_mapImg = (ImageView) convertView.findViewById(R.id.tv_mapImg);
            holder.tv_mapName = (TextView) convertView.findViewById(R.id.tv_mapName);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 集合数据不为空
        if (mDatas != null && !mDatas.isEmpty()) {
            final ServiceBean sb = mDatas.get(position);
            holder.tv_mapImg.setImageResource(sb.getID());
            holder.tv_mapName.setText(sb.getNAME());
        }

        return convertView;
    }

    /**
     * 设置数据的方法
     */
    public void setmDatas(ArrayList<ServiceBean> mDatas) {
        this.mDatas = mDatas;
    }

    /**
     * @Description描述:静态缓存内部类
     * @Author作者:lip
     * @Date日期:2014-11-20 上午10:38:23
     */
    final static class ViewHolder {

        ImageView tv_mapImg;// 图片
        TextView tv_mapName;// 名称
    }

}
