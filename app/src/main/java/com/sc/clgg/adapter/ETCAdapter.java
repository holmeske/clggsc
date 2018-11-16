package com.sc.clgg.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.activity.IdentityCertificationActivity;
import com.sc.clgg.activity.MyCardActivity;
import com.sc.clgg.activity.RechargeActivity;
import com.sc.clgg.adapter.ETCAdapter.MyHolder;
import com.sc.clgg.tool.helper.MeasureHelper;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author：lvke
 * @date：2018/9/12 19:29
 */
public class ETCAdapter extends RecyclerView.Adapter<MyHolder> {
    private Context mContext;
    private String[] names = new String[]{"申请ETC卡", "充值·圈存", "预充值", "充值记录查询",
            "ETC卡余额", "我的ETC卡", "我的车队", "路况查询",};
    private int[] drawables = new int[]{R.drawable.etc_icon1, R.drawable.etc_icon2, R.drawable.etc_icon3, R.drawable.etc_icon4,
            R.drawable.etc_icon5, R.drawable.etc_icon6, R.drawable.etc_icon7, R.drawable.etc_icon8,};

    private Class[] activitys = new Class[]{IdentityCertificationActivity.class, RechargeActivity.class, RechargeActivity.class, RechargeActivity.class
            , RechargeActivity.class, MyCardActivity.class, RechargeActivity.class, RechargeActivity.class};


    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MyHolder(LayoutInflater.from(mContext).inflate(R.layout.item_etc, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.name.setText(names[holder.getAdapterPosition()]);
        holder.name.setCompoundDrawablesWithIntrinsicBounds(null,
                ContextCompat.getDrawable(mContext, drawables[holder.getAdapterPosition()]),
                null,
                null);

        holder.name.setOnClickListener(new MyListener(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return names.length;
    }


    static class MyHolder extends RecyclerView.ViewHolder {
        private TextView name;

        public MyHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name);
            name.setWidth(MeasureHelper.getScreenWidth(itemView.getContext()) / 4);

        }

    }

    class MyListener implements View.OnClickListener {
        private int pos;

        public MyListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            mContext.startActivity(new Intent(mContext, activitys[pos]));
        }
    }

}
