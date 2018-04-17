package com.sc.clgg.activity.vehiclemanager.tallybook;

import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sc.clgg.R;
import com.sc.clgg.adapter.ExpendListDetailAdapter;
import com.sc.clgg.base.BaseActivity;
import com.sc.clgg.bean.ExpendListBean;
import com.sc.clgg.bean.ExpendListDetailBean;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;
import com.sc.clgg.view.ExtraListView;

import butterknife.BindView;
import butterknife.OnClick;
import tool.helper.ActivityHelper;

/**
 * @author lvke
 */
public class ExpendListDetailActivity extends BaseActivity {

    @BindView(R.id.tv_title) TextView tv_title;
    @BindView(R.id.moreTxt) ImageView moreTxt;
    @BindView(R.id.ibtn_left) ImageButton ibtn_left;
    @BindView(R.id.tv_money) TextView tv_money;
    @BindView(R.id.mList) ExtraListView mList;
    @BindView(R.id.tv_tip) TextView tv_tip;
    private ExpendListDetailAdapter adapter;

    @Override
    protected int layoutRes() {
        return R.layout.activity_spending;
    }

    @Override
    protected void init() {
        moreTxt.setVisibility(View.GONE);
        int year = getIntent().getIntExtra("year", 0);
        int month = getIntent().getIntExtra("month", 0);

        ExpendListBean.DataBean.ListBean bean = getIntent().getParcelableExtra("bean");
        tv_money.setText(String.valueOf(bean.getMoney()));
        tv_title.setText(bean.getCostText() + "明细");
        tv_tip.setText("本月" + bean.getCostText() + "总支出（元）");

        HttpRequestHelper.expendDetail(year, month, bean.getCostType(), new HttpCallBack() {
            @Override
            public void onSuccess(String body) {
                ExpendListDetailBean bean = new Gson().fromJson(body, ExpendListDetailBean.class);

                if (adapter == null) {
                    adapter = new ExpendListDetailAdapter(ExpendListDetailActivity.this, bean);
                    mList.setAdapter(adapter);
                    adapter.setData(bean);
                } else {
                    adapter.setData(bean);
                }
            }
        });
    }

    @OnClick(R.id.ibtn_left)
    void a() {
        ActivityHelper.finishAcMove(this);
    }

}
