package com.sc.clgg.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.bean.Vehicle;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author：lvke
 * @date：2018/5/17 17:45
 */
public class FaultSelectVehicleAdapter extends RecyclerView.Adapter<FaultSelectVehicleAdapter.CustomHolder> {
    private List<Vehicle.Bean> mDataList = new ArrayList<>();
    private List<Vehicle.Bean> mSelectedList = new ArrayList<>();
    private SelectAllListener mSelectAllListener;

    public void clearSelected() {
        mSelectedList.clear();
    }

    public void allUnSelected(List<Vehicle.Bean> dataList) {
        for (Vehicle.Bean b : dataList) {
            b.setChecked(false);
        }
        refresh(dataList);
    }

    public void allSelected(List<Vehicle.Bean> dataList) {
        for (Vehicle.Bean b : dataList) {
            b.setChecked(true);
        }
        refresh(dataList);
    }

    public void refresh(List<Vehicle.Bean> dataList) {
        if (dataList==null){
            return;
        }
        clearSelected();
        mDataList = dataList;
        for (Vehicle.Bean b : dataList) {
            if (b.isChecked()) {
                mSelectedList.add(b);
            }else {
                mSelectedList.remove(b);
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CustomHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_vehicle, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CustomHolder holder, int position) {
        Vehicle.Bean bean = mDataList.get(holder.getAdapterPosition());

        holder.mImageView.setVisibility(View.VISIBLE);
        if (bean.isChecked()) {
            holder.mImageView.setImageResource(R.drawable.checkbox_chosen);
        } else {
            holder.mImageView.setImageResource(R.drawable.checkbox_vain);
        }

        holder.mTextView.setText(bean.getCarNumber());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bean.setChecked(!bean.isChecked());
                if (bean.isChecked()) {
                    mSelectedList.add(bean);
                    holder.mImageView.setImageResource(R.drawable.checkbox_chosen);
                } else {
                    mSelectedList.remove(bean);
                    if (mSelectAllListener != null) {
                        mSelectAllListener.noSelectAll();
                    }
                    holder.mImageView.setImageResource(R.drawable.checkbox_vain);
                }
            }
        });
    }

    public List<Vehicle.Bean> getSelectedList() {
        return mSelectedList;
    }

    public void setSelectedList(List<Vehicle.Bean> selectedList) {
        mSelectedList = selectedList;
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    public void setSelectAllListener(SelectAllListener selectAllListener) {
        mSelectAllListener = selectAllListener;
    }

    public interface SelectAllListener {
        void noSelectAll();
    }

    class CustomHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;
        private ImageView mImageView;

        public CustomHolder(View itemView) {
            super(itemView);

            mTextView = itemView.findViewById(R.id.vehicle_no);
            mImageView = itemView.findViewById(R.id.iv_check);
        }
    }
}
