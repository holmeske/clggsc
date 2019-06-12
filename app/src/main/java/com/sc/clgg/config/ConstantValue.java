package com.sc.clgg.config;

import android.Manifest;

import com.sc.clgg.util.ConfigUtil;

/**
 * @author lvke
 */
public class ConstantValue {
    public static final String[] PERMISSION_NEED = new String[]{
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_PHONE_STATE};

    /**
     * 客服电话
     */
    public static final String SERVICE_TEL = "400-888-1122";
    /**
     * 测试账号
     */
    public static final String TEST_ACCOUNT = "12045";
    /**
     * WX_APP_KEY = "a5404367a2d24624fe3e566015f4715b"
     */
    public static final String WX_APP_ID = "wxd9d46485cf9c6a3b";
    public static final String WX_PARTNER_ID = "1514756421";
    public static final int RC_CAMERA_AND_LOCATION = 0;
    public static final String MORE_CAR = NetField.SITE + "shop/vehicleShowList/3";
    public static final String CAR_HEAVY = NetField.SITE + "shop/vehicleShowList/0";
    public static final String CAR_MEDIUM = NetField.SITE + "shop/vehicleShowList/1";
    public static final String CAR_NEW = NetField.SITE + "shop/vehicleShowList/2";
    public static final String TIRE = NetField.SITE + "shop/tire";
    public static final String LUBE = NetField.SITE + "shop/lubricant";
    public static final String LEASE = NetField.SITE + "shop/lease";
    public static final String FACTORING = NetField.SITE + "shop/premiums";
    public static final String INSURANCE = NetField.SITE + "shop/mx";
    public static final String USER_AGREEMENT = NetField.SITE + "preview/cardB";

    private static String NO_VEHICLE_HEAD = "http://sj.clgg.com:8004/Html/View/Main/Index.html?username="
            + new ConfigUtil().getAccount() + "&password=" + new ConfigUtil().getPassword();
    public static final String REAL_NAME_AUTHENTICATION = NO_VEHICLE_HEAD + "&url=NameAuthentication/Index.html";
    public static final String WALLET_ENTRANCE = NO_VEHICLE_HEAD + "&url=MyWallet/Index.html";
    public static final String MEMBER_INFORMATION = NO_VEHICLE_HEAD + "&url=Membership/Index.html";
    public static final String OPERATING = NO_VEHICLE_HEAD + "&url=Operate/Index.html";


}
