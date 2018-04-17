//package com.sc.clgg.activity.fragment;
//
//
//import android.graphics.Color;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.content.ContextCompat;
//import android.support.v4.widget.SwipeRefreshLayout;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import com.bigkoo.pickerview.TimePickerView;
//import com.sc.clgg.R;
//import com.sc.clgg.activity.basic.MainActivity;
//import com.sc.clgg.activity.contact.TransportManageContact;
//import com.sc.clgg.activity.presenter.TransportManagePresenter;
//import com.sc.clgg.adapter.TransportManageAdapter;
//import com.sc.clgg.bean.MessageEvent;
//import com.sc.clgg.bean.TransportManageBean;
//import com.sc.clgg.dialog.LoadingDialogHelper;
//
//import org.greenrobot.eventbus.EventBus;
//
//import java.text.SimpleDateFormat;
//import java.util.Calendar;
//import java.util.Locale;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import butterknife.Unbinder;
//import toolbox.helper.CalendarHelper;
//import toolbox.helper.LogHelper;
//import toolbox.widget.ShapeLinearLayout;
//import toolbox.widget.ShapeTextView;
//
///**
// * A simple {@link Fragment} subclass.
// * 运输管理
// *
// * @author lvke
// */
//public class TransportManageFragment extends Fragment implements TransportManageContact {
//
//    @BindView(R.id.titlebar_right_text) TextView mTitlebarRightText;
//    @BindView(R.id.tv_wait_pickup_goods) TextView mTvWaitPickupGoods;
//    @BindView(R.id.tv_wait_pickup_goods_number) ShapeTextView mTvWaitPickupGoodsNumber;
//    @BindView(R.id.ll_wait_pickup_goods) ShapeLinearLayout mLlWaitPickupGoods;
//    @BindView(R.id.tv_wait_sign_for) TextView mTvWaitSignFor;
//    @BindView(R.id.tv_wait_sign_for_number) ShapeTextView mTvWaitSignForNumber;
//    @BindView(R.id.ll_wait_sign_for) ShapeLinearLayout mLlWaitSignFor;
//    @BindView(R.id.tv_all) ShapeTextView mTvAll;
//    @BindView(R.id.refreshlayout) SwipeRefreshLayout refreshlayout;
//    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
//
//    private Unbinder unbinder;
//    private TransportManageAdapter mTransportManageAdapter;
//    private TransportManagePresenter presenter;
//    private LoadingDialogHelper mLoadingDialogHelper;
//    private String waybillStatus = "10";
//    private TimePickerView mTimePickerView;
//    private TimePickerView.Builder mBuilder;
//    private Calendar startCalendar = Calendar.getInstance();//开始日期
//    private Calendar mSelectedCalendar = Calendar.getInstance();//系统当前时间
//
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_transport_manage, container, false);
//        unbinder = ButterKnife.bind(this, view);
//        return view;
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        mTransportManageAdapter = new TransportManageAdapter();
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.setAdapter(mTransportManageAdapter);
//
//        mTransportManageAdapter.setOperationCallBack(position -> presenter.getOrderList(waybillStatus, null, null));
//
//        refreshlayout.setOnRefreshListener(() -> presenter.getOrderList(waybillStatus, null, null));
//
//        presenter = new TransportManagePresenter(this);
//        mLoadingDialogHelper = new LoadingDialogHelper(getActivity());
//        presenter.getOrderList(waybillStatus, null, null);
//
//        initDatePicker();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//        LogHelper.e("onResume()");
//        if (getActivity() != null && ((MainActivity) getActivity()).getCurrenMainTabIndex() == 1) {
//            presenter.getOrderList(waybillStatus, null, null);
//        }
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }
//
//    @OnClick({R.id.ll_wait_pickup_goods, R.id.ll_wait_sign_for, R.id.tv_all, R.id.titlebar_right_text})
//    void onViewClicked(View view) {
//        if (getContext() == null) {
//            return;
//        }
//        switch (view.getId()) {
//            case R.id.ll_wait_pickup_goods:
//                mTitlebarRightText.setVisibility(View.GONE);
//                mLlWaitPickupGoods.setSolid(ContextCompat.getColor(getContext(), R.color.red));
//                mLlWaitSignFor.setSolid(ContextCompat.getColor(getContext(), R.color.white));
//                mTvAll.setSolid(ContextCompat.getColor(getContext(), R.color.white));
//
//                mTvWaitPickupGoods.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
//                mTvWaitSignFor.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
//                mTvAll.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
//
//                mTvWaitPickupGoodsNumber.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
//                mTvWaitPickupGoodsNumber.setSolid(ContextCompat.getColor(getContext(), R.color.white));
//
//                mTvWaitSignForNumber.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
//                mTvWaitSignForNumber.setSolid(ContextCompat.getColor(getContext(), R.color.red));
//
//                waybillStatus = "10";
//                presenter.getOrderList(waybillStatus, null, null);
//                break;
//            case R.id.ll_wait_sign_for:
//                mTitlebarRightText.setVisibility(View.GONE);
//                mLlWaitPickupGoods.setSolid(ContextCompat.getColor(getContext(), R.color.white));
//                mLlWaitSignFor.setSolid(ContextCompat.getColor(getContext(), R.color.red));
//                mTvAll.setSolid(ContextCompat.getColor(getContext(), R.color.white));
//
//                mTvWaitPickupGoods.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
//                mTvWaitSignFor.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
//                mTvAll.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
//
//                mTvWaitPickupGoodsNumber.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
//                mTvWaitPickupGoodsNumber.setSolid(ContextCompat.getColor(getContext(), R.color.red));
//                mTvWaitSignForNumber.setTextColor(ContextCompat.getColor(getContext(), R.color.red));
//                mTvWaitSignForNumber.setSolid(ContextCompat.getColor(getContext(), R.color.white));
//
//                waybillStatus = "20";
//                presenter.getOrderList(waybillStatus, null, null);
//                break;
//            case R.id.tv_all:
//                mTitlebarRightText.setVisibility(View.VISIBLE);
//                mTitlebarRightText.setText(getString(R.string.all));
//                mLlWaitPickupGoods.setSolid(ContextCompat.getColor(getContext(), R.color.white));
//                mLlWaitSignFor.setSolid(ContextCompat.getColor(getContext(), R.color.white));
//                mTvAll.setSolid(ContextCompat.getColor(getContext(), R.color.red));
//                mTvWaitPickupGoodsNumber.setSolid(ContextCompat.getColor(getContext(), R.color.red));
//                mTvWaitSignForNumber.setSolid(ContextCompat.getColor(getContext(), R.color.red));
//
//                mTvWaitPickupGoods.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
//                mTvWaitSignFor.setTextColor(ContextCompat.getColor(getContext(), R.color.black));
//                mTvAll.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
//                mTvWaitPickupGoodsNumber.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
//                mTvWaitSignForNumber.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
//
//                waybillStatus = null;
//                presenter.getOrderList(waybillStatus, null, null);
//                break;
//
//            case R.id.titlebar_right_text:
//                mTimePickerView.show();
//                break;
//            default:
//                break;
//        }
//    }
//
//    private void initDatePicker() {
//        startCalendar.set(2013, 5, 18);
//        mBuilder = new TimePickerView.Builder(getActivity(), (date, v) -> {
//            mSelectedCalendar.setTime(date);
//            mTitlebarRightText.setText(new SimpleDateFormat("yyyy年MM月", Locale.getDefault()).format(date));
//            presenter.getOrderList(waybillStatus, CalendarHelper.getFirstDayOfMonth(mSelectedCalendar), CalendarHelper.getLastDayOfMonth(mSelectedCalendar));
//        }).setType(new boolean[]{true, true, false, false, false, false})
//                .setLabel("", "", "", "", "", "")
//                .isCenterLabel(false)
//                .setDividerColor(Color.DKGRAY)
//                .setContentSize(21)
//                .setDate(mSelectedCalendar)
//                .setRangDate(startCalendar, Calendar.getInstance())
//                .setBackgroundId(R.color.white) //设置外部遮罩颜色
//                .setDecorView(null);
//        mTimePickerView = mBuilder.build();
//    }
//
//    @Override
//    public void requestStart() {
//        if (((MainActivity) getActivity()).getCurrenMainTabIndex() == 1) {
//            mLoadingDialogHelper.show();
//        }
//    }
//
//    @Override
//    public void requestSuccess(TransportManageBean bean) {
//        if (null == getActivity()) {
//            return;
//        }
//        mTvWaitSignForNumber.setText(String.valueOf(bean.getWaybillCountProgress()));
//        mTvWaitPickupGoodsNumber.setText(String.valueOf(bean.getWaybillCountWaiting()));
//
//        EventBus.getDefault().post(new MessageEvent(bean.getWaybillCountProgress() + bean.getWaybillCountWaiting()));
//
//        mTransportManageAdapter.refresh(bean.getWaybillList(), waybillStatus == null);
//    }
//
//    @Override
//    public void requestFail(String msg) {
//    }
//
//    @Override
//    public void requestFinish() {
//        mLoadingDialogHelper.dismiss();
//        if (refreshlayout.isRefreshing()) {
//            refreshlayout.setRefreshing(false);
//        }
//    }
//
//}
