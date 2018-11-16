package com.sc.clgg.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.application.App;
import com.sc.clgg.bean.Service;
import com.sc.clgg.bean.ServiceStation;
import com.sc.clgg.tool.helper.LogHelper;
import com.sc.clgg.tool.helper.MeasureHelper;
import com.sc.clgg.util.DialogUtil;
import com.sc.clgg.util.MapUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import androidx.recyclerview.widget.RecyclerView;


/**
 * CreateDate：2017/8/11 15:00
 * 列表适配器(维修保养,加油加气，物流园)
 *
 * @author lvke
 */

public class ServiceStationAdapter extends RecyclerView.Adapter<ServiceStationAdapter.MyHolder> {
    private List<ServiceStation.Page.Station> listAll = new ArrayList<>();
    private Context mContext;
    private boolean noMore;
    private View.OnClickListener listener;
    private View.OnClickListener onNavigationListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ServiceStation.Page.Station sb = (ServiceStation.Page.Station) view.getTag();
            ArrayList<Service> list = MapUtil.getMapApp((Activity) mContext);
            if (!list.isEmpty()) {
                // 显示导航对话框
                DialogUtil.showCustomDialog((Activity) mContext,
                        sb,
                        MeasureHelper.getScreenWidth(App.getInstance()),
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        R.style.Theme_dialog,
                        list);
            }
        }
    };
    private View.OnClickListener onPhoneListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            ServiceStation.Page.Station bean = (ServiceStation.Page.Station) view.getTag();
            List<String> telList = new ArrayList<>();
            if (!TextUtils.isEmpty(bean.getMobile()) && bean.getMobile().length() >= 11) {
                if (bean.getMobile().contains(",")) {
                    telList.addAll(Arrays.asList(bean.getMobile().split(",")));
                } else {
                    telList.add(bean.getMobile());
                }
            }
            if (!TextUtils.isEmpty(bean.getTel()) && bean.getTel().length() >= 11) {
                if (bean.getTel().contains(",")) {
                    telList.addAll(Arrays.asList(bean.getTel().split(",")));
                } else {
                    telList.add(bean.getTel());
                }
            }
            DialogUtil.showTelDialog((Activity) mContext, new ArrayList(new TreeSet(telList)));

        }
    };
    private String stationType = "0";
    private OnRefreshCallback mOnRefreshCallback;

    public ServiceStationAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_service_station, parent, false));
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        if (payloads.isEmpty()) {
            // payloads 为 空，说明是更新整个 ViewHolder
            onBindViewHolder(holder, holder.getAdapterPosition());
        } else {
            // payloads 不为空，这只更新需要更新的 View 即可。
            LogHelper.e("局部刷新");
            if ((int) payloads.get(0) == 1) {
                holder.tv_loadmore.setVisibility(View.GONE);
            } else {
                holder.tv_loadmore.setVisibility(View.VISIBLE);
                holder.tv_loadmore.setText(R.string.loading);
            }
        }

    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        if (holder.getAdapterPosition() == listAll.size() - 1) {
            if (!noMore) {
                holder.tv_loadmore.setVisibility(View.VISIBLE);
                holder.tv_loadmore.setText(R.string.loading);
                if (listener != null) {
                    holder.tv_loadmore.setOnClickListener(listener);
                }
            }
        } else {
            holder.tv_loadmore.setVisibility(View.GONE);
        }
        bindView(holder, holder.getAdapterPosition());
    }

    @Override
    public int getItemCount() {
        return listAll == null ? 0 : listAll.size();
    }

    private void bindView(MyHolder holder, int position) {
        if (stationType.equals("2")) {
            holder.ll_one.setVisibility(View.GONE);
        } else {
            holder.ll_one.setVisibility(View.VISIBLE);
        }
        final ServiceStation.Page.Station sb = listAll.get(position);

        holder.itemLayout.setTag(position);

        holder.short_company_name.setText(sb.getStationDesc() == null ? "" : sb.getStationDesc());
        holder.company_name.setText(sb.getStationName() == null ? "" : sb.getStationName());

        try {
            if (sb.getDistance() > 0) {
                holder.company_instance.setText(sb.getDistance() + "km");
                holder.company_instance.setVisibility(View.VISIBLE);
            } else {
                holder.company_instance.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            holder.company_instance.setVisibility(View.GONE);
            LogHelper.e("捕获异常啦");
        }

        if (stationType.equals("1")) {
            holder.company_position_str.setText("接入范围: ");
        } else {
            holder.company_position_str.setText("地址: ");
        }
        holder.company_position.setText(sb.getAddress() == null ? "" : sb.getAddress());

        holder.company_person.setText(TextUtils.isEmpty(sb.getContact()) ? "" : sb.getContact());
        holder.company_phone.setText(sb.getMobile() == null ? "" : sb.getMobile());
        if (TextUtils.isEmpty(sb.getTel())) {
            holder.ll_contact_phone.setVisibility(View.GONE);
        } else {
            holder.ll_contact_phone.setVisibility(View.VISIBLE);
            holder.contact_phone.setText(sb.getTel() == null ? "" : sb.getTel());
        }

        holder.line_tel.setTag(sb);
        holder.line_tel.setOnClickListener(onPhoneListener);

        // 经纬度是否有效
        if (!TextUtils.isEmpty(sb.getLon()) && !TextUtils.isEmpty(sb.getLat()) && Double.parseDouble(sb.getLat()) > 0 && Double.parseDouble(sb.getLon()) > 0) {
            holder.line_map.setTag(sb);
            holder.line_map.setOnClickListener(onNavigationListener);
        }
    }

    public void refreshLast(int flag) {
        if (listAll.size() > 0) {
            notifyItemChanged(listAll.size() - 1, flag);
        }
    }

    public void clear() {
        listAll.clear();
        notifyDataSetChanged();
    }

    public void refresh(String stationType, List<ServiceStation.Page.Station> newList, boolean noMore) {
        this.stationType = stationType;
        this.noMore = noMore;
        if (newList != null && newList.size() > 0) {
            notifyItemChanged(listAll.size() - 1, 1);

            listAll.addAll(newList);
            notifyItemRangeInserted(listAll.size(), newList.size());
            if (mOnRefreshCallback != null) {
                mOnRefreshCallback.callback(listAll.size());
            }
        }

    }

    public void setOnRefreshCallback(OnRefreshCallback onRefreshCallback) {
        mOnRefreshCallback = onRefreshCallback;
    }

    public void setLoadMoreListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public interface OnRefreshCallback {
        void callback(int size);
    }

    class MyHolder extends RecyclerView.ViewHolder {
        public TextView company_name,// 公司名称
                short_company_name,
                company_instance, // 距离
                company_position, // 位置
                company_position_str,
                company_phone,// 电话
                company_person,// 联系人
                tv_map,// 导航
                contact_phone,
                tv_tel;// 联系电话
        public LinearLayout line_tel,// 打电话
                line_map;// 查看地图
        public LinearLayout itemLayout, ll_one, ll_contact_phone;
        public TextView tv_loadmore;
        View v_divider;

        public MyHolder(View itemView) {
            super(itemView);
            v_divider = itemView.findViewById(R.id.v_divider);
            company_name = itemView.findViewById(R.id.company_name);
            short_company_name = itemView.findViewById(R.id.short_company_name);
            company_instance = itemView.findViewById(R.id.company_instance);
            company_position_str = itemView.findViewById(R.id.company_position_str);
            company_position = itemView.findViewById(R.id.company_position);
            company_phone = itemView.findViewById(R.id.company_phone);
            company_person = itemView.findViewById(R.id.company_person);
            line_tel = itemView.findViewById(R.id.line_tel);
            contact_phone = itemView.findViewById(R.id.contact_phone);
            tv_tel = itemView.findViewById(R.id.tv_tel);
            line_map = itemView.findViewById(R.id.line_map);
            tv_map = itemView.findViewById(R.id.tv_map);
            itemLayout = itemView.findViewById(R.id.itemLayout);
            tv_loadmore = itemView.findViewById(R.id.tv_loadmore);

            ll_one = itemView.findViewById(R.id.ll_one);
            ll_contact_phone = itemView.findViewById(R.id.ll_contact_phone);
        }
    }
}
