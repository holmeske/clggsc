package com.sc.clgg.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.sc.clgg.R;
import com.sc.clgg.activity.vehiclemanager.maintenance.BusinessInfoActivity;
import com.sc.clgg.adapter.holder.MaintenanceViewHolder;
import com.sc.clgg.application.App;
import com.sc.clgg.bean.ServiceBean;
import com.sc.clgg.bean.StoreInfoBean;
import com.sc.clgg.util.DialogUtil;
import com.sc.clgg.util.MapUtil;
import com.sc.clgg.util.Tools;

import java.util.ArrayList;
import java.util.List;

import tool.helper.LogHelper;

/**
 * CreateDate：2017/8/11 15:00
 * 列表适配器(维修保养,加油加气，物流园)
 *
 * @author lvke
 */

public class MaintenanceAdapter extends RecyclerView.Adapter<MaintenanceViewHolder> {
    private List<StoreInfoBean> dataList = new ArrayList<>();
    private Context mContext;
    private int count;
    private boolean noMore;
    private View.OnClickListener listener;
    private RequestOptions requestOptions;
    private View.OnClickListener onNavigationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            StoreInfoBean sb = (StoreInfoBean) view.getTag();
            ArrayList<ServiceBean> list = MapUtil.getMapApp((Activity) mContext);
            if (!list.isEmpty()) {
                // 显示导航对话框
                DialogUtil.showCustomDialog((Activity) mContext, sb, App.screenWidth, ViewGroup.LayoutParams.WRAP_CONTENT, R.style.Theme_dialog, list);
            }
        }
    };
    private View.OnClickListener onPhoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            StoreInfoBean sb = (StoreInfoBean) view.getTag();
            if (!Tools.isNull(sb.getPhone())) {
                Tools.callPhone(sb.getPhone(), (Activity) mContext);
            } else if (!Tools.isNull(sb.getPhonenum())) {
                Tools.callPhone(sb.getPhonenum(), (Activity) mContext);
            }
        }
    };
    private View.OnClickListener itemClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (Integer) view.getTag();
            Intent intent = new Intent(mContext, BusinessInfoActivity.class);
            intent.putExtra("cls", ((Activity) mContext).getClass());

            App.mLocationBean.setObj(new Gson().toJson(dataList.get(position)));
            App.mLocationBean.setList(new Gson().toJson(dataList));
            App.mLocationBean.setFlag(true);
            App.mLocationBean.setPosition(position);
            mContext.startActivity(intent);
        }
    };

    public MaintenanceAdapter(Context context, List<StoreInfoBean> dataList) {
        this.mContext = context;
        this.dataList = dataList;
        count = dataList.size();
        requestOptions = new RequestOptions();
        requestOptions.error(R.drawable.picture).placeholder(R.drawable.picture);
    }

    @Override
    public MaintenanceViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MaintenanceViewHolder(View.inflate(mContext, R.layout.item_maintenance, null));
    }

    @Override
    public void onBindViewHolder(MaintenanceViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        if (payloads.isEmpty()) {
            // payloads 为 空，说明是更新整个 ViewHolder
            onBindViewHolder(holder, position);
        } else {
            // payloads 不为空，这只更新需要更新的 View 即可。
            LogHelper.e("局部刷新");
            if ((int) payloads.get(0) == 1) {
                holder.tv_loadmore.setVisibility(View.GONE);
            } else {
                holder.tv_loadmore.setVisibility(View.VISIBLE);
                holder.tv_loadmore.setText(R.string.load_more);
            }
        }

    }

    @Override
    public void onBindViewHolder(MaintenanceViewHolder holder, int position) {
        if (position == count - 1) {
            if (!noMore) {
                holder.tv_loadmore.setVisibility(View.VISIBLE);
                holder.tv_loadmore.setText(R.string.load_more);
                if (listener != null) {
                    holder.tv_loadmore.setOnClickListener(listener);
                }
            }
        } else {
            holder.tv_loadmore.setVisibility(View.GONE);
        }
        bindView(holder, position);
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    private void bindView(MaintenanceViewHolder holder, int position) {
        final StoreInfoBean sb = dataList.get(position);

        holder.itemLayout.setTag(position);
        holder.itemLayout.setOnClickListener(itemClickListener);

        Glide.with(mContext).setDefaultRequestOptions(requestOptions).load(sb.getHead()).into(holder.company_img);

        holder.company_name.setText(sb.getName());

        float distance = Tools.calculateLineDistance(sb.getLat(), sb.getLng());

        holder.company_instance.setText(mContext.getString(R.string.placeholders_two, Tools.saveDecimal1(distance), "km"));

        holder.company_position.setText(sb.getAddress());

        holder.company_person.setText(Tools.isNull(sb.getContact()) ? "暂无" : sb.getContact());

        if (!Tools.isNull(sb.getPhone())) {
            holder.company_phone.setText(sb.getPhone());
        } else if (!Tools.isNull(sb.getPhonenum())) {
            holder.company_phone.setText(sb.getPhonenum());
        } else {
            holder.company_phone.setText(R.string.now_no_phone_number);
        }

        holder.line_tel.setTag(sb);
        holder.line_tel.setOnClickListener(onPhoneListener);

        // 经纬度是否有效
        if (sb.getLat() > 0 && sb.getLng() > 0) {
            holder.line_map.setTag(sb);
            holder.line_map.setOnClickListener(onNavigationListener);
        }
    }

    public void refreshLast(int flag) {
        if (dataList.size() > 0) {
            notifyItemChanged(dataList.size() - 1, flag);
        }
    }

    public void refresh(List<StoreInfoBean> newList, boolean noMore) {
        this.noMore = noMore;
        if (newList != null && newList.size() > 0) {

            refreshLast(1);

            dataList.addAll(newList);
            count = dataList.size();

            notifyItemRangeInserted(dataList.size() == 0 ? 0 : dataList.size() - 1, newList.size());
        }
    }

    public void setLoadMoreListener(View.OnClickListener listener) {
        this.listener = listener;
    }


}
