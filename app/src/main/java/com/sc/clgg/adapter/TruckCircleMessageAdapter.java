package com.sc.clgg.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sc.clgg.R;
import com.sc.clgg.activity.InteractiveDetailActivity;
import com.sc.clgg.bean.NoReadInfo;
import com.sc.clgg.util.PotatoKt;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：lvke
 * @date：2018/6/25 10:46
 */
public class TruckCircleMessageAdapter extends RecyclerView.Adapter<TruckCircleMessageAdapter.MyHolder> {
    private List<NoReadInfo.Info> dataList = new ArrayList<>();
    private Context mContext;

    public void refresh(List<NoReadInfo.Info> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_truck_circle_message, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        NoReadInfo.Info bean = dataList.get(holder.getAdapterPosition());

        PotatoKt.setRoundedCornerPicture(holder.iv_head, mContext, bean.getHeadImg());

        if (bean.getRemark() != null) {
            if (bean.getRemark().equals("点赞")) {
                holder.iv_des.setVisibility(View.VISIBLE);
                holder.tv_des.setVisibility(View.INVISIBLE);
            } else {
                holder.iv_des.setVisibility(View.INVISIBLE);
                holder.tv_des.setVisibility(View.VISIBLE);
                holder.tv_des.setText(bean.getRemark());
            }
        }
        holder.tv_name.setText(bean.getNickName() == null ? "" : bean.getNickName());
        holder.tv_time.setText(bean.getCreateTime() == null ? "" : bean.getCreateTime());

        if (TextUtils.isEmpty(bean.getMessage())) {
            holder.tv_right.setVisibility(View.INVISIBLE);
            holder.iv_right.setVisibility(View.VISIBLE);

            Glide.with(mContext).load(bean.getImagesList().get(0).getImgUrl()).apply(new RequestOptions().placeholder(R.drawable.ic_launcher).error(R.drawable.ic_launcher)).into(holder.iv_right);
        } else {
            holder.tv_right.setVisibility(View.VISIBLE);
            holder.iv_right.setVisibility(View.INVISIBLE);
            holder.tv_right.setText(bean.getMessage());
        }

        holder.itemView.setOnClickListener(v -> {
            mContext.startActivity(new Intent(mContext, InteractiveDetailActivity.class).putExtra("messageId", bean.getId()));
        });
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private ImageView iv_head;
        private TextView tv_name, tv_des, tv_time, tv_right;
        private ImageView iv_des, iv_right;

        public MyHolder(View itemView) {
            super(itemView);
            iv_head = itemView.findViewById(R.id.iv_head);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_des = itemView.findViewById(R.id.tv_des);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_right = itemView.findViewById(R.id.tv_right);
            iv_des = itemView.findViewById(R.id.iv_des);
            iv_right = itemView.findViewById(R.id.iv_right);
        }
    }
}
