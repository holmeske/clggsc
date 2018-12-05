package com.sc.clgg.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.bean.Record;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author lvke
 */
public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.MyHolder> {
    private List<Record> data;
    private OnItemClickListener mItemClickListener;

    public RecordAdapter(List<Record> data) {
        this.data = data;
    }

    public void refresh(List<Record> data) {
        this.data = data;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Record record = data.get(holder.getAdapterPosition());
        holder.mTextView.setText(record.getName());

        if (record.isChecked()) {
            holder.ivType.setImageResource(record.getPressImg());
        } else {
            holder.ivType.setImageResource(record.getNomalImg());
        }

        holder.itemView.setOnClickListener(v -> {
            holder.ivType.setImageResource(record.getPressImg());
            if (mItemClickListener != null) {
                mItemClickListener.click(record.getTypeId() + "", record.getName(), holder.getAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    public interface OnItemClickListener {
        void click(String s1, String s2, int i);
    }

    class MyHolder extends RecyclerView.ViewHolder {

        private TextView mTextView;
        private ImageView ivType;

        public MyHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_type);
            ivType = itemView.findViewById(R.id.iv_type);
        }

    }
}
