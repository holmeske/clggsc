package com.sc.clgg.activity.basic;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;

import com.sc.clgg.R;
import com.sc.clgg.dialog.AlertDialogHelper;
import com.sc.clgg.tool.helper.LogHelper;
import com.yanzhenjie.permission.AndPermission;

/**
 * @author lvke
 */
public class LaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestPermission();
    }

    private void requestPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.CALL_PHONE)
                .onGranted(permissions -> {
                    LogHelper.e("权限同意");
                    init();
                })
                .onDenied(permissions -> {
                    LogHelper.e("权限拒绝");

                    new AlertDialogHelper().show(this,
                            "为了保证功能的正常使用，请前往设置-应用权限页面同意以下权限: \n\n\t\t存储\n\t\t定位\n\t\t电话\n\t\t读取本机识别码",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent();
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                                    startActivity(intent);

//                                    Intent mItent=new Intent(Settings.ACTION_APPLICATION_SETTINGS);
//                                    startActivity(mItent);
                                }
                            },
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    System.exit(0);
                                }
                            }, null);
                })
                .start();
    }

    private void init() {
        startActivity(new Intent(this, MainActivity.class));
        overridePendingTransition(R.anim.scale_in, R.anim.alpha_out);
        finish();
    }

}
