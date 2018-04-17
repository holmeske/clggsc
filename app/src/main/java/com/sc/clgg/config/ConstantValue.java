package com.sc.clgg.config;

import android.os.Environment;

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
}
