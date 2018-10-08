package com.sc.clgg.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;

import com.sc.clgg.application.App;
import com.sc.clgg.config.ConstantValue;

public class Tools {
    public static void callPhone(String telphone, Activity activity) {
        Uri uri = Uri.parse("tel:" + telphone);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        activity.startActivity(intent);
    }

    public static void getScreenInfo(Context context) {
        ConstantValue.SCREEN_HEIGHT = context.getResources().getDisplayMetrics().heightPixels;
        ConstantValue.SCREEN_WIDHT = context.getResources().getDisplayMetrics().widthPixels;
    }

    public static void Toast(String content) {
        if (!TextUtils.isEmpty(content)) {
            Toast.makeText(App.getInstance(), content, Toast.LENGTH_SHORT).show();
        }
    }

}
