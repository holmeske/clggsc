package com.sc.clgg.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.activity.vehiclemanager.tallybook.ExpendListDetailActivity;
import com.sc.clgg.base.BaseAdNet;
import com.sc.clgg.bean.ExpendListBean;

import java.text.DecimalFormat;

/**
 * 选择车辆 适配器
 *
 * @author ZhangYi 2014-12-4 14:08:39
 */
public class SpendingAdapter extends BaseAdNet<ExpendListBean.DataBean.ListBean> {

    private Context mContext;
    private double max = 0;
    private DecimalFormat df = new DecimalFormat("0.000");
    private int year, month;

    public SpendingAdapter(Activity activity, ExpendListBean data) {
        super(activity);
        this.mContext = activity;

        max = data.getData().getCountPrice();
        if (data.getData().getList() != null && data.getData().getList().size() > 0) {
            mDatas = data.getData().getList();
        }
    }

    @Override
    protected View setConvertView(View convertView, int position) {
        convertView = mInflater.inflate(R.layout.item_expenditure_list, null);
        TextView tv_weight = (TextView) convertView.findViewById(R.id.tv_weight);
        TextView tv_price = (TextView) convertView.findViewById(R.id.tv_price);
        TextView tv_typeName = (TextView) convertView.findViewById(R.id.tv_typeName);

        ProgressBar color_progressBar = (ProgressBar) convertView.findViewById(R.id.color_progressBar);
        color_progressBar.setMax((int) Math.ceil(max));

        final ExpendListBean.DataBean.ListBean bean = mDatas.get(position);

        if (null != bean) {
            tv_price.setText("-" + new DecimalFormat("0.00").format(bean.getMoney()));

            if (!TextUtils.isEmpty(bean.getCostText())) {
                tv_typeName.setText(bean.getCostText());
            }

            tv_weight.setText(df.format(((bean.getMoney() / max)) * 100) + "%");

            if (bean.getMoney() > 99 && bean.getMoney() < 100) {
                color_progressBar.setProgress(99);
            } else {
                int value = (int) Math.ceil(bean.getMoney());
                color_progressBar.setProgress(value);
            }
        }

        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ExpendListDetailActivity.class);
                intent.putExtra("bean", bean);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                mContext.startActivity(intent);
            }
        });
        return convertView;
    }

    public void setData(ExpendListBean data, int year, int month) {
        mDatas = data.getData().getList();
        this.year = year;
        this.month = month;
        notifyDataSetChanged();
    }

}
