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
import com.sc.clgg.bean.ItemOrderBean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import tool.helper.LogHelper;

/**
 * Author：lvke
 * CreateDate：2017/9/25 20:12
 */

public class SpendingSourcesAdapter extends RecyclerView.Adapter<SpendingSourcesAdapter.MyViewHolder> {
    private List<ItemOrderBean> dataList = new ArrayList<>();
    private ItemOrderBean bean;
    private int previousItemPosition = -1;

    private StringContact mStringContact;

    public SpendingSourcesAdapter() {
    }

    public StringContact getStringContact() {
        return mStringContact;
    }

    public void setStringContact(StringContact stringContact) {
        mStringContact = stringContact;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_spending_sources, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        bean = dataList.get(position);
        holder.tvRoute.setText(bean.getDispatchCity() + holder.itemView.getContext().getString(R.string.minus) + bean.getConsigneeCity());
        holder.tvDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date(bean.getCreatedAt())));
        holder.tvNumber.setText(bean.getWaybillNo());

        if (bean.isChecked()) {
            holder.ivSelected.setVisibility(View.VISIBLE);
        } else {
            holder.ivSelected.setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (previousItemPosition == position) {
                    return;
                }
                if (previousItemPosition != -1 && previousItemPosition != position) {
                    ItemOrderBean bean = dataList.get(previousItemPosition);
                    bean.setChecked(false);
                    notifyItemChanged(previousItemPosition);
                }
                ItemOrderBean bean = dataList.get(position);
                if (bean.isChecked()) {
                    holder.ivSelected.setVisibility(View.INVISIBLE);
                    bean.setChecked(false);
                    previousItemPosition = -1;
                } else {
                    holder.ivSelected.setVisibility(View.VISIBLE);
                    bean.setChecked(true);
                    previousItemPosition = position;
                    if (mStringContact != null) {
                        mStringContact.callback(bean.getWaybillNo());
                    }
                }
                LogHelper.e("上一个是：" + previousItemPosition + "当前是：" + position);
                LogHelper.e("" + bean.getWaybillNo());
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public void refresh(List<ItemOrderBean> dataList) {
        this.dataList = dataList;
        notifyDataSetChanged();
    }

    class MyViewHolder extends ViewHolder {
        private TextView tvRoute, tvDate, tvNumber;
        private ImageView ivSelected;

        private MyViewHolder(View itemView) {
            super(itemView);
            tvRoute = (TextView) itemView.findViewById(R.id.tv_route);
            tvDate = (TextView) itemView.findViewById(R.id.tv_date);
            tvNumber = (TextView) itemView.findViewById(R.id.tv_no);
            ivSelected = (ImageView) itemView.findViewById(R.id.iv_selected);
        }
    }


}
