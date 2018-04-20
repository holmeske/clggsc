package com.sc.clgg.http.retrofit;

import com.sc.clgg.bean.DrivingScoreBean;
import com.sc.clgg.bean.PayDetailBean;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * @author：lvke
 * @date：2018/1/2 10:29
 */

public interface RetrofitHelper {

    @Headers({"Content-Type: application/json", "Accept: application/json"})//需要添加头
    @POST("txj/driveReport/getDriveReportList")
    Call<DrivingScoreBean> drivingScore(@Body Object s);

    @GET("account/waybillCostDetail/{waybillNo}")
    Call<PayDetailBean> get(@Path("waybillNo") String waybillNo);
}
