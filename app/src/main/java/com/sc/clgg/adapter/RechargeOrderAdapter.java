package com.sc.clgg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.bean.RechargeOrderList;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class RechargeOrderAdapter extends RecyclerView.Adapter<RechargeOrderAdapter.MyHolder> {
    private List<RechargeOrderList.Order> dataList = new ArrayList<>();
    private Context mContext;

    public void clear() {
        dataList.clear();
        notifyDataSetChanged();
    }

    public void notifyItemInserted(List<RechargeOrderList.Order> list) {
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
            notifyItemRangeChanged(dataList.size(), list.size());
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_recharge_order, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        RechargeOrderList.Order order = dataList.get(holder.getAdapterPosition());

        holder.tv_recharge_number.setText(order.getWasteSn());
        if (order.isLoad().equals("0")) {
            holder.tv_recharge_state.setText("待圈存");
            holder.tv_recharge_state.setBackgroundResource(R.drawable.bg_fff3e5_15);
            holder.tv_recharge_state.setTextColor(ContextCompat.getColor(mContext, R.color._ee8031));
        } else if (order.isLoad().equals("1")) {
            holder.tv_recharge_state.setText("已完成");
            holder.tv_recharge_state.setBackgroundResource(R.drawable.bg_6ccd37_15);
            holder.tv_recharge_state.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        } else {
            holder.tv_recharge_state.setText("已冲正");
            holder.tv_recharge_state.setBackgroundResource(R.drawable.bg_fa6565_15);
            holder.tv_recharge_state.setTextColor(ContextCompat.getColor(mContext, R.color.white));
        }

        holder.tv_card_number.setText(order.getCardNo());
        holder.tv_recharge_amount.setText("￥" + order.getPayMoney() / 100f);
        holder.tv_pay_time.setText(order.getPayTime());
        holder.tv_pay_into.setText(order.getPayFlag().equals("1") ? "已到账" : "确认中");
        holder.tv_pay_way.setText(order.getPayType().equals("18") ? "微信支付" : "");
        holder.tv_write_time.setText(order.getCreateTime());
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_recharge_number, tv_recharge_state, tv_card_number, tv_recharge_amount, tv_pay_time, tv_pay_into, tv_pay_way, tv_write_time;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_recharge_number = itemView.findViewById(R.id.tv_recharge_number);
            tv_recharge_state = itemView.findViewById(R.id.tv_recharge_state);
            tv_card_number = itemView.findViewById(R.id.tv_card_number);
            tv_recharge_amount = itemView.findViewById(R.id.tv_recharge_amount);
            tv_pay_time = itemView.findViewById(R.id.tv_pay_time);
            tv_pay_into = itemView.findViewById(R.id.tv_pay_into);
            tv_pay_way = itemView.findViewById(R.id.tv_pay_way);
            tv_write_time = itemView.findViewById(R.id.tv_write_time);
        }
    }

}
