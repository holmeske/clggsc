package com.sc.clgg.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.activity.vehiclemanager.myvehicle.PathRecordActivity;
import com.sc.clgg.bean.MileageDetail;
import com.sc.clgg.util.TimeHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * @author：lvke
 * @date：2018/5/23 15:35
 */
public class StatisticalDetailAdapter extends RecyclerView.Adapter<StatisticalDetailAdapter.MyHolder> {
    private List<MileageDetail.Data.Detail> data = new ArrayList<>();
    private int year, month;
    private Context mContext;
    private String carno, vin;

    public StatisticalDetailAdapter(int year, int month, String carno, String vin) {
        this.year = year;
        this.month = month;
        this.carno = carno;
        this.vin = vin;
    }

    public void refresh(List<MileageDetail.Data.Detail> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_statistical_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        MileageDetail.Data.Detail bean = data.get(holder.getAdapterPosition());
        if (!TextUtils.isEmpty(bean.getDate())) {
            holder.tv_date.setText(month + "月" + bean.getDate() + "日");
        }

        if (!TextUtils.isEmpty(bean.getDayMileage()) && Double.parseDouble(bean.getDayMileage()) > 0) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Calendar calendar = Calendar.getInstance(Locale.CHINA);
                    calendar.set(year, month - 1, Integer.parseInt(bean.getDate()));

                    mContext.startActivity(new Intent(mContext, PathRecordActivity.class)
                            .putExtra("carno", carno)
                            .putExtra("vin", vin)
                            .putExtra("startDate", TimeHelper.long2time(TimeHelper.JAVA_DATE_FORAMTER_2, calendar.getTimeInMillis()) + "000000")
                            .putExtra("endDate", TimeHelper.long2time(TimeHelper.JAVA_DATE_FORAMTER_2, calendar.getTimeInMillis()) + "235959"));
                }
            });
        }
        holder.tv_mileage.setText(bean.getDayMileage() == null ? "" : bean.getDayMileage() + "km");
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        public TextView tv_date, tv_mileage;

        public MyHolder(View itemView) {
            super(itemView);
            tv_date = itemView.findViewById(R.id.tv_date);
            tv_mileage = itemView.findViewById(R.id.tv_mileage);
        }
    }

}
