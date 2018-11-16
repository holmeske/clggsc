package com.sc.clgg.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.base.BaseAdNet;

import java.util.List;

public class TelAdapter extends BaseAdNet<String> {

    public TelAdapter(Activity activity) {
        super(activity);
    }

    @Override
    protected View setConvertView(View convertView, int position) {

        ViewHolder holder;

        if (convertView == null) {

            holder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_tel, null);

            // 初始化子控件
            holder.tv_mapName = convertView.findViewById(R.id.tv_mapName);

            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 集合数据不为空
        if (mDatas != null && !mDatas.isEmpty()) {
            holder.tv_mapName.setText(mDatas.get(position));
        }

        return convertView;
    }

    public void setData(List<String> list) {
        this.mDatas = list;
    }

    final static class ViewHolder {

        TextView tv_mapName;// 名称
    }

}
