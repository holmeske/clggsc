package com.sc.clgg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sc.clgg.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import tool.widget.ShapeTextView;

/**
 * @author：lvke
 * @date：2017/11/1 15:36
 */

public class DrivingRemindAdapter extends RecyclerView.Adapter<DrivingRemindAdapter.Holder> {
    private Context mContext;
    private List<Object> dataList = new ArrayList<>();

    public void refresh(List<Object> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @Override
    public DrivingRemindAdapter.Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();

        return new DrivingRemindAdapter.Holder(LayoutInflater.from(mContext).inflate(R.layout.item_driving_remind, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        @BindView(R.id.shapeTextView) ShapeTextView mShapeTextView;
        @BindView(R.id.tv_remind) TextView mTvRemind;
        @BindView(R.id.tv_vehicle_number) TextView mTvVehicleNumber;
        @BindView(R.id.tv_address) TextView mTvAddress;
        @BindView(R.id.tv_date) TextView mTvDate;

        public Holder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

