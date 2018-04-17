package com.sc.clgg.activity.vehiclemanager.monitor;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sc.clgg.R;
import com.sc.clgg.adapter.ChooseVehicleAdapter;
import com.sc.clgg.base.BaseActivity;
import com.sc.clgg.bean.VehicleLocation;
import com.sc.clgg.bean.VehicleLocationBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import tool.helper.LogHelper;


/**
 * 选择车
 *
 * @author lvke
 */
public class ChooseVehicleActivity extends BaseActivity {

    @BindView(R.id.tv_chose_1) TextView tv_chose_1;
    @BindView(R.id.tv_chose_2) TextView tv_chose_2;
    @BindView(R.id.tv_chose_3) TextView tv_chose_3;
    @BindView(R.id.tv_chose_4) TextView tv_chose_4;
    @BindView(R.id.et_account) EditText et_account;
    @BindView(R.id.linear_return_map) LinearLayout linear_return_map;
    @BindView(R.id.tradeListView) RecyclerView mRecyclerView;
    private VehicleLocation mVehicleLocation;
    private View.OnClickListener onLoadMoreClick = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            if (arg0.getId() == R.id.loadMoreLayout) {
                if (((TextView) arg0.getTag()).getText().toString().equals("加载更多")) {
                    ((TextView) arg0.getTag()).setText("加载中...");
                }
            }
        }
    };
    private ChooseVehicleAdapter adapter;
    private Map<String, VehicleLocationBean> mTotalMap = new HashMap<>();

    @OnClick({R.id.tv_chose_1, R.id.tv_chose_2, R.id.tv_chose_3, R.id.tv_chose_4, R.id.et_account, R.id.linear_return_map})
    void click(View v) {
        if (mVehicleLocation == null) {
            return;
        }
        VehicleLocation contener = new VehicleLocation();
        String inputText = et_account.getText().toString().trim();
        switch (v.getId()) {
            case R.id.tv_chose_1:
                changeStatus(R.id.tv_chose_1);
                if (!TextUtils.isEmpty(inputText)) {
                    refresh(inputText, null);
                    return;
                }
                adapter.refresh(mVehicleLocation);
                break;
            case R.id.tv_chose_2:
                changeStatus(R.id.tv_chose_2);
                if (!TextUtils.isEmpty(inputText)) {
                    refresh(inputText, "1");
                    return;
                }
                contener.setRunList(mVehicleLocation.getRunList());
                adapter.refresh(contener);
                break;
            case R.id.tv_chose_3:
                changeStatus(R.id.tv_chose_3);
                if (!TextUtils.isEmpty(inputText)) {
                    refresh(inputText, "2");
                    return;
                }
                contener.setStopList(mVehicleLocation.getStopList());
                contener.setWarnList(mVehicleLocation.getWarnList());
                adapter.refresh(contener);
                break;
            case R.id.tv_chose_4:
                changeStatus(R.id.tv_chose_4);
                if (!TextUtils.isEmpty(inputText)) {
                    refresh(inputText, "3");
                    return;
                }
                contener.setLeaveList(mVehicleLocation.getLeaveList());
                adapter.refresh(contener);
                break;
            case R.id.linear_return_map:
                onBackPressed();
                break;
        }

    }

    private void refresh(String inputText, String status) {
        VehicleLocation vehicleLocation = new VehicleLocation();
        if (mTotalMap.containsKey(inputText)) {

            List<VehicleLocationBean> list = new ArrayList<>();

            if (status != null) {
                if (status.equals("2")) {
                    String s = mTotalMap.get(inputText).getStatus();
                    if (s.equals("2") || s.equals("3")) {
                        list.add(mTotalMap.get(inputText));
                    }
                } else {
                    if (mTotalMap.get(inputText).getStatus().equals(status)) {
                        list.add(mTotalMap.get(inputText));
                    }
                }

            } else {
                list.add(mTotalMap.get(inputText));
            }

            vehicleLocation.setRunList(list);
            adapter.refresh(vehicleLocation);
        }
    }

    @Override
    public void initTitle() {
        setTitle(getString(R.string.select_car));
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_choose_vehicle;
    }

    @Override
    protected void init() {
        initData();
        initListener();
    }

    private void initData() {
        mVehicleLocation =  getIntent().getParcelableExtra("allTnfo");

        List<VehicleLocationBean> leaveList = mVehicleLocation.getLeaveList();
        List<VehicleLocationBean> runList = mVehicleLocation.getRunList();
        List<VehicleLocationBean> stopList = mVehicleLocation.getStopList();
        List<VehicleLocationBean> warnList = mVehicleLocation.getWarnList();

        List<VehicleLocationBean> totalList = new ArrayList<>();
        if (leaveList != null && leaveList.size() > 0) {
            totalList.addAll(leaveList);
        }
        if (runList != null && runList.size() > 0) {
            totalList.addAll(runList);
        }
        if (stopList != null && stopList.size() > 0) {
            totalList.addAll(stopList);
        }
        if (warnList != null && warnList.size() > 0) {
            totalList.addAll(warnList);
        }

        for (VehicleLocationBean data : totalList) {
            if (!TextUtils.isEmpty(data.getLatitude()) && !TextUtils.isEmpty(data.getLongitude())) {
                mTotalMap.put(data.getCarno(), data);
            }
        }

        adapter = new ChooseVehicleAdapter(this, mVehicleLocation, onLoadMoreClick);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setAdapter(adapter);
        adapter.refresh(mVehicleLocation);

    }

    private void initListener() {
        et_account.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                LogHelper.e("mTotalMap = " + new Gson().toJson(mTotalMap));
                VehicleLocation vehicleLocation = new VehicleLocation();
                if (!TextUtils.isEmpty(s.toString()) && mTotalMap.containsKey(s.toString())) {
                    List<VehicleLocationBean> list = new ArrayList<>();
                    list.add(mTotalMap.get(s.toString()));
                    vehicleLocation.setRunList(list);
                }
                adapter.refresh(vehicleLocation);
            }
        });
    }

    private void changeStatus(int id) {
        switch (id) {
            case R.id.tv_chose_1:
                tv_chose_1.setEnabled(false);
                tv_chose_2.setEnabled(true);
                tv_chose_3.setEnabled(true);
                tv_chose_4.setEnabled(true);
                break;
            case R.id.tv_chose_2:
                tv_chose_1.setEnabled(true);
                tv_chose_2.setEnabled(false);
                tv_chose_3.setEnabled(true);
                tv_chose_4.setEnabled(true);
                break;
            case R.id.tv_chose_3:
                tv_chose_1.setEnabled(true);
                tv_chose_2.setEnabled(true);
                tv_chose_3.setEnabled(false);
                tv_chose_4.setEnabled(true);
                break;
            case R.id.tv_chose_4:
                tv_chose_1.setEnabled(true);
                tv_chose_2.setEnabled(true);
                tv_chose_3.setEnabled(true);
                tv_chose_4.setEnabled(false);
                break;
        }
    }

}


