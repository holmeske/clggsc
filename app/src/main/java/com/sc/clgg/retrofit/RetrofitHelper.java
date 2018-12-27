package com.sc.clgg.retrofit;

import com.clgg.api.contract.ClggApiException;
import com.clgg.api.contract.ClggConstants;
import com.clgg.api.signature.ClggSignature;
import com.google.gson.Gson;
import com.sc.clgg.BuildConfig;
import com.sc.clgg.application.App;
import com.sc.clgg.application.AppPresenterKt;
import com.sc.clgg.bean.Area;
import com.sc.clgg.bean.Banner;
import com.sc.clgg.bean.BusinessNoteList;
import com.sc.clgg.bean.CarNumberList;
import com.sc.clgg.bean.CardInfo;
import com.sc.clgg.bean.CardList;
import com.sc.clgg.bean.CertificationInfo;
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
import com.sc.clgg.config.ConstantValue;
import com.sc.clgg.config.NetField;
import com.sc.clgg.tool.helper.LogHelper;
import com.sc.clgg.util.ConfigUtil;
import com.sc.clgg.util.Tools;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.sc.clgg.config.NetField.PUBLIC_SITE;

/**
 * @author：lvke
 * @date：2018/5/3 14:36
 */
public class RetrofitHelper {
    private HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger()).setLevel(HttpLoggingInterceptor.Level.BODY);

    private Map<String, String> sign(Map<String, String> params) {
        try {
            if (!params.containsKey("version")) {
                params.put("version", NetField.VERSION);
            }
            params.put("charset", NetField.CHARSET);
            params.put("merchantid", NetField.MERCHANTID);
            params.put("sign_type", NetField.SIGN_TYPE);
            params.put("sign", ClggSignature.rsaSign(params, NetField.D_KEY, ClggConstants.CHARSET_UTF8));
        } catch (ClggApiException e) {
            e.printStackTrace();
        }
        return params;
    }

    private Retrofit Retrofit(String... baseUrl) {
        OkHttpClient client;
        if (BuildConfig.DEBUG) {
            client = new OkHttpClient.Builder()
                    .addInterceptor(logInterceptor)
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();
        } else {
            client = new OkHttpClient.Builder()
                    .connectTimeout(60, TimeUnit.SECONDS)
                    .readTimeout(60, TimeUnit.SECONDS)
                    .writeTimeout(60, TimeUnit.SECONDS)
                    .build();
        }
        return new Retrofit.Builder()
                .baseUrl(baseUrl.length == 0 ? NetField.SITE : baseUrl[0])
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    /**
     * 微信支付
     */
    public retrofit2.Call<WeChatOrder> wxPay(String card_no, String total_fee) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("card_no", card_no);
        params.put("total_fee", total_fee);
        params.put("spbill_create_ip", Tools.getIpAddress(App.getInstance().getApplicationContext()));

        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));
        return Retrofit(PUBLIC_SITE).create(RetrofitApi.class).wxPay(json);
    }

    /**
     * 开卡申请
     */
    public retrofit2.Call<Check> apply(CertificationInfo info) {

        List<MultipartBody.Part> parts = new ArrayList<>();

        parts.add(MultipartBody.Part.createFormData("cardType", info.getCardType()));
        parts.add(MultipartBody.Part.createFormData("userCode", new ConfigUtil().getUserid()));
        parts.add(MultipartBody.Part.createFormData("userType", info.getUserType()));
        parts.add(MultipartBody.Part.createFormData("userName", info.getUserName()));
        parts.add(MultipartBody.Part.createFormData("certType", "2"));
        parts.add(MultipartBody.Part.createFormData("certSn", info.getCertSn()));
        parts.add(MultipartBody.Part.createFormData("linkMobile", info.getLinkMobile()));
        parts.add(MultipartBody.Part.createFormData("invitationCode", info.getInvitationCode()));

        if (!info.getIdcardImgFront().isEmpty()) {
            parts.add(creatPart("idcardImgFront", randomId(), info.getIdcardImgFront()));
        }
        if (!info.getIdcardImgBehind().isEmpty()) {
            parts.add(creatPart("idcardImgBehind", randomId(), info.getIdcardImgBehind()));
        }
        if (!info.getBusinessLicenseImg().isEmpty()) {
            parts.add(creatPart("businessLicenseImg", randomId(), info.getBusinessLicenseImg()));
        }

        for (CertificationInfo.Car car : info.getEtcCardApplyVehicleVoList()) {
            car.setImageId(randomId());
            parts.add(creatPart("allVehicleImages", car.getImageId(), car.getVehicleLicenseImg()));
        }
        parts.add(MultipartBody.Part.createFormData("etcCardApplyVehicleVoList", new Gson().toJson(info.getEtcCardApplyVehicleVoList())));


        parts.add(MultipartBody.Part.createFormData("recipientsName", info.getRecipientsName()));
        parts.add(MultipartBody.Part.createFormData("recipientsPhone", info.getRecipientsPhone()));
        parts.add(MultipartBody.Part.createFormData("recipientsAddress", info.getRecipientsAddress()));

        LogHelper.e("info = " + new Gson().toJson(info));
        return Retrofit().create(RetrofitApi.class).apply(parts);
    }

    private String randomId() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase(Locale.getDefault());
    }

    private MultipartBody.Part creatPart(String name, String id, String filePath) {
        File file = new File(filePath);

        String fileName = file.getName();

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part part = MultipartBody.Part.createFormData(name,
                id + fileName.substring(fileName.indexOf("."), fileName.length()),
                requestFile);
        return part;
    }

    /**
     * 身份验证码
     */
    public retrofit2.Call<StatusBean> identityCertification(String phone) {
        return Retrofit().create(RetrofitApi.class).identityCertification(phone);
    }

    /**
     * 我的车辆
     */
    public retrofit2.Call<Vehicle> myVehicle() {
        return Retrofit().create(RetrofitApi.class).myVehicle(getDefaultUserId());
    }

    /**
     * 获取mac2
     */
    public retrofit2.Call<CircleSave> loadMoney(String cardNo, String money, String adjustMoney, String mac1,
                                                String storeMoney, String rand, String onlineNum, String bluetoothSn) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("cardNo", cardNo);
        params.put("money", money);
        params.put("adjustMoney", adjustMoney);
        params.put("mac1", mac1);
        params.put("storeMoney", storeMoney);
        params.put("rand", rand);
        params.put("onlineNum", onlineNum);
        params.put("bluetoothSn", bluetoothSn);
        params.put("userCode", new ConfigUtil().getUserid());

        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));
        return Retrofit().create(RetrofitApi.class).loadMoney(json);
    }

    public retrofit2.Call<RechargeOrderList> getRechargeOrderList() {
        return Retrofit().create(RetrofitApi.class).getRechargeOrderList(new ConfigUtil().getUserid());
    }

    public retrofit2.Call<BusinessNoteList> getEtcBusinessNote() {
        return Retrofit().create(RetrofitApi.class).getEtcBusinessNote();
    }

    /**
     * 圈存确认
     *
     * @param cardNo     20位鲁通卡卡号
     * @param chargeLsh  圈存流水号（同圈存申请时的应答2402流水号RChargeLsh）
     * @param storeMoney 圈存后卡余额
     * @param chargeFlag 圈存结果--0：成功 其他：失败
     * @param tac        从与写卡设备交互后的数据获取（16机制串）
     * @param cardTran   从与写卡设备交互后的数据获取（16进制串）
     * @param realMoney  实际写卡金额（单位：分）  实际写卡金额=圈存金额+调整金额
     * @param writeTime  写卡时间格式：YYYY-MM-DD HH:MI:SS
     * @return
     */
    public retrofit2.Call<CircleSave> sureLoadMoney(String cardNo, String chargeLsh, String storeMoney,
                                                    String chargeFlag, String tac, String cardTran,
                                                    String realMoney, String writeTime) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("cardNo", cardNo);
        params.put("chargeLsh", chargeLsh);
        params.put("storeMoney", storeMoney);
        params.put("chargeFlag", chargeFlag);
        params.put("tac", tac);
        params.put("cardTran", cardTran);
        params.put("realMoney", realMoney);
        params.put("writeTime", writeTime);
        params.put("userCode", new ConfigUtil().getUserid());

        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));
        return Retrofit().create(RetrofitApi.class).sureLoadMoney(json);
    }

    /**
     * ETC卡列表
     */
    public retrofit2.Call<CardList> getCardList(String cardType, String carNo) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("cardType", cardType);
        params.put("carNo", carNo);
        params.put("userCode", new ConfigUtil().getUserid());

        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));
        return Retrofit().create(RetrofitApi.class).getCardList(json);
    }

    /**
     * 车牌列表
     */
    public retrofit2.Call<CarNumberList> getCarNumberList() {
        return Retrofit().create(RetrofitApi.class).getCarNumberList(new ConfigUtil().getUserid());
    }

    /**
     * 获取卡信息
     *
     * @param storeMoney 卡内余额
     * @return
     */
    public retrofit2.Call<CardInfo> getCardInfo(String cardNo, String storeMoney) {
        return Retrofit().create(RetrofitApi.class).getCardInfo(cardNo, storeMoney);
    }

    /**
     * 齐鲁交通充值
     */
    public retrofit2.Call<StatusBean> payMoney(String cardNo, String money) {

        String yyyymmddhhmmss = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(System.currentTimeMillis());

        HashMap<String, Object> params = new HashMap<>();
        params.put("cardNo", cardNo);
        params.put("payTime", yyyymmddhhmmss);
        params.put("money", money);
        params.put("userCode", new ConfigUtil().getUserid());

        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));

        return Retrofit().create(RetrofitApi.class).payMoney(json);
    }

    /**
     * 齐鲁交通充值确认
     */
    public retrofit2.Call<StatusBean> surePayMoney(String cardNo, String money) {

        long time = System.currentTimeMillis();

        HashMap<String, Object> params = new HashMap<>();
        params.put("cardNo", cardNo);
        params.put("payTime", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(time));
        params.put("money", money);
        params.put("userCode", new ConfigUtil().getUserid());
        params.put("payType", "18");
        params.put("payFlag", "0");
        params.put("tradeno", time + "");

        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));

        return Retrofit().create(RetrofitApi.class).surePayMoney(json);
    }

    /**
     * Banner数据
     */
    public retrofit2.Call<Banner> getBannerList() {
        return Retrofit().create(RetrofitApi.class).getBannerList();
    }

    /**
     * 我的车辆-删除
     */
    public retrofit2.Call<Check> vehicleDelete(String vinShort) {
        return Retrofit().create(RetrofitApi.class).vehicleDelete(vinShort, new ConfigUtil().getUserid());
    }

    /**
     * 删除评论
     */
    public retrofit2.Call<Check> removeOpinion(String id) {
        return Retrofit().create(RetrofitApi.class).removeOpinion(id);
    }

    /**
     * 互动详情
     */
    public retrofit2.Call<InteractiveDetail> getSingleDetail(String messageId) {
        return Retrofit().create(RetrofitApi.class).getSingleDetail(messageId);
    }

    /**
     * 删除卡友圈动态
     */
    public retrofit2.Call<Check> removeMessage(int id) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("id", id);
        params.put("userId", new ConfigUtil().getUserid());
        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));

        return Retrofit().create(RetrofitApi.class).removeMessage(json);
    }

    public retrofit2.Call<PathRecord> pathRecord(String vin, String startdate, String enddate) {

        HashMap<String, Object> params = new HashMap<>();
        params.put("vin", vin);
        params.put("startTime", startdate);
        params.put("endTime", enddate);
        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));

        return Retrofit().create(RetrofitApi.class).pathRecord(json);
    }

    public retrofit2.Call<Check> vehicleAdd(String carNumber,
                                            String vinLong,
                                            String scanFlag,
                                            String carType,
                                            String carOwner,
                                            String address,
                                            String engineNumber,
                                            String registrationDate,
                                            String carLicenceDate) {

        HashMap<String, Object> params = new HashMap<>();

        if (scanFlag.equals("1")) {
            params.put("carType", carType);
            params.put("carOwner", carOwner);
            params.put("address", address);
            params.put("engineNumber", engineNumber);
            params.put("registrationDate", registrationDate);
            params.put("carLicenceDate", carLicenceDate);
        }

        params.put("userCode", new ConfigUtil().getUserid());
        params.put("carNumber", carNumber);
        params.put("vinLong", vinLong);
        params.put("vinShort", vinLong.substring(vinLong.length() - 8));

        params.put("scanFlag", scanFlag);

        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));

        return Retrofit().create(RetrofitApi.class).vehicleAdd(json);
    }

    public retrofit2.Call<Location> location() {

        HashMap<String, String> params = new HashMap<>();
        params.put("corp", getDefaultUserId());

        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));

        return Retrofit().create(RetrofitApi.class).location(json);
    }

    public retrofit2.Call<LocationDetail> locationDetail(String vin) {
        HashMap<String, String> params = new HashMap<>();
        params.put("corp", getDefaultUserId());
        params.put("vin", vin);

        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));

        return Retrofit().create(RetrofitApi.class).locationDetail(json);
    }

    public retrofit2.Call<TallyBook> tallybook(String queryYear, String queryMonth) {
        HashMap<String, String> params = new HashMap<>();
        params.put("createUser", new ConfigUtil().getUserid());
        params.put("queryYear", queryYear);
        params.put("queryMonth", queryMonth);

        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));

        return Retrofit().create(RetrofitApi.class).tallybook(json);
    }

    public retrofit2.Call<Map<String, Object>> add(Long createTimeStamp, String amount, String recordType, String costType, String remark) {
        HashMap<String, String> params = new HashMap<>();
        params.put("createTimeStamp", createTimeStamp.toString());
        params.put("amount", amount);
        params.put("recordType", recordType);
        params.put("costType", costType);
        params.put("remark", remark);
        params.put("createUser", new ConfigUtil().getUserid());

        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));

        return Retrofit().create(RetrofitApi.class).add(json);
    }

    public retrofit2.Call<Check> update(String gender, String nickName, String realName, String clientSign, String inviteCode) {
        Map<String, String> params = new HashMap<>();

        params.put("userCode", new ConfigUtil().getUserid());
        params.put("clientSign", clientSign);
        params.put("gender", gender);
        params.put("nickName", nickName);
        params.put("realName", realName);
        params.put("inviteCode", inviteCode);

        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));

        return Retrofit().create(RetrofitApi.class).update(json);
    }

    public retrofit2.Call<Message> messages(String position, String pageNo, String pageSize) {
        Map<String, String> params = new HashMap<>();
        params.put("position", position);
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);

        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));

        return Retrofit().create(RetrofitApi.class).messages(json);
    }

    public retrofit2.Call<TruckFriend> driverCircle(int pageNum, int pageSize) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("pageNum", pageNum);
        params.put("pageSize", pageSize);

        RequestBody json = RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(params));

        return Retrofit().create(RetrofitApi.class).driverCircle(json);
    }

    public retrofit2.Call<Area> getArea() {
        HashMap<String, Object> params = new HashMap<>();
        params.put("level", 1);
        return Retrofit().create(RetrofitApi.class).area(RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(params)));
    }

    public retrofit2.Call<ServiceStation> getServiceStation(String queryType, String area, String stationType, int pageNo, int pageSize) {
        HashMap<String, Object> params = new HashMap<>();
        params.put("queryType", queryType);
        String a = "";
        if (area != null) {
            if (area.contains("省")) {
                a = area.replace("省", "");
            } else if (area.contains("市")) {
                a = area.replace("市", "");
            } else {
                a = area;
            }
        }
        params.put("area", a);
        if (AppPresenterKt.CURRENT_LOCATION != null) {
            params.put("longitude", String.valueOf(AppPresenterKt.CURRENT_LOCATION.getLongitude()));
            params.put("latitude", String.valueOf(AppPresenterKt.CURRENT_LOCATION.getLatitude()));
        } else {
            params.put("longitude", "0.0");
            params.put("latitude", "0.0");
        }
        params.put("stationType", stationType);
        params.put("pageNo", pageNo);
        params.put("pageSize", pageSize);
        return Retrofit().create(RetrofitApi.class).serviceStation(RequestBody.create(MediaType.parse("application/json"), new Gson().toJson(params)));
    }

    public retrofit2.Call<Check> sendOpinion(int circleMessageId, int commentUserId, String comment) {
        HashMap<String, Object> params = new HashMap<>(4);
        params.put("circleMessageId", circleMessageId);
        params.put("commentUserId", commentUserId);
        params.put("userId", new ConfigUtil().getUserid());
        params.put("comment", comment);

        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));

        return Retrofit().create(RetrofitApi.class).sendOpinion(json);
    }

    public retrofit2.Call<Check> like(int circleMessageId, int laudUserId) {
        HashMap<String, Object> params = new HashMap<>(3);
        params.put("circleMessageId", circleMessageId);
        params.put("laudUserId", laudUserId);
        params.put("userId", new ConfigUtil().getUserid());

        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));

        return Retrofit().create(RetrofitApi.class).like(json);
    }

    public retrofit2.Call<NoReadInfo> noReadInfo() {
        return Retrofit().create(RetrofitApi.class).noReadInfo(new ConfigUtil().getUserid());
    }

    public retrofit2.Call<IsNotReadInfo> isNotReadInfo() {
        return Retrofit().create(RetrofitApi.class).isNotReadInfo(new ConfigUtil().getUserid());
    }

    public retrofit2.Call<Check> removeLike(int circleMessageId) {
        HashMap<String, Object> params = new HashMap<>(2);
        params.put("circleMessageId", circleMessageId);
        params.put("userId", new ConfigUtil().getUserid());

        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));

        return Retrofit().create(RetrofitApi.class).removeLike(json);
    }

    public retrofit2.Call<Check> remove(int id) {
        return Retrofit().create(RetrofitApi.class).remove(id);
    }

    public retrofit2.Call<Fault> faultDiagnose(String vin) {
        return Retrofit().create(RetrofitApi.class).faultDiagnose(vin);
    }

    public retrofit2.Call<Check> verificationCode(String phone) {
        return Retrofit().create(RetrofitApi.class).verificationCode(phone);
    }


    public retrofit2.Call<Check> modifyAccount(String phone, String checkCode, String password) {
        HashMap<String, String> params = new HashMap<>(4);
        params.put("userCode", new ConfigUtil().getUserid());
        params.put("phone", phone);
        params.put("checkCode", checkCode);
        params.put("password", password);

        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));

        return Retrofit().create(RetrofitApi.class).modifyAccount(json);
    }

    /**
     * 登录
     */
    public retrofit2.Call<User> login(String userName, String password) {
        HashMap<String, String> params = new HashMap<>(2);
        params.put("userName", userName);
        params.put("password", password);

        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));
        return Retrofit(NetField.SITE).create(RetrofitApi.class).login(json);
    }

    /**
     * 获取版本信息
     * source 1：苹果 2：安卓
     */
    public retrofit2.Call<VersionInfoBean> getVersionInfo() {
        HashMap<String, String> params = new HashMap<>(7);
        params.put("source", "2");
        params.put("method", "clgg.com.version.findMaxVersion");
        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(sign(params)));

        return Retrofit(NetField.CLGG_SITE).create(RetrofitApi.class).versionInfo(json);
    }

    /**
     * 验证码校验
     */
    public retrofit2.Call<StatusBean> authCodeCheck(String phone, String code) {
        return Retrofit(NetField.SSO_SITE).create(RetrofitApi.class).authCodeCheck(phone, code);
    }

    /**
     * 发送验证码
     */
    public retrofit2.Call<StatusBean> sendVerificationCode(String phone) {
        return Retrofit(NetField.SSO_SITE).create(RetrofitApi.class).sendCheckCode(phone);
    }

    /**
     * 修改密码
     */
    public retrofit2.Call<StatusBean> modifyPassword(String userName, String password, String newPassword) {
        Map<String, String> params = new HashMap<>();
        params.put("userName", userName);
        params.put("password", password);
        params.put("newPassword", newPassword);
        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));
        return Retrofit(NetField.SSO_SITE).create(RetrofitApi.class).modifyPassword(json);
    }

    /**
     * 注册
     */
    public retrofit2.Call<StatusBean> register(String userName, String password, String checkCode, String inviteUserCode) {
        Map<String, String> params = new HashMap<>();
        params.put("userName", userName);
        params.put("password", password);
        params.put("checkCode", checkCode);
        params.put("inviteUserCode", inviteUserCode);
        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));
        return Retrofit(NetField.SITE).create(RetrofitApi.class).appRegister(json);
    }

    /**
     * 重置密码
     */
    public retrofit2.Call<StatusBean> resetPassword(String userName, String password) {
        Map<String, String> params = new HashMap<>();
        params.put("userName", userName);
        params.put("password", password);
        RequestBody json = RequestBody.create(MediaType.parse("application/json;charset=UTF-8"), new Gson().toJson(params));
        return Retrofit(NetField.SSO_SITE).create(RetrofitApi.class).resetPassword(json);
    }

    public retrofit2.Call<PersonalData> personalData() {
        return Retrofit().create(RetrofitApi.class).personalData(new ConfigUtil().getUserid());
    }

    public retrofit2.Call<Consumption> oilsteams(String date) {
        return Retrofit().create(RetrofitApi.class).oilsteams(getDefaultUserId(), date);
    }

    public retrofit2.Call<Mileage> mileage(String date) {
        return Retrofit().create(RetrofitApi.class).mileage(getDefaultUserId(), date);
    }

    public retrofit2.Call<MileageDetail> mileageDetail(String date, String vin) {
        return Retrofit().create(RetrofitApi.class).mileageDetail(getDefaultUserId(), date, vin);
    }

    private String getDefaultUserId() {
        return "".equals(new ConfigUtil().getUserid()) ? ConstantValue.TEST_ACCOUNT : new ConfigUtil().getUserid();
    }

    public retrofit2.Call<Check> upload(File file) {
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase(Locale.getDefault());
        String fileName = file.getName();

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part p2 = MultipartBody.Part.createFormData("files",
                uuid + fileName.substring(fileName.indexOf("."), fileName.length()), requestFile);

        List<MultipartBody.Part> parts = new ArrayList<>();
        parts.add(p2);
        parts.add(MultipartBody.Part.createFormData("userCode", new ConfigUtil().getUserid()));

        return Retrofit().create(RetrofitApi.class).upload(parts);
    }

    public retrofit2.Call<ConsumptionDetail> oilsteamsDetail(String vin, String date) {
        return Retrofit().create(RetrofitApi.class).oilsteamsDetail(vin, date);
    }

    public retrofit2.Call<Map<String, Object>> scan(File file) {
        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase(Locale.getDefault());
        String fileName = file.getName();

        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part p2 = MultipartBody.Part.createFormData("files",
                uuid + fileName.substring(fileName.indexOf("."), fileName.length()), requestFile);

        List<MultipartBody.Part> parts = new ArrayList<>();
        parts.add(p2);
        parts.add(MultipartBody.Part.createFormData("userCode", new ConfigUtil().getUserid()));
        return Retrofit().create(RetrofitApi.class).scan(parts);
    }

    public retrofit2.Call<Check> feedBack(List<String> files, String content) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        parts.add(MultipartBody.Part.createFormData("userId", new ConfigUtil().getUserid()));
        parts.add(MultipartBody.Part.createFormData("phone", new ConfigUtil().getMobile()));
        parts.add(MultipartBody.Part.createFormData("content", content));

        for (String s : files) {
            File file = new File(s);
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase(Locale.getDefault());
            String fileName = file.getName();
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part p = MultipartBody.Part.createFormData("images", uuid + fileName.substring(fileName.indexOf("."), fileName.length()), requestFile);
            parts.add(p);
        }
        return Retrofit().create(RetrofitApi.class).feedBack(parts);
    }

    public retrofit2.Call<Check> publishDynamic(List<String> files, String message) {
        List<MultipartBody.Part> parts = new ArrayList<>();
        parts.add(MultipartBody.Part.createFormData("userId", new ConfigUtil().getUserid()));
        parts.add(MultipartBody.Part.createFormData("type", "0"));
        parts.add(MultipartBody.Part.createFormData("message", message));

        for (String s : files) {
            File file = new File(s);
            String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase(Locale.getDefault());
            String fileName = file.getName();
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            MultipartBody.Part p = MultipartBody.Part.createFormData("images", uuid + fileName.substring(fileName.indexOf("."), fileName.length()), requestFile);
            parts.add(p);
        }

        return Retrofit().create(RetrofitApi.class).publishDynamic(parts);
    }

    public class HttpLogger implements HttpLoggingInterceptor.Logger {
        @Override
        public void log(String message) {
            LogHelper.v("logcat", message);
        }

    }
}
