package com.sc.clgg.base;

import com.sc.clgg.config.ConstantValue;
import com.sc.clgg.dialog.LoadingDialog;
import com.sc.clgg.tool.helper.LogHelper;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;
import pub.devrel.easypermissions.PermissionRequest;


/**
 * @author lvke
 * CreateDate：2017/8/23 16:42
 */

public class BaseAppCompatActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    private LoadingDialog dialog;

    protected void showProgressDialog() {
        show();
    }

    protected void showProgressDialog(boolean canceledOnTouchOutside) {
        show(canceledOnTouchOutside);
    }

    protected void showProgressDialog(String msg,boolean canceledOnTouchOutside) {
        show(msg);
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
    }
    protected void showProgressDialog(String msg) {
        show(msg);
    }


    private void show(String msg) {
        if (dialog == null) {
            LogHelper.e("创建dialog");
            dialog = new LoadingDialog(this);
            dialog.show();
            dialog.setContent(msg);
        } else {
            LogHelper.e("复用dialog");
            if (!dialog.isShowing()) {
                dialog.show();
                dialog.setContent(msg);
            }
        }
    }

    private void show(boolean canceledOnTouchOutside) {
        if (dialog == null) {
            LogHelper.e("创建dialog");
            dialog = new LoadingDialog(this);
            dialog.show();
        } else {
            LogHelper.e("复用dialog");
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }
        dialog.setCanceledOnTouchOutside(canceledOnTouchOutside);
    }

    private void show() {
        if (dialog == null) {
            dialog = new LoadingDialog(this);
            dialog.show();
        } else {
            if (!dialog.isShowing()) {
                dialog.show();
            }
        }
    }

    protected void hideProgressDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideProgressDialog();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogHelper.e("permissionssss", "onResume()");
        requestPermissions();
    }

    @AfterPermissionGranted(ConstantValue.RC_CAMERA_AND_LOCATION)
    private void requestPermissions() {
        LogHelper.e("permissionssss", "requestPermissions()");
        if (!EasyPermissions.hasPermissions(this, ConstantValue.PERMISSION_NEED)) {
            LogHelper.e("permissionssss", "hasPermissions()");
            // Do not have permissions, request them now
            //这个方法是用户在拒绝权限之后，再次申请权限，才会弹出自定义的dialog，详情可以查看下源码 shouldShowRequestPermissionRationale()方法

            EasyPermissions.requestPermissions(
                    new PermissionRequest.Builder(this, ConstantValue.RC_CAMERA_AND_LOCATION, ConstantValue.PERMISSION_NEED)
                            .setRationale("申请权限")
                            .setPositiveButtonText("确认")
                            .setNegativeButtonText("取消")
                            .build());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogHelper.e("permissionssss", "onRequestPermissionsResult()");
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        LogHelper.e("permissionssss", "onPermissionsGranted()");
    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        LogHelper.e("permissionssss", "onPermissionsDenied()");

        //如果用户点击永远禁止，这个时候就需要跳到系统设置页面去手动打开了
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            LogHelper.e("permissionssss", "somePermissionPermanentlyDenied()");

            new AppSettingsDialog.Builder(this)
                    .setTitle("权限申请")
                    .setRationale("重要权限被禁止，无法正常使用。打开应用设置页面修改权限？")
                    .setPositiveButton("确认")
                    .setNegativeButton("取消")
                    .build()
                    .show();
        }
    }

    /*private boolean is(AppCompatActivity activity) {
        List<Fragment> fragments = activity.getSupportFragmentManager().getFragments();
        for (Fragment f : fragments) {
            if (f instanceof AppCompatDialogFragment) {
                return true;
            }
        }
        return false;
    }*/
}

