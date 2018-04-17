//package com.sc.clgg.activity.basic;
//
//import android.Manifest;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//
//import com.sc.clgg.R;
//import com.sc.clgg.util.ConfigUtil;
//import com.yanzhenjie.permission.AndPermission;
//import com.yanzhenjie.permission.PermissionListener;
//import com.yanzhenjie.permission.Rationale;
//import com.yanzhenjie.permission.RationaleListener;
//
//import java.util.List;
//
///**
// * @author lvke
// */
//public class LaunchActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        AndPermission.with(this)
//                .requestCode(100)
//                .permission(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE,
//                        Manifest.permission.ACCESS_COARSE_LOCATION)
//                .rationale(new RationaleListener() {
//                    @Override
//                    public void showRequestPermissionRationale(int requestCode, final Rationale rationale) {
//                        // 此对话框可以自定义，调用rationale.resume()就可以继续申请。
//                        AndPermission.rationaleDialog(LaunchActivity.this, rationale).show();
//                    }
//                })
//                .callback(new PermissionListener() {
//                    @Override
//                    public void onSucceed(int requestCode, @NonNull List<String> grantPermissions) {
////                        String path = Environment.getExternalStorageDirectory().getPath() + "/clggsc/apk/";
////                        String apk = Environment.getExternalStorageDirectory().getPath() + "/clggsc/apk/" + "clgg" + ".apk";
////                        File file = new File(path);
////                        LogUtils.e("" + file.exists());
////                        LogUtils.e("" + file.isDirectory());
////
////                        long start = System.currentTimeMillis();
////                        LogUtils.e("start = " + start);
////                        if (!file.exists()) {
////                            if (file.mkdirs()) {
////                                long end = System.currentTimeMillis();
////                                LogUtils.e("end = " + end);
////                                LogUtils.e((end - start) + "");
////                            }
////                        }
////                        BigDecimalHelper.log(App.getInstance());
//                        init();
//                    }
//
//                    @Override
//                    public void onFailed(int requestCode, @NonNull List<String> deniedPermissions) {
//                        // 是否有不再提示并拒绝的权限。
//                        if (AndPermission.hasAlwaysDeniedPermission(LaunchActivity.this, deniedPermissions)) {
//                            // 第一种：用AndPermission默认的提示语。
//                            AndPermission.defaultSettingDialog(LaunchActivity.this, 100).show();
//
//                            /*// 第二种：用自定义的提示语。
//                            AndPermission.defaultSettingDialog(LaunchActivity.this, 100)
//                                    .setTitle("权限申请失败")
//                                    .setMessage("您拒绝了我们必要的一些权限，已经没法愉快的玩耍了，请在设置中授权！")
//                                    .setPositiveButton("好，去设置")
//                                    .show();
//
//                            // 第三种：自定义dialog样式。
//                            SettingService settingService = AndPermission.defineSettingDialog(LaunchActivity.this, 100);
//
//                            // 你的dialog点击了确定调用：
//                            settingService.execute();
//                            // 你的dialog点击了取消调用：
//                            settingService.cancel();*/
//                        }
//                    }
//                })
//                .start();
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        switch (requestCode) {
//            case 100: { // 这个100就是你上面传入的数字。
//                // 你可以在这里检查你需要的权限是否被允许，并做相应的操作。
//                if (AndPermission.hasPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.READ_PHONE_STATE)) {
//                    init();
//                }
//                break;
//            }
//        }
//    }
//
//    private void init() {
//        if (!TextUtils.isEmpty(new ConfigUtil().getUserid())) {
//            startActivity(new Intent(this, MainActivity.class));
//        } else {
//            Intent intent = new Intent(this, LoginActivity.class);
//            startActivity(intent);
//        }
//        overridePendingTransition(R.anim.scale_in, R.anim.alpha_out);
//        finish();
//    }
//
//}
