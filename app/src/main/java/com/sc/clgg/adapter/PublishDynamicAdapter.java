package com.sc.clgg.adapter;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.sc.clgg.R;
import com.sc.clgg.activity.contact.CallbackListener;
import com.sc.clgg.tool.helper.LogHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author：lvke
 * @date：2018/6/22 10:54
 */
public class PublishDynamicAdapter extends RecyclerView.Adapter<PublishDynamicAdapter.MyHolder> {

    private Context mContext;
    public List<String> pathList = new ArrayList<>();
    private CallbackListener mCallbackListener;



    public List<String> getPublishImageList() {
        pathList.remove("");
        return pathList;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_publish_dynamic, parent, false));
    }

    public void refresh(List<String> pathList) {
        this.pathList = pathList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        if (pathList.get(holder.getAdapterPosition()).equals("")) {
            holder.iv_pic.setImageResource(R.drawable.pic_addpic);
            holder.iv_close.setVisibility(View.GONE);
            holder.iv_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mCallbackListener != null) {
                        mCallbackListener.callback(holder.getAdapterPosition());
                    }
                }
            });
        } else {
            holder.iv_close.setVisibility(View.VISIBLE);
            holder.iv_pic.setImageBitmap(BitmapFactory.decodeFile(pathList.get(holder.getAdapterPosition())));
            holder.iv_close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pathList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), pathList.size() - holder.getAdapterPosition());

                    if (pathList.size() < 9 && !pathList.get(pathList.size() - 1).equals("")) {
                        pathList.add("");
                        notifyDataSetChanged();
                    }

                }
            });
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LogHelper.e(" 路径 = " + pathList.get(holder.getAdapterPosition()));
            }
        });
    }


    @Override
    public int getItemCount() {
        return pathList == null ? 0 : pathList.size();
    }

    public void setCallbackListener(CallbackListener callbackListener) {
        mCallbackListener = callbackListener;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        ImageView iv_pic, iv_close;

        public MyHolder(View itemView) {
            super(itemView);
            iv_pic = itemView.findViewById(R.id.iv_pic);
            iv_close = itemView.findViewById(R.id.iv_close);
        }
    }
}
