package com.sc.clgg.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.bean.ConsumptionDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：lvke
 * @date：2018/5/23 15:35
 */
public class ConsumptionDetailAdapter extends RecyclerView.Adapter<ConsumptionDetailAdapter.MyHolder> {
    private List<ConsumptionDetail.Data.B> data = new ArrayList<>();

    public ConsumptionDetailAdapter() {
    }

    public void refresh(List<ConsumptionDetail.Data.B> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consumption_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        ConsumptionDetail.Data.B bean = data.get(holder.getAdapterPosition());

        if (!TextUtils.isEmpty(bean.getClctDate()) && bean.getClctDate().length() == 8) {
            holder.tv_carno.setText(Integer.parseInt(bean.getClctDate().substring(4, 6)) + "月" + Integer.parseInt(bean.getClctDate().substring(6, 8)) + "日");
        }else {
            holder.tv_carno.setText("");
        }

        holder.tv_total_consumption.setText(bean.getTotalFuel() == null ? "" : bean.getTotalFuel());

        holder.tv_mileage.setText(bean.getHundredFuel() == null ? "" : bean.getHundredFuel());

    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }


    class MyHolder extends RecyclerView.ViewHolder {

        public TextView tv_carno, tv_total_consumption, tv_mileage;

        public MyHolder(View itemView) {
            super(itemView);
            tv_carno = itemView.findViewById(R.id.tv_carno);
            tv_total_consumption = itemView.findViewById(R.id.tv_total_consumption);
            tv_mileage = itemView.findViewById(R.id.tv_mileage);
        }
    }
}
