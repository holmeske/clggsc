package com.sc.clgg.activity.etc.ble;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.sc.clgg.R;
import com.sc.clgg.application.App;
import com.sc.clgg.bean.CircleSave;
import com.sc.clgg.etc.NewDES;
import com.sc.clgg.retrofit.RetrofitHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import etc.obu.data.CardInformation;
import etc.obu.data.DeviceInformation;
import etc.obu.data.ServiceStatus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BleActivity extends AppCompatActivity {
    private String tag = "logcat";
    private ServiceStatus mServiceStatus;
    private String KEY = "2D65d001246ade79151C634be75264AF";
    private String intRandom = "";
    private String intMac = "";
    private String newKey = "";
    private String bluetoothSn = "";
    private String a_cid = "", a_pt = "", a_rnd = "", a_cbb = "", a_m1 = "", a_on = "";
    private int RQcMoney = 0, RAdjust = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ble);
        RQcMoney = getIntent().getIntExtra("RQcMoney", 0);
        RAdjust = getIntent().getIntExtra("RAdjust", 0);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            // 没有权限，申请权限
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, 0);
        }

    }

    public void btnClick(View v) {
        switch (v.getId()) {
            case R.id.connect:
                new Thread(() -> {
                    Log.e(tag, "开始连接");
                    mServiceStatus = App.app.mObuInterface.connectDevice();
                    if (mServiceStatus.getServiceCode() == 0) {
                        Log.e(tag, "连接成功");
                    } else {
                        Log.e(tag, "连接失败");
                    }
                    Log.e(tag, "返回结果" + new Gson().toJson(mServiceStatus));
                }).start();
                break;

            case R.id.disconnect:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(tag, "返回结果" + new Gson().toJson(App.app.mObuInterface.disconnectDevice()));
                    }
                }).start();
                break;

            case R.id.getBluetoothSn:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DeviceInformation deviceInformation = App.app.mObuInterface.getDeviceInformation();
                        if (deviceInformation != null) {
                            bluetoothSn = deviceInformation.Sn;
                        }
                        Log.e(tag, "" + new Gson().toJson(deviceInformation));
                    }
                }).start();
                break;
            case R.id.read_and_circle:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        intRandom = "1234";
                        try {
                            intMac = NewDES.PBOC_3DES_MAC(intRandom, KEY).substring(0, 8);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        if (App.app.mObuInterface.intAuthDev(intRandom.length() / 2, intRandom, intMac) == 0) {
                            Log.e(tag, "认证成功");
                            CardInformation cardInfo = new CardInformation();
                            ServiceStatus status = App.app.mObuInterface.getCardInformation(cardInfo);
                            Log.e(tag, "读卡 = " + new Gson().toJson(status));

                            if (status.getServiceCode() == 0) {
                                Log.e(tag, "读卡成功 " + new Gson().toJson(cardInfo));

                                String pinCode = "";
                                if ("40".equals(cardInfo.getCardVersion())) {
                                    pinCode = "313233343536";
                                } else {
                                    pinCode = "123456";
                                }
                                ServiceStatus loadMac1Status = App.app.mObuInterface.loadCreditGetMac1(
                                        cardInfo.getCardId(),
                                        RQcMoney + RAdjust,
                                        "000000000000",
                                        pinCode,
                                        "02",
                                        "01");
                                String[] info = loadMac1Status.getServiceInfo().split("&");

                                /*a_cid   卡号（后16位）
                                a_pt     充值金额（单位分）
                                a_rnd    伪随机数
                                a_cbb   充值前余额（单位分）
                                a_m1    MAC1码
                                a_on     联机交易序号*/
                                for (String s : info) {
                                    Log.e(tag, "info元素 = " + s);
                                    if (s.startsWith("a_cid=")) {
                                        a_cid = s.substring(6, s.length());
                                    } else if (s.startsWith("a_pt=")) {
                                        a_pt = s.substring(5, s.length());
                                    } else if (s.startsWith("a_rnd=")) {
                                        a_rnd = s.substring(6, s.length());
                                    } else if (s.startsWith("a_cbb=")) {
                                        a_cbb = s.substring(6, s.length());
                                    } else if (s.startsWith("a_m1=")) {
                                        a_m1 = s.substring(5, s.length());
                                    } else if (s.startsWith("a_on=")) {
                                        a_on = s.substring(5, s.length());
                                    }
                                }
                                if (loadMac1Status.getServiceCode() == 0) {
                                    new RetrofitHelper().loadMoney(
                                            cardInfo.getCardId(),
                                            RQcMoney + "",
                                            RAdjust + "",
                                            a_m1,
                                            a_cbb, a_rnd, a_on, bluetoothSn).enqueue(new Callback<CircleSave>() {
                                        @Override
                                        public void onResponse(Call<CircleSave> call, Response<CircleSave> response) {
                                            if (response.isSuccessful() && response.body().getSuccess()) {

                                                String date = "";
                                                String timeStr = response.body().getRWriteTime();
                                                if (!TextUtils.isEmpty(timeStr)) {
                                                    date = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault()).format(new Date(timeStr).getTime());
                                                }
                                                String dateMac2 = date + response.body().getMac2();
                                                Log.e(tag, "dateMac2 = " + dateMac2);
                                                ServiceStatus writeCardStatu = App.app.mObuInterface.loadCreditWriteCard(dateMac2);
                                                Log.e(tag, "写卡 = " + new Gson().toJson(writeCardStatu));
                                                String chargeFlag = "-1";
                                                if (writeCardStatu.getServiceCode() == 0) {
                                                    chargeFlag = "0";
                                                } else {
                                                    chargeFlag = "-1";
                                                }
                                                new RetrofitHelper().sureLoadMoney(cardInfo.getCardId(),
                                                        response.body().getRChargeLsh(),
                                                        cardInfo.getBalanceString(), chargeFlag, writeCardStatu.getServiceInfo(),
                                                        a_on,
                                                        String.valueOf(RQcMoney + RAdjust),
                                                        response.body().getRWriteTime().replaceAll("/", "-"))
                                                        .enqueue(new Callback<CircleSave>() {
                                                            @Override
                                                            public void onResponse(Call<CircleSave> call, Response<CircleSave> response) {
                                                                if (response.body().getSuccess()) {
                                                                    Toast.makeText(BleActivity.this, "圈存成功", Toast.LENGTH_SHORT).show();
                                                                } else {
                                                                    Toast.makeText(BleActivity.this, "圈存失败", Toast.LENGTH_SHORT).show();
                                                                }
                                                            }

                                                            @Override
                                                            public void onFailure(Call<CircleSave> call, Throwable t) {
                                                                Toast.makeText(BleActivity.this, R.string.network_anomaly, Toast.LENGTH_SHORT).show();
                                                            }
                                                        });
                                            }
                                        }

                                        @Override
                                        public void onFailure(Call<CircleSave> call, Throwable t) {
                                            Toast.makeText(BleActivity.this, R.string.network_anomaly, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }

                        }
                    }
                }).start();
                break;
            case R.id.read_card_info:
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Log.e(tag, "链接状态 " + new Gson().toJson(App.app.mObuInterface.getConnectStatus()));
                        intRandom = "1234";
                        try {
                            intMac = NewDES.PBOC_3DES_MAC(intRandom, KEY).substring(0, 8);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        ServiceStatus status = null;
                        int AuthDev = App.app.mObuInterface.intAuthDev(intRandom.length() / 2, intRandom, intMac);
                        if (AuthDev == 0) {
                            Log.e(tag, "认证成功");
                            CardInformation cardInfo = new CardInformation();
                            status = App.app.mObuInterface.getCardInformation(cardInfo);
                            if (status.getServiceCode() == 0) {
                                Log.e(tag, "读卡成功 " + new Gson().toJson(cardInfo));
                            }
                        }
                        Log.e(tag, "AuthDev =  " + AuthDev);
                        Log.e(tag, "读卡 " + new Gson().toJson(App.app.mObuInterface.getCardInformation(new CardInformation())));
                    }
                }).start();
                break;
            default:
                break;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, permissions[i] + " 权限打开失败!", Toast.LENGTH_LONG).show();
                    return;
                }
            }
            Toast.makeText(this, " 权限打开成功!", Toast.LENGTH_LONG).show();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
