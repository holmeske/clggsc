package com.sc.clgg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.sc.clgg.R;
import com.sc.clgg.bean.ApplyStateListBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：lvke
 * @date：2018/11/16 16:39
 */
public class ApplyStateNewAdapter extends RecyclerView.Adapter<ApplyStateNewAdapter.MyHolder> {
    private List<ApplyStateListBean.A> dataList = new ArrayList<>();
    private Context mContext;

    public void clear() {
        dataList.clear();
        notifyDataSetChanged();
    }

    public void refresh(List<ApplyStateListBean.A> list) {
        dataList.clear();
        dataList.addAll(list);
        notifyDataSetChanged();
    }

    public void notifyItemInserted(List<ApplyStateListBean.A> list) {
        if (list != null && list.size() > 0) {
            dataList.addAll(list);
            notifyItemRangeChanged(dataList.size(), list.size());
        }
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_apply_state, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        ApplyStateListBean.A bean = dataList.get(holder.getAdapterPosition());
        holder.tv_apply_time.setText("申请时间:  " + bean.getCreateTime());
        holder.tv_card_type.setText("卡类型:  鲁通卡");
        holder.tv_car_no.setText("车牌:  " + bean.getVehiclePlate());
        holder.tv_etc_card.setVisibility(View.GONE);
        holder.ll_audit_opinion.setVisibility(View.GONE);
        holder.iv_apply_state.setVisibility(View.GONE);
        switch (bean.getStatus()) {
            case "0":
                holder.tv_audit_state.setText("新增");
                break;
            case "1":
                holder.tv_audit_state.setText("审核成功");
                break;
            case "2":
                holder.tv_audit_state.setText("审核失败");
                break;
            case "3":
                holder.tv_audit_state.setText("开卡成功");
                break;
            case "4":
                holder.tv_audit_state.setText("开卡失败");
                break;
            case "5":
                holder.tv_audit_state.setText("接口调用失败，请重试");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        private TextView tv_apply_time, tv_car_no, tv_etc_card, tv_card_type, tv_audit_opinion, tv_audit_state;
        private LinearLayout ll_audit_opinion;
        private ImageView iv_apply_state;


        public MyHolder(@NonNull View itemView) {
            super(itemView);
            tv_apply_time = itemView.findViewById(R.id.tv_apply_time);
            tv_car_no = itemView.findViewById(R.id.tv_car_no);
            tv_etc_card = itemView.findViewById(R.id.tv_etc_card);
            tv_card_type = itemView.findViewById(R.id.tv_card_type);
            ll_audit_opinion = itemView.findViewById(R.id.ll_audit_opinion);
            tv_audit_opinion = itemView.findViewById(R.id.tv_audit_opinion);
            tv_audit_state = itemView.findViewById(R.id.tv_audit_state);
            iv_apply_state = itemView.findViewById(R.id.iv_apply_state);
        }
    }
}
