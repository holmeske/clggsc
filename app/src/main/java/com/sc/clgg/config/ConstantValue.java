package com.sc.clgg.config;

import android.os.Environment;

import com.sc.clgg.util.ConfigUtil;

import java.util.HashMap;

public class ConstantValue {
    /**
     * sdcard 路径
     */
    public static final String SDCARD_PATH = Environment.getExternalStorageDirectory().getPath() + "/CLGG";
    /**
     * 客服电话
     */
    public static final String SERVICE_TEL = "400-888-1122";
    /**
     * 测试账号
     */
    public static final String TEST_ACCOUNT = "12045";

    /**
     * 屏幕尺寸
     */
    public static int SCREEN_WIDHT = 480;
    public static int SCREEN_HEIGHT = 800;
    /**
     * 省列表
     */
    public static HashMap<String, String> REGIONS = new HashMap<>();
    /**
     * 城市列表
     */
    public static HashMap<String, String> CITIES = new HashMap<>();

    public static final String NO_VEHICLE_HEAD = "http://sj.clgg.com/Html/View/Main/Index.html?username=" + new ConfigUtil().getAccount()
            + "&password=" + new ConfigUtil().getPassword();

    public static final String REAL_NAME_AUTHENTICATION = NO_VEHICLE_HEAD + "&url=NameAuthentication/Index.html";
    public static final String WALLET_ENTRANCE = NO_VEHICLE_HEAD + "&url=MyWallet/Index.html";
    public static final String MEMBER_INFORMATION = NO_VEHICLE_HEAD + "&url=Membership/Index.html";
    public static final String OPERATING = NO_VEHICLE_HEAD + "&url=Operate/Index.html";

}
