package com.sc.clgg.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.activity.MainActivity;
import com.sc.clgg.activity.etc.ApplyStateActivity;
import com.sc.clgg.activity.etc.BalanceQueryPreActivity;
import com.sc.clgg.activity.etc.CardIntroduceActivity;
import com.sc.clgg.activity.etc.MyCardActivity;
import com.sc.clgg.activity.etc.RechargeActivity;
import com.sc.clgg.activity.etc.RechargeOrderActivity;
import com.sc.clgg.adapter.EtcAdapter.MyHolder;
import com.sc.clgg.bean.MessageEvent;
import com.sc.clgg.tool.helper.MeasureHelper;
import com.sc.clgg.util.ConfigUtil;

import org.greenrobot.eventbus.EventBus;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author：lvke
 * @date：2018/9/12 19:29
 */
public class EtcAdapter extends RecyclerView.Adapter<MyHolder> {
    private Context mContext;
    private String[] names = new String[]{
            "申请ETC卡", "充值 · 圈存", "预充值", "订单查询",
            "余额查询", "我的ETC卡", "我的车队", "开卡审核"};

    private int[] drawables = new int[]{
            R.drawable.etc_icon1, R.drawable.etc_icon2, R.drawable.etc_icon3, R.drawable.etc_icon4,
            R.drawable.etc_icon5, R.drawable.etc_icon6, R.drawable.etc_icon7, R.drawable.etc_icon8};

    private Class[] activitys = new Class[]{
            CardIntroduceActivity.class, RechargeActivity.class, MyCardActivity.class, RechargeOrderActivity.class,
            BalanceQueryPreActivity.class, MyCardActivity.class, MainActivity.class, ApplyStateActivity.class};

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

    class ItemListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }

    class MyListener implements View.OnClickListener {
        private int pos;

        MyListener(int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            if (new ConfigUtil().isLogined(mContext)) {
                switch (pos) {
                    case 2:
                        mContext.startActivity(new Intent(mContext, activitys[pos]).putExtra("click", true));
                        break;
                    case 5:
                        mContext.startActivity(new Intent(mContext, activitys[pos]).putExtra("click", false));
                        break;
                    case 6:
                        mContext.startActivity(new Intent(mContext, MainActivity.class));
                        EventBus.getDefault().postSticky(new MessageEvent(2));
                        break;
                    default:
                        mContext.startActivity(new Intent(mContext, activitys[pos]));
                        break;

                }
            }
        }
    }

}
