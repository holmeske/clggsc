package com.sc.clgg.service;

import android.content.Context;
import android.content.Intent;

import com.sc.clgg.bean.Area;
import com.sc.clgg.retrofit.RetrofitHelper;
import com.sc.clgg.tool.helper.LogHelper;
import com.sc.clgg.widget.AreaPopHelper;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppService extends JobIntentService {


    public AppService() {
    }

    public static void start(Context context) {
        Intent intent = new Intent(context, AppService.class);
        context.startService(intent);
    }

    private void loadAreaData() {
        new RetrofitHelper().getArea().enqueue(new Callback<Area>() {
            @Override
            public void onResponse(Call<Area> call, Response<Area> response) {
                Area area = response.body();
                if (area != null && area.getData() != null && area.getData().size() > 0) {
                    AreaPopHelper.areas_net = new String[area.getData().size()];
                    for (int i = 0, size = area.getData().size(); i < size; i++) {
                        AreaPopHelper.areas_net[i] = area.getData().get(i).getName();
                    }
                }
            }

            @Override
            public void onFailure(Call<Area> call, Throwable t) {

            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LogHelper.e("AppService --- onCreate()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogHelper.e("AppService --- onDestroy()");
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        LogHelper.e("onHandleWork()");
        loadAreaData();
    }
}
