package com.sc.clgg.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.bean.ListBean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * CreateDate：2017/10/10 15:14
 *
 * @author lvke
 */

public class DrivingScoreAdapter extends RecyclerView.Adapter<DrivingScoreAdapter.Holder> {

    private Context mContext;
    private List<ListBean> dataList = new ArrayList<>();
    private String timeLine;

    public DrivingScoreAdapter() {
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new Holder(LayoutInflater.from(mContext).inflate(R.layout.item_drive_score, parent, false));
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {

        final ListBean bean = dataList.get(position);

        if (null != bean) {
            if (!TextUtils.isEmpty(bean.getCarno())) {
                holder.mTvCarNo.setText(mContext.getString(R.string.truck_add_truck_num) + mContext.getString(R.string.colon) + bean.getCarno());
            } else {
                holder.mTvCarNo.setText(mContext.getString(R.string.truck_add_truck_num) + mContext.getString(R.string.colon) + mContext.getString(R.string.without));
            }

            holder.mTvDriveMile.setText("行驶里程：" + (bean.getMileage() > 0 ? bean.getMileage() : 0) + "km");

            if (bean.getScore() > 0) {
                DecimalFormat decimalFormat = new DecimalFormat(".#");
                holder.mTvMark.setText(Html.fromHtml(Double.parseDouble(decimalFormat.format(bean.getScore())) + "<font color='#666666'><small> 分</small></font>"));
            } else {
                holder.mTvMark.setText(Html.fromHtml(0 + "<font color='#cccccc'><small> 分</small></font>"));
            }
        }

        switch (position) {
            case 0:
                holder.mTvRanking.setBackgroundResource(R.drawable.drive_mark_1);
                break;
            case 1:
                holder.mTvRanking.setBackgroundResource(R.drawable.drive_mark_2);
                break;
            case 2:
                holder.mTvRanking.setBackgroundResource(R.drawable.drive_mark_3);
                break;
            default:
                holder.mTvRanking.setBackgroundResource(R.drawable.fleet_item_4);
                holder.mTvRanking.setText(String.valueOf(position + 1));
                break;
        }

        /*holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DrivingScoreDetailActivity.class);
            intent.putExtra("mark", bean);
            intent.putExtra("timeLine", timeLine);
            mContext.startActivity(intent);
        });*/
    }

    public void refresh(List<ListBean> dataList, String timeLine) {
        this.dataList = dataList;
        this.timeLine = timeLine;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    static class Holder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_ranking) TextView mTvRanking;
        @BindView(R.id.tv_car_no) TextView mTvCarNo;
        @BindView(R.id.tv_drive_mile) TextView mTvDriveMile;
        @BindView(R.id.tv_mark) TextView mTvMark;

        Holder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
