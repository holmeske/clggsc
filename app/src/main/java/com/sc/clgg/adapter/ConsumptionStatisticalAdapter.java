package com.sc.clgg.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.bean.Consumption;
import com.sc.clgg.tool.helper.DecimalFormatHelper;
import com.sc.clgg.tool.helper.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：lvke
 * @date：2018/5/23 15:35
 */
public class ConsumptionStatisticalAdapter extends RecyclerView.Adapter<ConsumptionStatisticalAdapter.MyHolder> {
    private List<Consumption.Data.A> data = new ArrayList<>();
    private ItemClickListener mItemClickListener;

    public ConsumptionStatisticalAdapter(ItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public void refresh(List<Consumption.Data.A> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void reverse() {
        List<Consumption.Data.A> reverseList = new ArrayList<>();
        for (int i = data.size() - 1; i >= 0; i--) {
            reverseList.add(data.get(i));
        }
        this.data = reverseList;

        LogHelper.e("倒序 size = " + this.data.size());

        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_consumption_statistical, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Consumption.Data.A bean = data.get(holder.getAdapterPosition());

        holder.tv_carno.setText(bean.getCarNo() == null ? "" : bean.getCarNo());

        holder.tv_total_consumption.setText(bean.getTotalFuel() == null ? "" : DecimalFormatHelper.formatTwo(bean.getTotalFuel()));

        holder.itemView.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(bean.getHundredFuel()) && Double.parseDouble(bean.getHundredFuel()) > 0) {
                if (mItemClickListener != null) {
                    mItemClickListener.click(bean.getVin(), bean.getCarNo());
                }
            }
        });

        holder.tv_mileage.setText(bean.getHundredFuel() == null ? "" : DecimalFormatHelper.formatTwo(bean.getHundredFuel()));
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public interface ItemClickListener {
        void click(String vin, String carno);
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
