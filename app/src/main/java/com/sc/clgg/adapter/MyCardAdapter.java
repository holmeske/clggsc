package com.sc.clgg.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mcxtzhang.swipemenulib.SwipeMenuLayout;
import com.sc.clgg.R;
import com.sc.clgg.activity.etc.PreRechargeActivity;
import com.sc.clgg.adapter.MyCardAdapter.MyHolder;
import com.sc.clgg.bean.CardList;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author：lvke
 * @date：2018/10/16 16:56
 */
public class MyCardAdapter extends RecyclerView.Adapter<MyHolder> {
    private List<CardList.Card> data = new ArrayList<>();
    private Context mContext;
    private boolean click;
    private onSwipeListener mOnSwipeListener;

    public void notifyItemRangeChanged(List<CardList.Card> list) {
        data.addAll(list);
        notifyItemRangeChanged(data.size(), list.size());
    }

    public void setItemEnable(boolean click) {
        this.click = click;
    }

    public void remove(int position) {
        data.remove(position);//删除数据源,移除集合中当前下标的数据
        notifyItemRemoved(position);//刷新被删除的地方
        notifyItemRangeChanged(position, getItemCount()); //刷新被删除数据，以及其后面的数据
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

        holder.tv_card.setText(card.getCardNo());
        holder.tv_carno.setText(card.getCarNo());

        holder.cl_content.setOnClickListener(v -> {
            if (click) {
                mContext.startActivity(new Intent(mContext, PreRechargeActivity.class).putExtra("card", card));
            }
        });
        if (!click) {
            holder.mSwipeMenuLayout.setIos(false).setLeftSwipe(true);
        } else {
            holder.mSwipeMenuLayout.setSwipeEnable(false);
            holder.cl_content.setOnClickListener(v -> {
                mContext.startActivity(new Intent(mContext, PreRechargeActivity.class).putExtra("card", card));
            });
        }

        holder.btnDelete.setOnClickListener(v -> {
            if (null != mOnSwipeListener) {
                //如果删除时，不使用mAdapter.notifyItemRemoved(pos)，则删除没有动画效果，
                //且如果想让侧滑菜单同时关闭，需要同时调用 ((CstSwipeDelMenu) holder.itemView).quickClose();
                //((CstSwipeDelMenu) holder.itemView).quickClose();
                mOnSwipeListener.onDel(card, holder.getAdapterPosition());
                holder.mSwipeMenuLayout.quickClose();
            }
        });
    }

    public void setOnDelListener(onSwipeListener mOnDelListener) {
        this.mOnSwipeListener = mOnDelListener;
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    public interface onSwipeListener {
        void onDel(CardList.Card card, int pos);
    }

    static class MyHolder extends RecyclerView.ViewHolder {
        SwipeMenuLayout mSwipeMenuLayout;
        Button btnDelete;
        Button btnUnRead;
        Button btnTop;
        ConstraintLayout cl_content;
        private TextView tv_card_type, tv_card, tv_carno;

        public MyHolder(@NonNull View itemView) {
            super(itemView);
            cl_content = itemView.findViewById(R.id.content);
            tv_card_type = itemView.findViewById(R.id.tv_card_type);
            tv_card = itemView.findViewById(R.id.tv_card);
            tv_carno = itemView.findViewById(R.id.tv_carno);

            mSwipeMenuLayout = itemView.findViewById(R.id.swipeMenuLayout);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnUnRead = itemView.findViewById(R.id.btnUnRead);
            btnTop = itemView.findViewById(R.id.btnTop);
        }
    }
}
