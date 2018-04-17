package com.sc.clgg.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.base.BaseAdNet;
import com.sc.clgg.bean.ExpendListDetailBean;
import com.sc.clgg.util.TimeHelper;

import java.util.Date;

/**
 * 选择车辆 适配器
 *
 * @author ZhangYi 2014-12-4 14:08:39
 */
public class ExpendListDetailAdapter extends BaseAdNet<ExpendListDetailBean.DataBean> {

    public ExpendListDetailAdapter(Activity activity, ExpendListDetailBean data) {
        super(activity);

        if (data.getData() != null && data.getData().size() > 0) {
            mDatas = data.getData();
        }
    }


    @Override
    protected View setConvertView(View convertView, int position) {
        convertView = mInflater.inflate(R.layout.item_expend_list_detail, null);
        TextView tv_data = (TextView) convertView.findViewById(R.id.tv_data);
        TextView tv_addresss = (TextView) convertView.findViewById(R.id.tv_addresss);
        TextView tv_price = (TextView) convertView.findViewById(R.id.tv_price);

        final ExpendListDetailBean.DataBean bean = mDatas.get(position);

        if (null != bean) {
            tv_data.setText(TimeHelper.date2str(TimeHelper.JAVA_DATE_FORAMTER_4, new Date(bean.getCreatedTm())));
            if (!TextUtils.isEmpty(bean.getDispatchCity()) && !TextUtils.isEmpty(bean.getConsigneeCity())) {
                tv_addresss.setText(bean.getConsigneeCity() + " - " + bean.getDispatchCity());
            }
            tv_price.setText("-" + bean.getMoney());
        }

        return convertView;
    }

    public void setData(ExpendListDetailBean datas) {
        mDatas = datas.getData();
        notifyDataSetChanged();
    }

}
