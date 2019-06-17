package com.sc.clgg.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.text.TextUtils;
import android.widget.Toast;

import com.sc.clgg.application.App;

/**
 * @author lvke
 */
public class Tools {

    public static void callPhone(String telphone, Activity activity) {
        Uri uri = Uri.parse("tel:" + telphone);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        activity.startActivity(intent);
    }

    public static void Toast(String content) {
        if (!TextUtils.isEmpty(content)) {
            Toast.makeText(App.app, content, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 当前ip地址
     *
     * @return String ip
     */
    public static String getIpAddress(Context context) {
        try {
            WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
            assert wifiManager != null;
            WifiInfo wifiInfo = wifiManager.getConnectionInfo();
            int ipAddress = wifiInfo.getIpAddress();
            return defaultIp((ipAddress & 0xFF) + "." +
                    ((ipAddress >> 8) & 0xFF) + "." +
                    ((ipAddress >> 16) & 0xFF) + "." +
                    (ipAddress >> 24 & 0xFF));
        } catch (Exception ignored) {

        }
        return defaultIp("");
    }

    private static String defaultIp(String ip) {
        if ("".equals(ip) || ip == null) {
            return "0.0.0.0";
        } else {
            return ip;
        }
    }
}
