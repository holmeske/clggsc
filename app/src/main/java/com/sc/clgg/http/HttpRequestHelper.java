package com.sc.clgg.http;

import android.text.TextUtils;

import com.clgg.api.contract.ClggApiException;
import com.clgg.api.contract.ClggConstants;
import com.clgg.api.signature.ClggSignature;
import com.sc.clgg.application.App;
import com.sc.clgg.config.Method;
import com.sc.clgg.config.NetField;
import com.sc.clgg.util.ConfigUtil;

import java.util.HashMap;
import java.util.Map;

import tool.helper.TextHelper;

/**
 * 作者：lvke
 * 创建时间：2017/6/27
 */

public class HttpRequestHelper {

    public static void historicalRoute(String carno, String startdate, String enddate, HttpCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("carno", carno);
        params.put("corp", new ConfigUtil().getUserid());
        params.put("startdate", startdate);
        params.put("enddate", enddate);
        new HttpManager().post(NetField.SITE + "txj/vehicleReport/getVehicleGpsReport", params, callback);
    }

    //油耗
    public static void oilWear(String carno, String year, String month, HttpCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("carno", carno);// 车牌号
        params.put("corp", new ConfigUtil().getUserid());
        params.put("year", year);
        params.put("month", month);
        new HttpManager().post(NetField.SITE + "/txj/vehicleReport/getVehicleMileageAndOilCostReport", params, callback);
    }

    //里程
    public static void mileage(String carno, String year, String month, HttpCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("carno", carno);// 车牌号
        params.put("corp", new ConfigUtil().getUserid());
        params.put("year", year);
        params.put("month", month);
        new HttpManager().post(NetField.SITE + "/txj/vehicleReport/getVehicleMileageAndOilCostReport", params, callback);
    }

    //记账本首页列表--明细
    public static void tallybookListDetail(String waybillNo, HttpCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        new HttpManager().get(NetField.SITE + "account/waybillCostDetail/" + waybillNo, params, callback);
    }

    //记一笔
    public static void rememberA(int businessType, String costType, String money, String waybillNo, String createdTm, HttpCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("businessType", String.valueOf(businessType));//收入支出类型，1：收入，2：支出,
        params.put("costType", costType);//类型编码
        params.put("money", money);
        params.put("createdCd", new ConfigUtil().getUserid());//司机ID
        params.put("waybillNo", waybillNo);
        params.put("createdTm", createdTm);
        new HttpManager().post(NetField.SITE + "account/insertAccount", params, callback);
    }

    //记账本首页
    public static void tallyBookHome(String driverId, String year, String month, String startDate, String endDate, HttpCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("driverId", driverId);
        params.put("year", year);
        params.put("month", month);
        if (!TextUtils.isEmpty(startDate)) {
            params.put("startDate", startDate);//选填项、记账本首页运单筛选条件，开始时间，如：2017-10-10
        }
        if (!TextUtils.isEmpty(endDate)) {
            params.put("endDate", endDate);//选填项、记账本首页运单筛选条件，结束时间，如：2017-10-11
        }
        new HttpManager().post(NetField.SITE + "account/feeIndex", params, callback);
    }

    //车辆监控 加carno 代表一辆车的详情
    public static void getVehicleLocation(HttpCallBack callback, String corp, String... carno) {
        HashMap<String, String> params = new HashMap<>();
        params.put("corp", corp);
        if (carno != null && carno.length != 0) {
            params.put("carno", carno[0]);
        }
        new HttpManager().post(NetField.SITE + "txj/monitor/getVehicleLocation", params, callback);
    }

    //发布车辆
    public static void releaseVehicle(String frameNumber, String vehicleNo, String vehicleType, String vehicleLength, String loadCapacity, HttpCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("corp", new ConfigUtil().getUserid());// 登陆用户Id
        params.put("frameNumber", frameNumber);
        params.put("vehicleNo", vehicleNo);
        params.put("vehicleType", vehicleType);
        params.put("vehicleLength", vehicleLength);
        params.put("loadCapacity", loadCapacity);
        new HttpManager().post(NetField.SITE + "vehicle/addAppVehicle", params, callback);
    }

    //删除车辆
    public static void deleteVehicle(String frameNumber, HttpCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("frameNumber", frameNumber);
        new HttpManager().post(NetField.SITE + "vehicle/deleteAppVehicle", params, callback);
    }

    //我的车辆
    public static void myVehicle(String carno, String date, HttpCallBack callback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("carno", carno);
        params.put("corp", new ConfigUtil().getUserid());
        params.put("date", date);
        new HttpManager().post("myVehicle",NetField.SITE + "txj/vehicleReport/getVehicleReportDetails", params, callback);
    }

    /**
     * 获取车辆长度集合
     */
    public static void getVehicleLength(HttpCallBack callback) {
        HashMap<String, String> params = new HashMap<>(1);
        params.put("codeId", "VEHICLE_LENGTH");
        new HttpManager().post(NetField.SITE + "code/getCode", params, callback);
    }

    public static void getMyTeam(String corp, String date, HttpCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("corp", corp);
        params.put("date", date);
        new HttpManager().post(NetField.SITE + Method.VEHICLE_REPORT_LIST, params, callback);
    }

    public static void getDataByType(HttpCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("codeId", "VEHICLE_TYPE");
        new HttpManager().post(NetField.SITE + "code/getCode", params, callback);
    }

    public static void getBusinessInfo(String userid, String storeid, HttpCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("method", Method.STORE_DETAIL);
        params.put("userid", userid);
        params.put("storeid", storeid);
        new HttpManager().post(NetField.CLGG_SITE, sign(params), callback);
    }

    public static void getMaintenanceData(String serviceid, int pageno, int pageSize, HttpCallBack callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("method", "clgg.backmarket.store.help.list");
        params.put("serviceid", serviceid);
        params.put("lat", String.valueOf(App.mLocationBean.getLatitude()));
        params.put("lng", String.valueOf(App.mLocationBean.getLongitude()));
        params.put("pageno", String.valueOf(pageno));
        params.put("pageSize", String.valueOf(pageSize));
        new HttpManager().post(NetField.CLGG_SITE, sign(params), callback);
    }

    //运单列表
    public static void getOrderList(String waybillStatus, String startDate, String endDate, HttpCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("id", new ConfigUtil().getUserid());
        if (!TextUtils.isEmpty(waybillStatus)) {
            params.put("waybillStatus", waybillStatus);
        }
        if (!TextUtils.isEmpty(startDate)) {
            params.put("startDate", startDate);
        }
        if (!TextUtils.isEmpty(endDate)) {
            params.put("endDate", endDate);
        }
        params.put("method", Method.TXJ_WAYBILL_SELECT_WAYBILL);
        new HttpManager().post(NetField.CLGG_SITE, sign(params), callback);
    }

    //收入列表
    public static void getIncomeList(int year, int month, HttpCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("driverId", new ConfigUtil().getUserid());
        params.put("year", String.valueOf(year));
        params.put("month", String.valueOf(month));
        new HttpManager().post(NetField.SITE + "account/incomeDetail", params, callback);
    }

    //运单列表
    public static void getWaybillList(String createdAtFrom, String createdAtTo, HttpCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("id", new ConfigUtil().getUserid());
        params.put("createdAtFrom", createdAtFrom);
        params.put("createdAtTo", createdAtTo);
        params.put("method", Method.TXJ_WAYBILL_SELECT_WAYBILL);
        new HttpManager().post(NetField.CLGG_SITE, sign(params), callback);
    }

    //记账本支出列表
    public static void expendList(int year, int month, HttpCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("createdCd", new ConfigUtil().getUserid());
        params.put("year", String.valueOf(year));
        params.put("month", TextHelper.instance.zeroPadding(month));
        params.put("businessType", "2");//1收入 2支出
        new HttpManager().post(NetField.SITE + "account/selectAccount", params, callback);
    }

    //记账本支出列表详情
    public static void expendDetail(int year, int month, String costType, HttpCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("driverId", new ConfigUtil().getUserid());
        params.put("year", String.valueOf(year));
        params.put("month", String.valueOf(month));
        params.put("costType", costType);
        new HttpManager().post(NetField.SITE + "account/expendDetail", params, callback);
    }

    //提货
    public static void pickUpGoods(String waybillNo, HttpCallBack callback) {
        final Map<String, String> params = new HashMap<>(8);
        params.put("waybillNo", waybillNo);
        params.put("method", Method.WAYBILL_SELECT_WAYBILL_TI);
        new HttpManager().post(NetField.CLGG_SITE, sign(params), callback);
    }

    //签收
    public static void signFor(String waybillNo, HttpCallBack callback) {
        final Map<String, String> params = new HashMap<>(8);
        params.put("waybillNo", waybillNo);
        params.put("method", Method.WAYBILL_SELECT_WAYBILL_SHOU);
        new HttpManager().post(NetField.CLGG_SITE, sign(params), callback);
    }

    //运输管理详情
    public static void getWaybillDetail(String waybillNo, String waybillStatus, HttpCallBack callback) {
        final Map<String, String> params = new HashMap<>(8);
        params.put("waybillNo", waybillNo);
        params.put("waybillStatus", waybillStatus);
        params.put("method", Method.WAYBILL_SELECT_WAYBILL_DETAIL);
        new HttpManager().post(NetField.CLGG_SITE, sign(params), callback);
    }

    public static void checkUpdate(HttpCallBack callback) {
        Map<String, String> params = new HashMap<>(7);
        params.put("source", "2"); // 终端来源 1：苹果 2：安卓
        params.put("method", Method.CHECK_VERSION_BUILD);
        new HttpManager().post(NetField.CLGG_SITE, sign(params), callback);
    }

    /*驾驶评分*/
    public static void drivingScore(String timeLine, HttpCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("corp", new ConfigUtil().getUserid());
        params.put("timeLine", timeLine);//01上周    02本周    03本月
        new HttpManager().post(NetField.SITE + "txj/driveReport/getDriveReportList", params, callback);
    }

    /*驾驶评分详情*/
    public static void drivingScoreDetail(String timeLine, String carno, HttpCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("corp", new ConfigUtil().getUserid());
        params.put("timeLine", timeLine);//01上周    02本周    03本月
        params.put("carno", carno);
        new HttpManager().post(NetField.SITE + Method.DRIVING_SCORE_DETAIL, params, callback);
    }

    public static void getUserInfo(String userName, String password, HttpCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("userName", userName);
        params.put("password", password);
        params.put("method", Method.USER_LOGIN_METHOD);
        new HttpManager().post(NetField.CLGG_SITE, sign(params), callback);
    }

    /*融合版注册接口*/
    public static void register(String userName, String password, String personalPhone, HttpCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("userName", userName);
        params.put("userType", "11");
        params.put("password", password);
        params.put("nickName", "");
        params.put("personalPhone", personalPhone);
        new HttpManager().post(NetField.SSO_SITE + "/user/appRegister", params, callback);
    }

    /*手机号码校验*/
    public static void phoneNumberCheck(String phone, HttpCallBack callback) {
        Map<String, String> params = new HashMap<>();
        new HttpManager().get(NetField.SSO_SITE + "/user/check/" + phone + "/1", params, callback);
    }

    /*发送验证码*/
    public static void sendVerificationCode(String phone, HttpCallBack callback) {
        Map<String, String> params = new HashMap<>();
        new HttpManager().get(NetField.SSO_SITE + "/user/sendCheckCode/" + phone, params, callback);
    }

    /*验证码校验*/
    public static void verificationCodeCheck(String phone, String code, HttpCallBack callback) {
        Map<String, String> params = new HashMap<>();
        new HttpManager().get(NetField.SSO_SITE + "/user/validateCode/" + phone + "/" + code, params, callback);
    }

    /*重置密码*/
    public static void resetPassword(String userName, String password, HttpCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("userName", userName);// 用户名
        params.put("password", password);// 用户密码
        new HttpManager().post(NetField.SSO_SITE + "/user/appResetPassword", params, callback);
    }

    /*修改密码*/
    public static void modifyPassword(String userName, String password, String newPassword, HttpCallBack callback) {
        Map<String, String> params = new HashMap<>();
        params.put("userName", userName);// 用户名
        params.put("password", password);// 用户密码
        params.put("newPassword", newPassword);//新密码
        new HttpManager().post(NetField.SSO_SITE + "/user/modifyPassword", params, callback);
    }

    private static Map<String, String> sign(Map<String, String> params) {
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
}
