package com.sc.clgg.activity.vehiclemanager.myvehicle;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.ReplacementTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.view.CropImageView;
import com.sc.clgg.R;
import com.sc.clgg.activity.GlideImageLoader;
import com.sc.clgg.base.BaseImmersionActivity;
import com.sc.clgg.bean.Check;
import com.sc.clgg.http.retrofit.RetrofitHelper;
import com.sc.clgg.tool.helper.LogHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

/**
 * @author lvke
 */
public class AddVehicleActivity extends BaseImmersionActivity {
    @BindView(R.id.et_car_no) EditText et_car_no;
    @BindView(R.id.et_car_vin) EditText et_car_vin;
    @BindView(R.id.tv_add) TextView tv_add;
    private Call<Check> mCall;
    private ArrayList<ImageItem> images;
    private Call<Map<String, Object>> call;
    private String scan = "0";
    private String carno = "";
    private String vin = "";
    private String carType = "";
    private String carOwner = "";
    private String address = "";
    private String engineNumber = "";
    private String registrationDate = "";
    private String carLicenceDate = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vehicle);
        unbinder = ButterKnife.bind(this);
        initTitle("添加车辆");
//        SoftHideKeyBoardUtil.assistActivity(this);
        et_car_no.setTransformationMethod(new AllCapTransformationMethod());
        et_car_vin.setTransformationMethod(new AllCapTransformationMethod());

        findViewById(R.id.iv_scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker imagePicker = ImagePicker.getInstance();
                imagePicker.setImageLoader(new GlideImageLoader());
                imagePicker.setShowCamera(true);
                imagePicker.setCrop(false);
                imagePicker.setSaveRectangle(true);
                imagePicker.setSelectLimit(1);
                imagePicker.setMultiMode(false);
                imagePicker.setStyle(CropImageView.Style.RECTANGLE);
                imagePicker.setFocusWidth(280);//裁剪框的宽度。单位像素（圆形自动取宽高最小值）
                imagePicker.setFocusHeight(280);//裁剪框的高度。单位像素（圆形自动取宽高最小值）
                imagePicker.setOutPutX(800);//保存文件的宽度。单位像素
                imagePicker.setOutPutY(800);//保存文件的高度。单位像素

                Intent intent = new Intent(AddVehicleActivity.this, ImageGridActivity.class);
                intent.putExtra(ImageGridActivity.EXTRAS_IMAGES, images);
                startActivityForResult(intent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            try {
                if (data != null && requestCode == 100) {
                    images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    LogHelper.e("" + images.get(0).path);

                    showProgressDialog();
                    compressImage(images.get(0).path);
                }
            } catch (Exception e) {
                LogHelper.e(e);
            }
        }
    }

    private void compressImage(String path) {
        Luban.with(this).load(path).ignoreBy(100).setCompressListener(new OnCompressListener() {
            @Override
            public void onStart() {
                LogHelper.e("onStart()");
            }

            @Override
            public void onSuccess(File file) {
                LogHelper.e("onSuccess()");
                call = new RetrofitHelper().scan(file);
                call.enqueue(new Callback<Map<String, Object>>() {
                    @Override
                    public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                        hideProgressDialog();
                        Map<String, Object> allMap = response.body();
                        if (allMap.containsKey("success") && (boolean) allMap.get("success")) {
                            Map<String, Object> identifyMap = (Map<String, Object>) allMap.get("identify");

                            if (identifyMap.containsKey("words_result")) {
                                Map<String, Object> resultMap = (Map<String, Object>) identifyMap.get("words_result");

                                et_car_no.setText(((Map<String, String>) resultMap.get("号牌号码")).get("words"));
                                et_car_vin.setText(((Map<String, String>) resultMap.get("车辆识别代号")).get("words"));

                                scan = "1";
                                carType = ((Map<String, String>) resultMap.get("车辆类型")).get("words");
                                carOwner = ((Map<String, String>) resultMap.get("所有人")).get("words");
                                address = ((Map<String, String>) resultMap.get("住址")).get("words");
                                engineNumber = ((Map<String, String>) resultMap.get("发动机号码")).get("words");
                                registrationDate = ((Map<String, String>) resultMap.get("注册日期")).get("words");
                                carLicenceDate = ((Map<String, String>) resultMap.get("发证日期")).get("words");
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                        hideProgressDialog();
                    }
                });
            }

            @Override
            public void onError(Throwable e) {
                LogHelper.e("onError()");
            }
        }).launch();
    }

    @OnClick(R.id.tv_add)
    void add() {
        carno = et_car_no.getText().toString();
        vin = et_car_vin.getText().toString();

        if (TextUtils.isEmpty(carno)) {
            Toast.makeText(this, "请输入车牌号", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(vin)) {
            Toast.makeText(this, "请输入VIN码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (vin.length() != 17) {
            Toast.makeText(this, "请输入17位VIN码", Toast.LENGTH_SHORT).show();
            return;
        }

        mCall = new RetrofitHelper().vehicleAdd(carno, vin, scan, carType, carOwner, address, engineNumber, registrationDate, carLicenceDate);
        mCall.enqueue(new Callback<Check>() {

            @Override
            public void onResponse(Call<Check> call, Response<Check> response) {
                if (response.body().getSuccess()) {
                    Toast.makeText(AddVehicleActivity.this, "添加车辆成功", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(AddVehicleActivity.this, String.valueOf(response.body().getMsg()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Check> call, Throwable t) {
                Toast.makeText(AddVehicleActivity.this, R.string.network_anomaly, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCall != null) {
            mCall.cancel();
        }
        if (call != null) {
            call.cancel();
        }
    }

    //小写字母自动转换为大写
    private class AllCapTransformationMethod extends ReplacementTransformationMethod {

        @Override
        protected char[] getOriginal() {
            return new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        }

        @Override
        protected char[] getReplacement() {
            return new char[]{'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        }

    }

}