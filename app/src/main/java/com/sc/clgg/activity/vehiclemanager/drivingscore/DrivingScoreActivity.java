//package com.sc.clgg.activity.vehiclemanager.drivingscore;
//
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.DefaultItemAnimator;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.sc.clgg.R;
//import com.sc.clgg.adapter.DrivingScoreAdapter;
//import com.sc.clgg.bean.DrivingScoreBean;
//import com.sc.clgg.config.NetField;
//import com.sc.clgg.http.ParamsHelper;
//import com.sc.clgg.http.retrofit.RetrofitHelper;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//import toolbox.helper.LogHelper;
//import toolbox.widget.Triangle;
//
//
///**
// * 驾驶评分
// *
// * @author lvke
// */
//public class DrivingScoreActivity extends AppCompatActivity implements OnClickListener {
//    private TextView tv_count_date;
//
//    private RecyclerView mRecyclerView;
//
//    private DrivingScoreAdapter adapter;
//
//    private Triangle triangle_left, triangle_right;
//
//    private String[] datas = new String[]{"上周", "本周", "本月"};
//    private int currentIndex = 1;
//
//    private String timeLine = "02";
//    private Call<DrivingScoreBean> call;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        setContentView(R.layout.activity_drive_score);
//        setTitle(getString(R.string.driving_score));
//        super.onCreate(savedInstanceState);
//
//        tv_count_date = (TextView) findViewById(R.id.tv_count_date);
//        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
//        triangle_left = (Triangle) findViewById(R.id.triangle_left);
//        triangle_right = (Triangle) findViewById(R.id.triangle_right);
//
//        init();
//    }
//
//    private void init() {
//
//        adapter = new DrivingScoreAdapter();
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
//        mRecyclerView.setAdapter(adapter);
//
//        triangle_left.setOnClickListener(this);
//        triangle_right.setOnClickListener(this);
//
//        tv_count_date.setText(datas[currentIndex]);
//
//        retrofit(datas[currentIndex]);
//    }
//
//    private void retrofit(String date) {
//        switch (date) {
//            case "上周":
//                timeLine = "01";
//                break;
//            case "本周":
//                timeLine = "02";
//                break;
//            case "本月":
//                timeLine = "03";
//                break;
//            default:
//                break;
//        }
//        call = new RetrofitHelper()
//                .init(NetField.SITE)
//                .drivingScore(ParamsHelper.drivingScore(timeLine));
//
//        call.enqueue(new Callback<DrivingScoreBean>() {
//            @Override
//            public void onResponse(Call<DrivingScoreBean> call, Response<DrivingScoreBean> response) {
//                LogHelper.e("onResponse()"
//                        + "\n" + call.request().url()
//                        + "\n" + response.isSuccessful()
//                        + "\n" + response.headers().toString());
//
//                DrivingScoreBean d = response.body();
//                LogHelper.e("DrivingScoreBean == " + new Gson().toJson(d));
//
//                adapter.refresh(d.getList(), timeLine);
//            }
//
//            @Override
//            public void onFailure(Call<DrivingScoreBean> call, Throwable t) {
//                LogHelper.e("onFailure()");
//                LogHelper.e(t.getMessage());
//            }
//
//        });
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.triangle_left:
//                if (currentIndex == 0) {
//                    return;
//                }
//
//                currentIndex--;
//                tv_count_date.setText(datas[currentIndex]);
//
//                retrofit(datas[currentIndex]);
//
//                LogHelper.e("" + datas[currentIndex]);
//                break;
//
//            case R.id.triangle_right:
//                if (currentIndex == 2) {
//                    return;
//                }
//
//                currentIndex++;
//                tv_count_date.setText(datas[currentIndex]);
//
//                retrofit(datas[currentIndex]);
//
//                LogHelper.e("" + datas[currentIndex]);
//                break;
//
//            default:
//                break;
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (!call.isCanceled()) {
//            call.cancel();
//        }
//    }
//}
