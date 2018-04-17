package com.sc.clgg.activity.vehiclemanager.tallybook;

import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sc.clgg.R;
import com.sc.clgg.adapter.WaybillDetailAdapter;
import com.sc.clgg.base.BaseActivity;
import com.sc.clgg.bean.PayDetailBean;
import com.sc.clgg.bean.TallyBookBean;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;

import java.util.List;

import butterknife.BindView;

//记账本首页运单列表详情
public class TallyBookListDetailActivity extends BaseActivity {

    @BindView(R.id.tv_waybill_balance) TextView tvWaybillBalance;
    @BindView(R.id.recyclerView) RecyclerView mRecyclerView;
    @BindView(R.id.titlebar_title) TextView mTvTitle;

    private WaybillDetailAdapter adapter = new WaybillDetailAdapter();

    @Override
    protected int layoutRes() {
        return R.layout.activity_tally_book_list_detail;
    }

    @Override
    protected void init() {
        TallyBookBean.DataBean.WaybillListBean bean = getIntent().getParcelableExtra("bean");
        mTvTitle.setText(bean.getAddress());

        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);

        HttpRequestHelper.tallybookListDetail(bean.getWaybillNo(), new HttpCallBack() {
            @Override
            public void onSuccess(String body) {
                PayDetailBean bean = new Gson().fromJson(body, PayDetailBean.class);

                double amount = bean.getData().getAmount();

                PayDetailBean.DataBean.CostListBean headBean = new PayDetailBean.DataBean.CostListBean();
                headBean.setMoney(amount);
                headBean.setCostText("收入");

                List<PayDetailBean.DataBean.CostListBean> dataList = bean.getData().getCostList();
                double sum = 0;
                for (int i = 0, size = dataList.size(); i < size; i++) {
                    sum += dataList.get(i).getMoney();
                }
                tvWaybillBalance.setText(String.valueOf(amount - sum));
                dataList.add(0, headBean);
                adapter.refresh(dataList);
            }
        });
    }

}
