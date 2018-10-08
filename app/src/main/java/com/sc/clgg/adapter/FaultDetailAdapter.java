package com.sc.clgg.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.bean.Fault;
import com.sc.clgg.bean.FaultDetail;
import com.sc.clgg.tool.helper.LogHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lvke
 */
public class FaultDetailAdapter extends RecyclerView.Adapter<FaultDetailAdapter.MyHolder> {
    private List<FaultDetail> mDataList = new ArrayList<>();
    private Context mContext;
    private int lastIndex = -1;

    public void refresh(List<FaultDetail> dataList) {
        this.mDataList = dataList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_fault_detail, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position, @NonNull List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
        if (payloads.isEmpty()) {
            // payloads 为 空，说明是更新整个 ViewHolder
            onBindViewHolder(holder, holder.getAdapterPosition());
            LogHelper.e("position = " + holder.getAdapterPosition());
        } else {
            // payloads 不为空，这只更新需要更新的 View 即可。
            LogHelper.e("局部刷新");
            if ((int) payloads.get(0) == 1) {

                holder.container.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        if (lastIndex != holder.getAdapterPosition()) {
            holder.container.setVisibility(View.GONE);
        } else {
            holder.container.setVisibility(View.VISIBLE);
        }

        FaultDetail bean = mDataList.get(holder.getAdapterPosition());

        holder.tv_fault_number.setText(bean.getData() == null ? "0" : bean.getData().size() + "");
        holder.tv_carno.setText(bean.getCarno());

        if (bean.getData().size() > 0) {
            setFaultDetail(bean.getData(), holder.container);
            holder.tv_operation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LogHelper.e("lastIndex = " + lastIndex);
                    if (lastIndex != -1 && lastIndex != holder.getAdapterPosition()) {
                        notifyItemChanged(lastIndex, 1);
                    }

                    if (holder.container.getVisibility() == View.GONE) {
                        holder.container.setVisibility(View.VISIBLE);
                        lastIndex = holder.getAdapterPosition();
                        LogHelper.e("lastIndex = " + lastIndex);
                    } else if (holder.container.getVisibility() == View.VISIBLE) {
                        holder.container.setVisibility(View.GONE);
                        lastIndex = -1;
                        LogHelper.e("lastIndex = " + lastIndex);
                    }

                }
            });
        }
    }

    private void setFaultDetail(List<Fault.Data> list, LinearLayout container) {
        container.removeAllViews();
        container.addView(View.inflate(mContext, R.layout.view_fault_describe, null));
        if (list != null) {
            for (Fault.Data data : list) {
                View view = View.inflate(mContext, R.layout.view_fault_describe, null);
                TextView tv_no = view.findViewById(R.id.tv_no);
                TextView tv_des = view.findViewById(R.id.tv_des);
                TextView tv_plan = view.findViewById(R.id.tv_plan);

                tv_no.setText(list.indexOf(data) + 1 + "");
                tv_des.setText(data.getDesc());
                tv_plan.setText(data.getSolution());
                container.addView(view);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        TextView tv_fault_number, tv_carno, tv_operation;
        LinearLayout container;

        public MyHolder(View itemView) {
            super(itemView);
            tv_fault_number = itemView.findViewById(R.id.tv_fault_number);
            tv_carno = itemView.findViewById(R.id.tv_carno);
            tv_operation = itemView.findViewById(R.id.tv_operation);
            container = itemView.findViewById(R.id.ll);

        }
    }

}
