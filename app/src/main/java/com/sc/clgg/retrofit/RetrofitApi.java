package com.sc.clgg.retrofit;

import com.sc.clgg.bean.Area;
import com.sc.clgg.bean.Banner;
import com.sc.clgg.bean.BusinessNoteList;
import com.sc.clgg.bean.CarNumberList;
import com.sc.clgg.bean.CardInfo;
import com.sc.clgg.bean.CardList;
import com.sc.clgg.bean.Check;
import com.sc.clgg.bean.CircleSave;
import com.sc.clgg.bean.Consumption;
import com.sc.clgg.bean.ConsumptionDetail;
import com.sc.clgg.bean.Fault;
import com.sc.clgg.bean.InteractiveDetail;
import com.sc.clgg.bean.IsNotReadInfo;
import com.sc.clgg.bean.Location;
import com.sc.clgg.bean.LocationDetail;
import com.sc.clgg.bean.Message;
import com.sc.clgg.bean.Mileage;
import com.sc.clgg.bean.MileageDetail;
import com.sc.clgg.bean.NoReadInfo;
import com.sc.clgg.bean.PathRecord;
import com.sc.clgg.bean.PersonalData;
import com.sc.clgg.bean.RechargeOrderList;
import com.sc.clgg.bean.ServiceStation;
import com.sc.clgg.bean.StatusBean;
import com.sc.clgg.bean.TallyBook;
import com.sc.clgg.bean.TruckFriend;
import com.sc.clgg.bean.User;
import com.sc.clgg.bean.Vehicle;
import com.sc.clgg.bean.VersionInfoBean;
import com.sc.clgg.bean.WeChatOrder;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author：lvke
 * @date：2018/1/2 10:29
 */

public interface RetrofitApi {

    @GET("etc/cardApply/getEtcBusinessNote")
    Call<BusinessNoteList> getEtcBusinessNote();

    @GET("etc/cardApply/payOrders")
    Call<RechargeOrderList> getRechargeOrderList(@Query("userCode") String userCode);

    @POST("etc/cardApply/loadMoney")
    Call<CircleSave> loadMoney(@Body RequestBody json);

    @POST("etc/cardApply/sureLoadMoney")
    Call<CircleSave> sureLoadMoney(@Body RequestBody json);

    @POST("etc/cardApply/cardList")
    Call<CardList> getCardList(@Body RequestBody json);

    @GET("etc/cardApply/getCarNoByUserCode")
    Call<CarNumberList> getCarNumberList(@Query("userCode") String userCode);

    @POST("etc/cardApply/payMoney")
    Call<StatusBean> payMoney(@Body RequestBody json);

    @POST("etc/cardApply/surePayMoney")
    Call<StatusBean> surePayMoney(@Body RequestBody json);

    @GET("etc/cardApply/getCardInfoByQL")
    Call<CardInfo> getCardInfo(@Query("cardNo") String cardNo, @Query("storeMoney") String storeMoney);

    @GET("etc/cardApply/getVerificationCode")
    Call<StatusBean> identityCertification(@Query("phone") String phone);

    @Multipart
    @POST("etc/cardApply/apply")
    Call<Check> apply(@Part List<MultipartBody.Part> parts);

    @POST("wxpay/preOrder")
    Call<WeChatOrder> wxPay(@Body RequestBody json);

    @GET("vehicleInfo/list")
    Call<Vehicle> myVehicle(@Query("userCode") String userCode);

    @GET("vehicleInfo/remove")
    Call<Check> vehicleDelete(@Query("vinShort") String vinShort, @Query("userId") String userId);

    @GET("cashBook/remove")
    Call<Check> remove(@Query("id") int id);

    @GET("client/list/{userCode}")
    Call<PersonalData> personalData(@Path("userCode") String userCode);

    @GET("sso/checkUser/{userCode}")
    Call<Check> checkUser(@Path("userCode") String userCode);

    @GET("user/validateCode/{phone}/{code}")
    Call<StatusBean> authCodeCheck(@Path("phone") String phone, @Path("code") String code);

    @GET("user/sendCheckCode/{phone}")
    Call<StatusBean> sendCheckCode(@Path("phone") String phone);

    @GET("vehicle/report/oilsteamsDetailsByCorp/{userCode}/{date}")
    Call<Consumption> oilsteams(@Path("userCode") String userCode, @Path("date") String date);

    @GET("vehicle/report/oilsteamsDetails/{vin}/{date}")
    Call<ConsumptionDetail> oilsteamsDetail(@Path("vin") String vin, @Path("date") String date);

    @GET("banner/list")
    Call<Banner> getBannerList();

    @GET("client/getMessageInfo")
    Call<Check> verificationCode(@Query("phoneNum") String phoneNum);

    @POST("sso/doRegister")
    Call<StatusBean> appRegister(@Body RequestBody json);

    @POST("vehicleInfo/add")
    Call<Check> vehicleAdd(@Body RequestBody json);

    @POST("vehicle/report/gps")
    Call<Location> location(@Body RequestBody json);

    @POST("vehicle/report/gps")
    Call<LocationDetail> locationDetail(@Body RequestBody json);

    @POST("cashBook/list")
    Call<TallyBook> tallybook(@Body RequestBody json);

    @POST("client/modifyAccount")
    Call<Check> modifyAccount(@Body RequestBody json);

    @POST("cashBook/add")
    Call<Map<String, Object>> add(@Body RequestBody json);

    @Multipart
    @POST("client/upload")
    Call<Check> upload(@Part List<MultipartBody.Part> parts);

    @Multipart
    @POST("vehicleInfo/scan")
    Call<Map<String, Object>> scan(@Part List<MultipartBody.Part> parts);

    @POST("client/update")
    Call<Check> update(@Body RequestBody json);

    @POST("news/information/list")
    Call<Message> messages(@Body RequestBody json);

    @Multipart
    @POST("feedBack/add")
    Call<Check> feedBack(@Part List<MultipartBody.Part> parts);

    @Multipart
    @POST("driverCircle/sendMessage")
    Call<Check> publishDynamic(@Part List<MultipartBody.Part> parts);

    @POST("driverCircle/list")
    Call<TruckFriend> driverCircle(@Body RequestBody json);

    @POST("driverCircle/sendOpinion")
    Call<Check> sendOpinion(@Body RequestBody json);

    @POST("driverCircle/like")
    Call<Check> like(@Body RequestBody json);

    @GET("driverCircle/noReadInfo")
    Call<NoReadInfo> noReadInfo(@Query("userId") String userId);

    @GET("driverCircle/isNotReadInfo")
    Call<IsNotReadInfo> isNotReadInfo(@Query("userId") String userId);

    @GET("vehicle/report/faultDiagnose/{vin}")
    Call<Fault> faultDiagnose(@Path("vin") String vin);

    @POST("driverCircle/removeLike")
    Call<Check> removeLike(@Body RequestBody json);

    @POST("driverCircle/removeMessage")
    Call<Check> removeMessage(@Body RequestBody json);

    @GET("driverCircle/removeOpinion")
    Call<Check> removeOpinion(@Query("id") String id);

    @GET("driverCircle/getSingleDetail")
    Call<InteractiveDetail> getSingleDetail(@Query("messageId") String messageId);

    @POST("vehicle/report/track")
    Call<PathRecord> pathRecord(@Body RequestBody json);

    @POST("user/modifyPassword")
    Call<StatusBean> modifyPassword(@Body RequestBody json);

    @POST("user/appResetPassword")
    Call<StatusBean> resetPassword(@Body RequestBody json);

    @GET("vehicle/report/milageReport/{userId}/{date}")
    Call<Mileage> mileage(@Path("userId") String userId, @Path("date") String date);

    @GET("vehicle/report/singleMilageReport/{userId}/{date}/{vin}")
    Call<MileageDetail> mileageDetail(@Path("userId") String userId, @Path("date") String date, @Path("vin") String vin);

    @GET("user/check/{phone}/1")
    Call<StatusBean> phoneCheck(@Path("phone") String phone);

    @POST("area/list")
    Call<Area> area(@Body RequestBody json);

    @POST("serviceStation/list")
    Call<ServiceStation> serviceStation(@Body RequestBody json);

    @POST("sso/doLogin")
    Call<User> login(@Body RequestBody json);

    @POST(".")
    Call<VersionInfoBean> versionInfo(@Body RequestBody json);

}