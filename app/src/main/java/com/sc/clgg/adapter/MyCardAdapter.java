package com.sc.clgg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.adapter.MyCardAdapter.MyHolder;
import com.sc.clgg.bean.Card;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author：lvke
 * @date：2018/10/16 16:56
 */
public class MyCardAdapter extends RecyclerView.Adapter<MyHolder> {
    List<Card> data = new ArrayList<>();
    private Context mContext;

    public void refresh(List<Card> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_my_card, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.tv_card_type.setText(data.get(position).getType());
        holder.tv_card.setText(data.get(position).getNo());
        holder.tv_carno.setText(data.get(position).getNumber());
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_card_type, tv_card, tv_carno;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_card_type = itemView.findViewById(R.id.tv_card_type);
            tv_card = itemView.findViewById(R.id.tv_card);
            tv_carno = itemView.findViewById(R.id.tv_carno);
        }
    }
}
