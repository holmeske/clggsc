package com.sc.clgg.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.activity.etc.PreRechargeActivity;
import com.sc.clgg.adapter.MyCardAdapter.MyHolder;
import com.sc.clgg.bean.CardList;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author：lvke
 * @date：2018/10/16 16:56
 */
public class MyCardAdapter extends RecyclerView.Adapter<MyHolder> {
    private List<CardList.Card> data = new ArrayList<>();
    private Context mContext;
    private boolean click;

    public void notifyItemRangeChanged(List<CardList.Card> list) {
        data.addAll(list);
        notifyItemRangeChanged(data.size(), list.size());
    }

    public void setItemEnable(boolean click) {
        this.click = click;
    }

    public void refresh(List<CardList.Card> list) {
        if (list != null) {
            this.data = list;
            notifyDataSetChanged();
        }
    }

    public void clean() {
        data.clear();
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
        CardList.Card card = data.get(holder.getAdapterPosition());
        if ("3".equals(card.getCardType())) {
            holder.tv_card_type.setText("鲁通B卡");
        } else {
            holder.tv_card_type.setText("");
        }

        holder.tv_card.setText(card.getCardId());
        holder.tv_carno.setText(card.getVlp());

        holder.itemView.setOnClickListener(v -> {
            if (click) {
                mContext.startActivity(new Intent(mContext, PreRechargeActivity.class).putExtra("card", card));
            }
        });
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
