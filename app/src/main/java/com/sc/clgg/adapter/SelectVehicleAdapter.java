package com.sc.clgg.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.bean.Location;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author：lvke
 * @date：2018/5/17 17:45
 */
public class SelectVehicleAdapter extends RecyclerView.Adapter<SelectVehicleAdapter.CustomHolder> {
    private List<Location.Data> mDataList = new ArrayList<>();
    private SelectVehicleListener mSelectVehicleListener;

    public SelectVehicleAdapter(List<Location.Data> dataList, SelectVehicleListener listener) {
        mDataList = dataList;
        this.mSelectVehicleListener = listener;
    }

    public void refresh(List<Location.Data> dataList){
        mDataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_vehicle, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomHolder holder, int position) {
        holder.mTextView.setText(mDataList.get(holder.getAdapterPosition()).getCarno());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectVehicleListener != null) {
                    mSelectVehicleListener.select(mDataList.get(holder.getAdapterPosition()).getVin(),
                            mDataList.get(holder.getAdapterPosition()).getCarno());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public void setSelectVehicleListener(SelectVehicleListener selectVehicleListener) {
        mSelectVehicleListener = selectVehicleListener;
    }

    public interface SelectVehicleListener {
        void select(String vin,String carno);
    }

    class CustomHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;

        public CustomHolder(View itemView) {
            super(itemView);

            mTextView = itemView.findViewById(R.id.vehicle_no);
        }
    }
}
