package com.sc.clgg.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.bean.Mileage;
import com.sc.clgg.tool.helper.DecimalFormatHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：lvke
 * @date：2018/5/23 15:35
 */
public class MileageStatisticalAdapter extends RecyclerView.Adapter<MileageStatisticalAdapter.MyHolder> {
    private List<Mileage.A> data = new ArrayList<>();

    public MileageStatisticalAdapter(ItemClickListener listener) {
    this.mItemClickListener=listener;
    }

    public void refresh(List<Mileage.A> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mileage_statistical, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        Mileage.A bean = data.get(holder.getAdapterPosition());
        if (holder.getAdapterPosition() == 0) {
            holder.iv_rank.setVisibility(View.VISIBLE);
            holder.tv_rank.setVisibility(View.INVISIBLE);
            holder.iv_rank.setImageResource(R.drawable.mileage_pic_first);
        } else if (holder.getAdapterPosition() == 1) {
            holder.iv_rank.setVisibility(View.VISIBLE);
            holder.tv_rank.setVisibility(View.INVISIBLE);
            holder.iv_rank.setImageResource(R.drawable.mileage_pic_second);
        } else if (holder.getAdapterPosition() == 2) {
            holder.iv_rank.setVisibility(View.VISIBLE);
            holder.tv_rank.setVisibility(View.INVISIBLE);
            holder.iv_rank.setImageResource(R.drawable.mileage_pic_third);
        } else {
            holder.iv_rank.setVisibility(View.INVISIBLE);
            holder.tv_rank.setVisibility(View.VISIBLE);
            holder.tv_rank.setText(String.valueOf(holder.getAdapterPosition()+1));
        }
        holder.tv_carno.setText(bean.getCarNo() == null ? "" : bean.getCarNo());
        holder.tv_mileage.setText(bean.getIntervalMileage() == null ? "" : DecimalFormatHelper.formatTwo(bean.getIntervalMileage()) + "km");

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener!=null&&Double.parseDouble(bean.getIntervalMileage())!=0){
                    mItemClickListener.click(bean.getVin(),bean.getCarNo());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {
        public ImageView iv_rank;
        public TextView tv_rank, tv_carno, tv_mileage;

        public MyHolder(View itemView) {
            super(itemView);
            iv_rank = itemView.findViewById(R.id.iv_rank);
            tv_rank = itemView.findViewById(R.id.tv_rank);
            tv_carno = itemView.findViewById(R.id.tv_carno);
            tv_mileage = itemView.findViewById(R.id.tv_mileage);
        }
    }

    private  ItemClickListener mItemClickListener;
    public interface ItemClickListener {
        void click(String vin,String carno );
    }
}
