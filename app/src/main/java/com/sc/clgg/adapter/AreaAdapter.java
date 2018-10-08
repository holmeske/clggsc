package com.sc.clgg.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.activity.contact.ItemClickListener;

import java.util.List;

/**
 * @author：lvke
 * @date：2018/5/11 11:51
 */
public class AreaAdapter extends RecyclerView.Adapter<AreaAdapter.MyHolder> {
    private final List<String> data;
    private ItemClickListener mItemClickListener;

    public AreaAdapter(List<String> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_area, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.mTextView.setText(data.get(holder.getAdapterPosition()));
        holder.itemView.setOnClickListener(v -> {
            if (mItemClickListener != null) {
                mItemClickListener.click(data.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.mItemClickListener = itemClickListener;
    }

    class MyHolder extends RecyclerView.ViewHolder {

        private final TextView mTextView;

        private MyHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_area);
        }

    }
}
