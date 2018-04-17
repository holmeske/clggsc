package com.sc.clgg.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.activity.transportmanager.WaybillDetailActivity;
import com.sc.clgg.application.App;
import com.sc.clgg.bean.TransportManageBean;
import com.sc.clgg.dialog.HintSelectDialog;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;
import com.sc.clgg.util.Tools;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import tool.widget.ShapeTextView;

/**
 * CreateDate：2017/9/29 20:50
 *
 * @author lvke
 */

public class TransportManageAdapter extends Adapter<TransportManageAdapter.MyViewHolder> {

    private Context mContext;
    private List<TransportManageBean.WaybillListBean> dataList = new ArrayList<>();
    private OperationCallBack operationCallBack;
    private HintSelectDialog mHintSelectDialog;

    private boolean selectAll;

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_waybill, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);

        TransportManageBean.WaybillListBean bean = dataList.get(position);
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {

            holder.mIvArrow.setVisibility(View.GONE);
            holder.mTvOperation.setVisibility(View.GONE);
            holder.mIvThreeDot.setVisibility(View.GONE);

            holder.mFlAmount.setVisibility(View.VISIBLE);

            holder.mTvOrderAmount.setText(String.valueOf(bean.getOrderAmount()) + "元");
            if (App.getInstance().getString(R.string.number_10).equals(bean.getWaybillStatus())) {
                holder.mShapeTextView.setVisibility(View.VISIBLE);
                holder.mShapeTextView.setText(mContext.getString(R.string.not_pick_up_goods));
            } else if (App.getInstance().getString(R.string.number_20).equals(bean.getWaybillStatus())) {
                holder.mShapeTextView.setVisibility(View.VISIBLE);
                holder.mShapeTextView.setText(mContext.getString(R.string.not_sign_for));
            } else {
                holder.mShapeTextView.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        TransportManageBean.WaybillListBean bean = dataList.get(position);

        holder.mTvWaybillDate.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date(bean.getUpdatedAt())));
        holder.mTvWaybillNumber.setText("运单号:" + bean.getWaybillNo());

        if (!TextUtils.isEmpty(bean.getConsigneeCity())) {
            holder.mTvSendAddress.setText("发:" + bean.getDispatchProvince() + mContext.getString(R.string.minus) + bean.getDispatchCity());
        } else {
            holder.mTvSendAddress.setText("发:" + bean.getDispatchProvince());
        }

        if (!TextUtils.isEmpty(bean.getConsigneeCity())) {
            holder.mTvReceiveAddress.setText("收:" + bean.getConsigneeProvince() + mContext.getString(R.string.minus) + bean.getConsigneeCity());
        } else {
            holder.mTvReceiveAddress.setText("收:" + bean.getConsigneeProvince());
        }

        if (selectAll) {
            holder.mIvArrow.setVisibility(View.GONE);
            holder.mTvOperation.setVisibility(View.GONE);
            holder.mIvThreeDot.setVisibility(View.GONE);

            holder.mFlAmount.setVisibility(View.VISIBLE);

            holder.mTvOrderAmount.setText(String.valueOf(bean.getOrderAmount()) + "元");
            if ("10".equals(bean.getWaybillStatus())) {
                holder.mShapeTextView.setVisibility(View.VISIBLE);
                holder.mShapeTextView.setText(mContext.getString(R.string.not_pick_up_goods));
            } else if ("20".equals(bean.getWaybillStatus())) {
                holder.mShapeTextView.setVisibility(View.VISIBLE);
                holder.mShapeTextView.setText(mContext.getString(R.string.not_sign_for));
            } else {
                holder.mShapeTextView.setVisibility(View.GONE);
            }
        } else {
            holder.mIvArrow.setVisibility(View.VISIBLE);
            holder.mTvOperation.setVisibility(View.VISIBLE);
            holder.mIvThreeDot.setVisibility(View.VISIBLE);

            holder.mFlAmount.setVisibility(View.GONE);
            holder.mShapeTextView.setVisibility(View.GONE);

            holder.mTvOperation.setOnClickListener(new OperationListener(bean.getWaybillStatus(), bean.getWaybillNo(), position));

            if ("10".equals(bean.getWaybillStatus())) {
                holder.mTvOperation.setText(mContext.getString(R.string.click_pick_up_goods));
            } else if ("20".equals(bean.getWaybillStatus())) {
                holder.mTvOperation.setText(mContext.getString(R.string.click_sign_for));
            }
        }
        holder.item.setOnClickListener(new onItemClickListener(bean.getWaybillNo(), bean.getWaybillStatus()));
    }

    public void removed(int position) {
        this.dataList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

    //提货签收成功的回调
    public void setOperationCallBack(OperationCallBack operationCallBack) {
        this.operationCallBack = operationCallBack;
    }

    public void refresh(List<TransportManageBean.WaybillListBean> newList, boolean b) {
        this.selectAll = b;
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallBack(dataList, newList), false);
        this.dataList = newList;
        diffResult.dispatchUpdatesTo(this);
    }

    public interface OperationCallBack {
        void callback(int position);
    }

    private class DiffCallBack extends DiffUtil.Callback {
        private List<TransportManageBean.WaybillListBean> oldList, newList;

        public DiffCallBack(List oldList, List newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList == null ? 0 : oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList == null ? 0 : newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            TransportManageBean.WaybillListBean oldBean = oldList.get(oldItemPosition);
            TransportManageBean.WaybillListBean newBean = newList.get(newItemPosition);

            return String.valueOf(oldBean.getUpdatedAt()).equals(String.valueOf(newBean.getUpdatedAt()));
        }

    }

    class onItemClickListener implements View.OnClickListener {
        private String waybillNo, waybillStatus;

        public onItemClickListener(String waybillNo, String waybillStatus) {
            this.waybillNo = waybillNo;
            this.waybillStatus = waybillStatus;
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), WaybillDetailActivity.class);
            intent.putExtra("order_no", waybillNo);
            intent.putExtra("order_status", waybillStatus);
            v.getContext().startActivity(intent);
        }
    }

    class OperationListener implements View.OnClickListener {
        private String waybillStatus, waybillNo;
        private int position;

        public OperationListener(String waybillStatus, String waybillNo, int position) {
            this.waybillStatus = waybillStatus;
            this.waybillNo = waybillNo;
            this.position = position;
        }

        @Override
        public void onClick(final View v) {
            mHintSelectDialog = new HintSelectDialog((Activity) mContext, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (waybillStatus.equals("10")) {
                        HttpRequestHelper.pickUpGoods(waybillNo, new HttpCallBack() {
                            @Override
                            public void onSuccess(String body) {
                                mHintSelectDialog.dismiss();
                                Tools.Toast("提货成功");
                                if (operationCallBack != null) {
                                    operationCallBack.callback(position);
                                }
                            }
                        });
                    } else if (waybillStatus.equals("20")) {
                        HttpRequestHelper.signFor(waybillNo, new HttpCallBack() {
                            @Override
                            public void onSuccess(String body) {
                                mHintSelectDialog.dismiss();
                                Tools.Toast("签收成功");
                                if (operationCallBack != null) {
                                    operationCallBack.callback(position);
                                }
                            }
                        });
                    }
                }
            });
            if (waybillStatus.equals("10")) {
                mHintSelectDialog.show();
                mHintSelectDialog.setHint("是否提货？");
            } else if (waybillStatus.equals("20")) {
                mHintSelectDialog.show();
                mHintSelectDialog.setHint("是否签收？");
            }

        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_waybill_date) TextView mTvWaybillDate;
        @BindView(R.id.shapeTextView) ShapeTextView mShapeTextView;
        @BindView(R.id.tv_waybill_number) TextView mTvWaybillNumber;
        @BindView(R.id.iv_arrow) ImageView mIvArrow;
        @BindView(R.id.ll_detail) LinearLayout mLlDetail;
        @BindView(R.id.tv_send_address) TextView mTvSendAddress;
        @BindView(R.id.iv_three_dot) ImageView mIvThreeDot;
        @BindView(R.id.tv_receive_address) TextView mTvReceiveAddress;
        @BindView(R.id.tv_order_amount) TextView mTvOrderAmount;
        @BindView(R.id.fl_amount) FrameLayout mFlAmount;
        @BindView(R.id.tv_operation) TextView mTvOperation;
        View item;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            item = itemView;
        }
    }
}
