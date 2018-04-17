package com.sc.clgg.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.activity.transportmanager.WaybillDetailActivity;
import com.sc.clgg.base.BaseAdNet;
import com.sc.clgg.bean.IncomeBean;

import java.util.List;

/**
 * 选择车辆 适配器
 *
 * @author ZhangYi 2014-12-4 14:08:39
 */
public class IncomeAdapter extends BaseAdNet<IncomeBean.DataBeanX.DataBean> {

    public IncomeAdapter(Activity activity, List<IncomeBean.DataBeanX.DataBean> list) {
        super(activity);
        mDatas = list;
    }

    @Override
    protected View setConvertView(View convertView, int position) {
        convertView = mInflater.inflate(R.layout.item_income_list, null);
        TextView tv_data = (TextView) convertView.findViewById(R.id.tv_data);
        TextView tv_addresss = (TextView) convertView.findViewById(R.id.tv_addresss);
        TextView tv_price = (TextView) convertView.findViewById(R.id.tv_price);

        final IncomeBean.DataBeanX.DataBean bean = mDatas.get(position);

        if (null != bean) {

            tv_data.setText(bean.getDay());

            if (!TextUtils.isEmpty(bean.getAddress())) {
                tv_addresss.setText(bean.getAddress());
            }
            tv_price.setText("+" + bean.getAmount());
        }


        convertView.setOnClickListener(v -> {
            if (TextUtils.isEmpty(bean.getWaybillNo()) || TextUtils.isEmpty(bean.getWaybillStatus())) {
                return;
            }
            Intent intent = new Intent(v.getContext(), WaybillDetailActivity.class);
            intent.putExtra("order_no", bean.getWaybillNo());
            intent.putExtra("order_status", bean.getWaybillStatus());
            v.getContext().startActivity(intent);
        });

        return convertView;
    }

}
