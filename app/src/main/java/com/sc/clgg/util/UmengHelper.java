package com.sc.clgg.util;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.sc.clgg.BuildConfig;
import com.sc.clgg.R;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareConfig;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * @author：lvke
 * @date：2018/6/19 15:39
 */
public class UmengHelper {


    public void init(Context context) {
        UMConfigure.init(context, "5b28b84ba40fa341c8000093", "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        PlatformConfig.setWeixin("wxd9d46485cf9c6a3b", "a5404367a2d24624fe3e566015f4715b");
//        PlatformConfig.setQQZone("1106985514", "6n1uH0NFjrAHcUsl");
        PlatformConfig.setQQZone("1105755915", "onOnQlludHdU0Q4g");
        UMConfigure.setLogEnabled(BuildConfig.LOG_DEBUG);
    }


    public void share(Activity activity, SHARE_MEDIA var1, String url, String title, String description, UMShareListener listener) {

        if (!UMShareAPI.get(activity).isInstall(activity, var1)) {
            Toast.makeText(activity, "没有安装该应用,无法分享", Toast.LENGTH_SHORT).show();
            return;
        }
        UMShareConfig config = new UMShareConfig();
        config.isNeedAuthOnGetUserInfo(true);
        UMShareAPI.get(activity).setShareConfig(config);

        UMImage image = new UMImage(activity, R.drawable.ic_launcher);//资源文件

        UMWeb web = new UMWeb(url);
        web.setTitle(title);//标题
        web.setThumb(image);  //缩略图
        web.setDescription(description);//描述

        new ShareAction(activity).setPlatform(var1).withMedia(web).setCallback(listener).share();
    }

    public void destroy(Activity activity){
        UMShareAPI.get(activity).release();
    }
}
