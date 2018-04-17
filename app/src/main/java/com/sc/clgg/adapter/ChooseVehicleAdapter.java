package com.sc.clgg.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.activity.vehiclemanager.monitor.MonitorDetailActivity;
import com.sc.clgg.adapter.holder.BaseRecyclerViewHolder;
import com.sc.clgg.adapter.holder.ChooseVehicleViewHolder;
import com.sc.clgg.bean.VehicleLocation;
import com.sc.clgg.bean.VehicleLocationBean;
import com.sc.clgg.util.Tools;

import java.util.ArrayList;
import java.util.List;


public class ChooseVehicleAdapter extends CLGGRecyclerViewAdapter {

    private boolean isHasFoot = false;
    private OnClickListener onLoadMoreClick;
    private VehicleLocation mDatas = null;
    private Context mContext;
    private int count = 0;//item数量
    private List<VehicleLocationBean> leaveList = null;
    private List<VehicleLocationBean> runList = null;
    private List<VehicleLocationBean> stopList = null;
    private List<VehicleLocationBean> warnList = null;
    private List<List<VehicleLocationBean>> mAdapterData = null;

    private LinearLayout.LayoutParams layoutParams =
            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT); // 每行的水平LinearLayout

    public ChooseVehicleAdapter(Context context, VehicleLocation tradeBeans, OnClickListener onLoadMoreClick) {
        super(context);
        layoutParams.setMargins(10, 3, 10, 3);
        layoutParams.gravity = Gravity.CENTER_HORIZONTAL;
        mAdapterData = new ArrayList<>();
        mContext = context;
        this.mDatas = tradeBeans;
        this.onLoadMoreClick = onLoadMoreClick;
        if (mDatas == null) {
            return;
        }
        leaveList = mDatas.getLeaveList();
        runList = mDatas.getRunList();
        stopList = mDatas.getStopList();
        warnList = mDatas.getWarnList();

        if (leaveList != null && leaveList.size() > 0) {
            count++;
            mAdapterData.add(leaveList);
        }
        if (runList != null && runList.size() > 0) {
            count++;
            mAdapterData.add(runList);
        }
        if (stopList != null && stopList.size() > 0) {
            count++;
            mAdapterData.add(stopList);
        }
        if (warnList != null && warnList.size() > 0) {
            count++;
            mAdapterData.add(warnList);
        }

    }

    public void refresh(VehicleLocation tradeBeans) {
        this.mDatas = tradeBeans;
        leaveList = tradeBeans.getLeaveList();
        runList = tradeBeans.getRunList();
        stopList = tradeBeans.getStopList();
        warnList = mDatas.getWarnList();
        count = 0;
        mAdapterData.clear();
        if (mDatas == null) {
            return;
        }
        if (leaveList != null && leaveList.size() > 0) {
            count++;
            mAdapterData.add(leaveList);
        }
        if (runList != null && runList.size() > 0) {
            count++;
            mAdapterData.add(runList);
        }
        if (stopList != null && stopList.size() > 0) {
            count++;
            mAdapterData.add(stopList);
        }
        if (warnList != null && warnList.size() > 0) {
            count++;
            mAdapterData.add(warnList);
        }
        notifyDataSetChanged();
    }

    @Override
    protected int getHeaderItemCount() {
        return 0;
    }

    @Override
    protected int getFooterItemCount() {
        if (isHasFoot) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    protected int getContentItemCount() {
        return count;
    }

    @Override
    protected ViewHolder onCreateHeaderItemViewHolder(ViewGroup parent, int headerViewType) {
        return null;
    }

    @Override
    protected ViewHolder onCreateContentItemViewHolder(ViewGroup parent, int contentViewType) {
        View view = mInflater.inflate(R.layout.item_choose_vehicle, null);
        ChooseVehicleViewHolder viewHolder = new ChooseVehicleViewHolder(view);
        return viewHolder;
    }

    @Override
    protected void onBindHeaderItemViewHolder(ViewHolder headerViewHolder, int position) {
    }

    @Override
    protected ViewHolder onCreateFooterItemViewHolder(ViewGroup parent, int footerViewType) {
        RelativeLayout footAddMoreBookLayout = (RelativeLayout) mInflater.inflate(R.layout.view_loading_more, null);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        params.addRule(Gravity.CLIP_HORIZONTAL);
        footAddMoreBookLayout.setLayoutParams(params);
        BaseRecyclerViewHolder viewHolder = new BaseRecyclerViewHolder(footAddMoreBookLayout);
        TextView addMoreBookTxt = (TextView) footAddMoreBookLayout.findViewById(R.id.loadMoreTxt);
        RelativeLayout loadMoreLayout = (RelativeLayout) footAddMoreBookLayout.findViewById(R.id.loadMoreLayout);
        ImageView imgLine = (ImageView) footAddMoreBookLayout.findViewById(R.id.imgLine);
        imgLine.setVisibility(View.VISIBLE);
        loadMoreLayout.setOnClickListener(onLoadMoreClick);
        loadMoreLayout.setTag(addMoreBookTxt);
        // addMoreBookTxt.setOnClickListener(onClickListener);
        return viewHolder;
    }

    @Override
    protected void onBindContentItemViewHolder(ViewHolder contentViewHolder, int position) {
        ChooseVehicleViewHolder holder = (ChooseVehicleViewHolder) contentViewHolder;
        List<VehicleLocationBean> list = mAdapterData.get(position);

        if (list == null || list.size() <= 0) {
            return;
        }

        if (list.get(0).getStatus().equals("0")) {
            holder.tv_car_status.setText("静止车辆");
        } else if (list.get(0).getStatus().equals("1")) {
            holder.tv_car_status.setText("行驶中车辆");
        } else if (list.get(0).getStatus().equals("2")) {
            holder.tv_car_status.setText("断开连接车辆");
        } else {
            if (Double.parseDouble(list.get(0).getSpeed()) > 0) {
                holder.tv_car_status.setText("行驶中车辆");
            } else {
                holder.tv_car_status.setText("静止车辆");
            }
        }

        holder.mainLayout.removeAllViews();

        LinearLayout rowLayout = null;
        for (int i = 0; i < list.size(); i++) {


            if (i % 2 == 0) {
                rowLayout = new LinearLayout(mContext);
                rowLayout.setOrientation(LinearLayout.HORIZONTAL);
                rowLayout.setLayoutParams(layoutParams);
                holder.mainLayout.addView(rowLayout);
            }

            TextView tv_car_no = (TextView) LayoutInflater.from(mContext).inflate(R.layout.view_choose_vehicle, null);
            tv_car_no.setText(list.get(i).getCarno() == null ? "" : list.get(i).getCarno());
            tv_car_no.setTag(list.get(i));

            LinearLayout.LayoutParams param = new LinearLayout.LayoutParams(
                    Tools.getWindowWidth((Activity) mContext) / 2 - Tools.px2dip(mContext, 110),
                    LayoutParams.MATCH_PARENT);
            param.setMargins(8, 8, 8, 8);

            tv_car_no.setLayoutParams(param);

            tv_car_no.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MonitorDetailActivity.class);
                    intent.putExtra("info", (VehicleLocationBean) v.getTag());
                    intent.putExtra("allTnfo", mDatas);
                    mContext.startActivity(intent);
                }
            });
            rowLayout.addView(tv_car_no);
        }
    }

}

