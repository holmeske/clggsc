package com.sc.clgg.activity.vehiclemanager.maintenance;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.sc.clgg.R;
import com.sc.clgg.activity.contact.CommonContact;
import com.sc.clgg.activity.presenter.LogisticsParkPresenter;
import com.sc.clgg.adapter.MaintenanceAdapter;
import com.sc.clgg.application.App;
import com.sc.clgg.base.BaseActivity;
import com.sc.clgg.bean.StoreInfoBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Author：lvke
 * CreateDate：2017/8/11 11:49
 * 物流园列表
 */

public class LogisticsParkActivity extends BaseActivity implements CommonContact {
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private LogisticsParkPresenter presenter;

    private int pageNo = 1;
    private int pageSize = 10;
    private boolean noMore;
    private List<StoreInfoBean> listAll = new ArrayList<>();
    private MaintenanceAdapter adapter;

    @Override
    protected int layoutRes() {
        return R.layout.activity_maintenance;
    }

    @Override
    protected void init() {
        ((ImageButton) findViewById(R.id.titlebar_right)).setImageResource(R.drawable.map_right);
        findViewById(R.id.titlebar_right).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listAll.isEmpty()) return;
                App.getInstance().setLocationBean(listAll, true, 0);
                startActivity();
            }
        });
        presenter = new LogisticsParkPresenter(this);

        adapter = new MaintenanceAdapter(this, listAll);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(adapter);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                noMore = false;
                pageNo = 1;
                presenter.loadData(pageNo, pageSize);
            }
        });

        adapter.setLoadMoreListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TextView) view).setText("加载中...");
                if (!noMore) {
                    pageNo++;
                    presenter.loadData(pageNo, pageSize);
                } else {
                    Toast.makeText(getApplicationContext(), getString(R.string.no_more), Toast.LENGTH_SHORT).show();
                }
            }
        });

        presenter.loadData(pageNo, pageSize);
    }

    private void startActivity() {
        startActivity(new Intent(this, MapActivity.class).putExtra("title", getString(R.string.logistics_park)).putExtra("cls", getClass()));
    }

    @Override
    public void onStartRequest() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.post(new Runnable() {

                @Override
                public void run() {
                    mSwipeRefreshLayout.setRefreshing(true);
                }
            });
        }
    }

    @Override
    public void onSuccess(List<StoreInfoBean> list) {
        if (pageNo == 1) {
            listAll.clear();
        }
        adapter.refresh(list, list.size() < pageSize);
    }

    @Override
    public void onError(String msg) {
        adapter.refreshLast(0);
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onFinish() {
        if (mSwipeRefreshLayout != null && mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

}
