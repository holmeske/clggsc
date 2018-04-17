package com.sc.clgg.adapter;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.activity.contact.StringContact;
import com.sc.clgg.bean.TypeBean;

import java.util.ArrayList;
import java.util.List;

import tool.helper.LogHelper;

/**
 * Author：lvke
 * CreateDate：2017/9/25 20:12
 */

public class TextViewAdapter extends RecyclerView.Adapter<TextViewAdapter.MyViewHolder> {
    private List<TypeBean> dataList = new ArrayList<>();
    private TypeBean bean;
    private int previousItemPosition = -1;
    private StringContact mStringContact;

    public TextViewAdapter(List<TypeBean> dataList) {
        this.dataList = dataList;
    }

    public StringContact getStringContact() {
        return mStringContact;
    }

    public void setStringContact(StringContact stringContact) {
        mStringContact = stringContact;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_textview, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        bean = dataList.get(position);
        holder.tv_name.setText(bean.getName());
        holder.ivIcon.setImageResource(bean.getUnCheckedResId());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previousItemPosition == position) {
                    return;
                }
                if (previousItemPosition != -1 && previousItemPosition != position) {
                    TypeBean bean = dataList.get(previousItemPosition);
                    bean.setChecked(false);
                    notifyItemChanged(previousItemPosition);
                }
                TypeBean bean = dataList.get(position);
                if (bean.isChecked()) {
                    holder.ivIcon.setImageResource(bean.getUnCheckedResId());
                    bean.setChecked(false);
                    previousItemPosition = -1;
                } else {
                    holder.ivIcon.setImageResource(bean.getCheckedResId());
                    bean.setChecked(true);
                    previousItemPosition = position;
                    if (mStringContact != null) {
                        mStringContact.callback(bean.getId());
                    }
                }
                LogHelper.e("上一个是：" + previousItemPosition + "当前是：" + position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public void refresh(List<TypeBean> dataList) {
        previousItemPosition = -1;
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    class MyViewHolder extends ViewHolder {
        private TextView tv_name;
        private ImageView ivIcon;

        private MyViewHolder(View itemView) {
            super(itemView);
            tv_name = (TextView) itemView.findViewById(R.id.tv);
            ivIcon = (ImageView) itemView.findViewById(R.id.iv);
        }
    }
}
