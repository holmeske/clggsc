package com.sc.clgg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.bean.TrafficDetail;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author：lvke
 * @date：2018/11/20 14:31
 */
public class TrafficDetailAdapter extends RecyclerView.Adapter<TrafficDetailAdapter.MyHolder> {
    private Context mContext;
    private List<TrafficDetail> dataList = new ArrayList<>();

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_traffic_detail, parent, false));
    }

    public void clear() {
        dataList.clear();
        notifyDataSetChanged();
    }

    public void notifyItemInserted(List<TrafficDetail> list) {
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
            notifyItemRangeChanged(dataList.size(), list.size());
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_consume_time, tv_consume_amount, tv_card_balance, tv_traffic_interval,
                tv_entrance_traffic_time, tv_export_traffic_time, tv_settlement_province;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_consume_time = itemView.findViewById(R.id.tv_consume_time);
            tv_consume_amount = itemView.findViewById(R.id.tv_consume_amount);
            tv_card_balance = itemView.findViewById(R.id.tv_card_balance);
            tv_traffic_interval = itemView.findViewById(R.id.tv_traffic_interval);
            tv_entrance_traffic_time = itemView.findViewById(R.id.tv_entrance_traffic_time);
            tv_export_traffic_time = itemView.findViewById(R.id.tv_export_traffic_time);
            tv_settlement_province = itemView.findViewById(R.id.tv_settlement_province);
        }
    }
}
