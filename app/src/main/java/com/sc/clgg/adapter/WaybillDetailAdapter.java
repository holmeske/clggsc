package com.sc.clgg.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.bean.PayDetailBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Author：lvke
 * CreateDate：2017/9/25 20:12
 * 运单明细适配器
 */

public class WaybillDetailAdapter extends RecyclerView.Adapter<WaybillDetailAdapter.MyViewHolder> {
    private List<PayDetailBean.DataBean.CostListBean> dataList = new ArrayList<>();
    private Context mContext;

    public WaybillDetailAdapter() {
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_waybill_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        PayDetailBean.DataBean.CostListBean bean = dataList.get(position);
        holder.tv_name.setText(bean.getCostText());
        if (position == 0) {
            holder.tv_money.setText("+" + bean.getMoney());
            holder.tv_money.setTextColor(ContextCompat.getColor(mContext, R.color.green));
            holder.tv_name.setTextColor(ContextCompat.getColor(mContext, R.color.green));
        } else {
            holder.tv_money.setText("-" + bean.getMoney());
            holder.tv_money.setTextColor(ContextCompat.getColor(mContext, R.color.black_333));
            holder.tv_name.setTextColor(ContextCompat.getColor(mContext, R.color.black_333));
        }

    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public void refresh(List<PayDetailBean.DataBean.CostListBean> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    class MyViewHolder extends ViewHolder {
        private TextView tv_name, tv_money;

        private MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv_name);
            tv_money = (TextView) itemView.findViewById(R.id.tv_money);

        }
    }
}
