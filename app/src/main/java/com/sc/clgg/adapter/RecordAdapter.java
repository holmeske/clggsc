package com.sc.clgg.adapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.bean.Record;

import java.util.List;

/**
 * @author：lvke
 * @date：2018/5/11 11:51
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
            holder.iv_type.setImageResource(record.getPressImg());
        } else {
            holder.iv_type.setImageResource(record.getNomalImg());
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.iv_type.setImageResource(record.getPressImg());
                if (mItemClickListener != null) {
                    mItemClickListener.click(record.getTypeId() + "", record.getName(), holder.getAdapterPosition());
                }
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
        private ImageView iv_type;

        public MyHolder(View itemView) {
            super(itemView);
            mTextView = itemView.findViewById(R.id.tv_type);
            iv_type = itemView.findViewById(R.id.iv_type);
        }

    }
}
