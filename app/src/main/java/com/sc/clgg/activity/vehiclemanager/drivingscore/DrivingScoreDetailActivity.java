package com.sc.clgg.activity.vehiclemanager.drivingscore;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sc.clgg.R;
import com.sc.clgg.adapter.DrivingScoreDetailAdapter;
import com.sc.clgg.bean.DrivingScoreDetailBean;
import com.sc.clgg.bean.ListBean;
import com.sc.clgg.dialog.LoadingDialogHelper;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;

import java.text.DecimalFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 驾驶评分的列表详情
 *
 * @author lvke
 */
public class DrivingScoreDetailActivity extends AppCompatActivity {
    @BindView(R.id.tv_drive_mile) TextView mTvDriveMile;
    @BindView(R.id.tv_mark) TextView mTvMark;
    @BindView(R.id.lv) ListView mLv;

    private DrivingScoreDetailBean mData;
    private DrivingScoreDetailAdapter adapter;
    private LoadingDialogHelper mLoadingDialogHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //--------------------------------------注意 Parcelable---------------------------------------
       ListBean bean = getIntent().getParcelableExtra("mark");
        //-------------------------------------注意 Parcelable----------------------------------------

        setContentView(R.layout.activity_driving_score_detail);
        ButterKnife.bind(this);
        setTitle((!TextUtils.isEmpty(bean.getCarno())) ? ("车牌号:" + bean.getCarno()) : "暂无车辆信息");
        super.onCreate(savedInstanceState);

        if (bean != null && bean.getScore() > 0) {
            DecimalFormat decimalFormat = new DecimalFormat(".#");
            mTvMark.setText(Double.parseDouble(decimalFormat.format(bean.getScore())) + "分");
        } else {
            mTvMark.setText("0分");
        }
        mTvDriveMile.setText("行驶里程：" + (bean.getMileage() > 0 ? bean.getMileage() : 0) + "km");

        mLoadingDialogHelper = new LoadingDialogHelper(this);
        adapter = new DrivingScoreDetailAdapter(this);
        mLv.setAdapter(adapter);

        loadData(bean.getCarno());
    }

    private void loadData(String carno) {

        HttpRequestHelper.drivingScoreDetail(getIntent().getStringExtra("timeLine"), carno, new HttpCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                mLoadingDialogHelper.show();
            }

            @Override
            public void onSuccess(String body) {
                mData = new Gson().fromJson(body, DrivingScoreDetailBean.class);
                if (mData != null) {
                    adapter.setData((ArrayList) mData.getList());
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mLoadingDialogHelper.dismiss();
            }
        });
    }

}
