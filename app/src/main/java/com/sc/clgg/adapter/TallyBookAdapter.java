package com.sc.clgg.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.activity.vehiclemanager.tallybook.TallyBookListDetailActivity;
import com.sc.clgg.bean.TallyBookBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import tool.helper.DateHelper;

/**
 * Author：lvke
 * CreateDate：2017/9/25 20:12
 * 记账本首页列表
 *
 * @author lvke
 */

public class TallyBookAdapter extends RecyclerView.Adapter<TallyBookAdapter.MyViewHolder> {
    private List<TallyBookBean.DataBean.WaybillListBean> dataList = new ArrayList<>();
    private Map<String, String> map = new HashMap<>();
    private Context mContext;
    private StringBuilder incomeAndExpend = new StringBuilder();

    private View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            TallyBookBean.DataBean.WaybillListBean bean = (TallyBookBean.DataBean.WaybillListBean) v.getTag();
            mContext.startActivity(new Intent(mContext, TallyBookListDetailActivity.class).putExtra("bean", bean));
        }
    };

    public TallyBookAdapter() {
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MyViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_tally_book, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TallyBookBean.DataBean.WaybillListBean bean = dataList.get(position);
        holder.mTvName.setText(bean.getAddress());
        holder.mTvName.setTag(bean);
        holder.mTvName.setOnClickListener(listener);

        if (mContext.getString(R.string.number_1).equals(bean.getShow())) {
            holder.mFlDateQuantity.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(bean.getUpdateTime())) {
                holder.mTvCurrentDayWeek.setText(DateHelper.getDay(bean.getUpdateTime()) + mContext.getString(R.string.hao_minus) + DateHelper.getWeek(bean.getUpdateTime()));

                if (!TextUtils.isEmpty(map.get(bean.getUpdateTime() + mContext.getString(R.string.minus) + mContext.getString(R.string.in)))) {
                    incomeAndExpend.append("收入：").append(map.get(bean.getUpdateTime() + mContext.getString(R.string.minus) + mContext.getString(R.string.in))).append("元");
                } else {
                    incomeAndExpend.append("收入：0元");
                }
                if (!TextUtils.isEmpty(map.get(bean.getUpdateTime() + mContext.getString(R.string.minus) + mContext.getString(R.string.out)))) {
                    incomeAndExpend.append("   支出：").append(map.get(bean.getUpdateTime() + mContext.getString(R.string.minus) + mContext.getString(R.string.out))).append("元");
                } else {
                    incomeAndExpend.append("   支出：0元");
                }
                holder.mTvWaybillCount.setText(String.valueOf(incomeAndExpend));

                incomeAndExpend.delete(0, incomeAndExpend.length());
            }
        } else {
            holder.mFlDateQuantity.setVisibility(View.GONE);
            holder.mTvCurrentDayWeek.setText("");

            holder.mTvWaybillCount.setText(String.valueOf(incomeAndExpend));
            incomeAndExpend.delete(0, incomeAndExpend.length());
        }

        if (mContext.getString(R.string.in).equals(bean.getInOrOut())) {
            holder.mTvMoney.setText(mContext.getString(R.string.sum) + bean.getAmount());
        } else {
            holder.mTvMoney.setText(mContext.getString(R.string.minus) + bean.getAmount());
        }

    }

    @Override
    public int getItemCount() {
        return dataList != null ? dataList.size() : 0;
    }

    public void refresh(List<TallyBookBean.DataBean.WaybillListBean> dataList, Map<String, String> map) {
        this.dataList = dataList;
        this.map = map;
        notifyDataSetChanged();
    }

    class MyViewHolder extends ViewHolder {

        @BindView(R.id.fl_date_quantity) FrameLayout mFlDateQuantity;
        @BindView(R.id.tv_current_day_week) TextView mTvCurrentDayWeek;
        @BindView(R.id.tv_waybill_count) TextView mTvWaybillCount;
        @BindView(R.id.tv_name) TextView mTvName;
        @BindView(R.id.tv_money) TextView mTvMoney;


        private MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
