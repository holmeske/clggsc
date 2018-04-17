package com.sc.clgg.adapter.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sc.clgg.R;

public class MaintenanceViewHolder extends BaseRecyclerViewHolder {

    public ImageView company_img;
    public TextView company_name,// 公司名称
            company_instance, // 距离
            company_position, // 位置
            company_phone,// 电话
            company_person,// 联系人
            tv_map,// 导航
            tv_tel;// 联系电话
    public LinearLayout line_tel,// 打电话
            line_map;// 查看地图
    public LinearLayout itemLayout;
    public TextView tv_loadmore;

    public MaintenanceViewHolder(View convertView) {
        super(convertView);
        company_img = (ImageView) convertView.findViewById(R.id.company_img);
        company_name = (TextView) convertView.findViewById(R.id.company_name);
        company_instance = (TextView) convertView.findViewById(R.id.company_instance);
        company_position = (TextView) convertView.findViewById(R.id.company_position);
        company_phone = (TextView) convertView.findViewById(R.id.company_phone);
        company_person = (TextView) convertView.findViewById(R.id.company_person);
        line_tel = (LinearLayout) convertView.findViewById(R.id.line_tel);
        tv_tel = (TextView) convertView.findViewById(R.id.tv_tel);
        line_map = (LinearLayout) convertView.findViewById(R.id.line_map);
        tv_map = (TextView) convertView.findViewById(R.id.tv_map);
        itemLayout = (LinearLayout) convertView.findViewById(R.id.itemLayout);
        tv_loadmore = (TextView) convertView.findViewById(R.id.tv_loadmore);
    }

}
