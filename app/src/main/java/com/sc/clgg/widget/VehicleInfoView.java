package com.sc.clgg.widget;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bumptech.glide.Glide;
import com.sc.clgg.R;
import com.sc.clgg.activity.contact.OnPickListener;
import com.sc.clgg.bean.CertificationInfo;
import com.sc.clgg.dialog.CarTypeDialog;
import com.sc.clgg.tool.helper.LogHelper;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import static org.jetbrains.anko.ToastsKt.toast;

/**
 * @author：lvke
 * @date：2018/10/20 14:56
 */
public class VehicleInfoView extends ConstraintLayout {
    private static boolean showVehicleLicenseHint = true;
    public ConstraintLayout cl_body, cl_upload_hint;
    private View mView;
    private TextView tv_title, tv_vehicle_type, tv_vehicle_use_type, tv_vehicle_color, tv_vehicle_brand_color;
    private ImageView iv_folding,iv_vehicle_type_des, iv_vehicle_use_type_des,iv_select_vehicle_type, iv_select_vehicle_use_type, iv_select_vehicle_color, iv_select_vehicle_brand_color, iv_vehicle_license;
    private EditText et_vehicle_no, et_vehicle_vin, et_vehicle_master, et_vehicle_load, et_vehicle_engine_no;
    private String vehicleLicense = "";

    public VehicleInfoView(Context context) {
        super(context);
        init(context);
    }

    public VehicleInfoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VehicleInfoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.view_vehicle_info, this, false);
        addView(mView);

        initView(context, mView);
        initListener(context);

        if (showVehicleLicenseHint) {
            mView.findViewById(R.id.cl_upload_hint).setVisibility(VISIBLE);
            showVehicleLicenseHint = false;
        }
    }

    /**
     * 输入校验
     *
     * @param context
     * @return
     */
    public boolean canSubmit(Context context) {
        if (et_vehicle_no.getText().toString().isEmpty()) {
            toast(context, "请输入车牌号");
            return false;
        }
        if (et_vehicle_vin.getText().toString().isEmpty()) {
            toast(context, "请输入车辆识别代码(VIN码)");
            return false;
        }
        if (et_vehicle_master.getText().toString().isEmpty()) {
            toast(context, "请输入车辆所有人");
            return false;
        }
        if (et_vehicle_load.getText().toString().isEmpty()) {
            toast(context, "请输入货车载重");
            return false;
        }
        if (tv_vehicle_type.getText().toString().equals("请选择车辆类型")) {
            toast(context, "请选择车辆类型");
            return false;
        }
        if (tv_vehicle_use_type.getText().toString().equals("请选择车辆使用性质")) {
            toast(context, "请选择车辆使用性质");
            return false;
        }
        if (et_vehicle_engine_no.getText().toString().isEmpty()) {
            toast(context, "请输入发动机号");
            return false;
        }
        if (tv_vehicle_color.getText().toString().equals("请选择车辆颜色")) {
            toast(context, "请选择车辆颜色");
            return false;
        }
        if (tv_vehicle_brand_color.getText().toString().equals("请选择车牌颜色")) {
            toast(context, "请选择车牌颜色");
            return false;
        }
        if (vehicleLicense.isEmpty()) {
            toast(context, "请上传行驶证");
            return false;
        }
        return true;
    }

    public CertificationInfo.Car getCar() {
        CertificationInfo.Car car = new CertificationInfo.Car();
        car.setCarNo(et_vehicle_no.getText().toString());
        car.setVinCode(et_vehicle_vin.getText().toString());
        car.setCarOwner(et_vehicle_master.getText().toString());
        car.setCarWeight(et_vehicle_load.getText().toString());
        car.setCarType(tv_vehicle_type.getText().toString());
        car.setFunction(tv_vehicle_use_type.getText().toString());
        car.setEngineNumber(et_vehicle_engine_no.getText().toString());
        car.setCarColor(tv_vehicle_color.getText().toString());
        car.setCarNoColor(tv_vehicle_brand_color.getText().toString());
        car.setVehicleLicenseImg(vehicleLicense);
        return car;
    }

    /**
     * 设置车辆信息
     *
     * @param car
     * @return
     */
    public VehicleInfoView setCar(CertificationInfo.Car car) {
        tv_title.setText(car.getCarNo());

        et_vehicle_no.setText(car.getCarNo());
        et_vehicle_vin.setText(car.getVinCode());
        et_vehicle_master.setText(car.getCarOwner());
        et_vehicle_load.setText(car.getCarWeight());
        tv_vehicle_type.setText(car.getCarType());
        tv_vehicle_use_type.setText(car.getFunction());
        et_vehicle_engine_no.setText(car.getEngineNumber());
        tv_vehicle_color.setText(car.getCarColor());
        tv_vehicle_brand_color.setText(car.getCarNoColor());

        LogHelper.e("car.getVehicleLicenseImg() = " + car.getVehicleLicenseImg());
        setVehicleLicense(car.getVehicleLicenseImg());
//        iv_vehicle_license.setImageBitmap(BitmapFactory.decodeFile(car.getVehicleLicenseImg()));
        return this;
    }

    public void cleanVehicleInfo() {
//            tv_title.setText("输入车辆信息");
//
//            et_vehicle_no.setText(car.getCarNo());
//            et_vehicle_vin.setText(car.getVinCode());
//            et_vehicle_master.setText(car.getCarOwner());
//            et_vehicle_load.setText(car.getCarWeight());
//            tv_vehicle_type.setText(car.getCarType());
//            tv_vehicle_use_type.setText(car.getFunction());
//            et_vehicle_engine_no.setText(car.getEngineNumber());
//            tv_vehicle_color.setText(car.getCarColor());
//            tv_vehicle_brand_color.setText(car.getCarNoColor());
//
//            Glide.with(getContext()).load(new File(car.getVehicleLicenseImg())).into(iv_vehicle_license);
    }

    public void setVehicleLicense(String filePath) {
        vehicleLicense = filePath;
        Glide.with(getContext()).load(new File(filePath)).into(iv_vehicle_license);
    }

    public void onUploadListener(OnClickListener listener) {
        iv_vehicle_license.setOnClickListener(listener);
    }

    private void initView(Context context, View v) {

        tv_title = v.findViewById(R.id.tv_title);
        tv_vehicle_type = v.findViewById(R.id.tv_vehicle_type);
        tv_vehicle_use_type = v.findViewById(R.id.tv_vehicle_use_type);
        tv_vehicle_color = v.findViewById(R.id.tv_vehicle_color);
        tv_vehicle_brand_color = v.findViewById(R.id.tv_vehicle_brand_color);
        iv_folding = v.findViewById(R.id.iv_folding);
        iv_vehicle_type_des = v.findViewById(R.id.iv_vehicle_type_des);
        iv_vehicle_use_type_des = v.findViewById(R.id.iv_vehicle_use_type_des);

        iv_select_vehicle_type = v.findViewById(R.id.iv_select_vehicle_type);
        iv_select_vehicle_use_type = v.findViewById(R.id.iv_select_vehicle_use_type);
        iv_select_vehicle_color = v.findViewById(R.id.iv_select_vehicle_color);
        iv_select_vehicle_brand_color = v.findViewById(R.id.iv_select_vehicle_brand_color);
        iv_vehicle_license = v.findViewById(R.id.iv_vehicle_license);

        cl_body = v.findViewById(R.id.cl_body);
        cl_upload_hint = v.findViewById(R.id.cl_upload_hint);

        et_vehicle_no = v.findViewById(R.id.et_vehicle_no);
        et_vehicle_vin = v.findViewById(R.id.et_vehicle_vin);
        et_vehicle_master = v.findViewById(R.id.et_vehicle_master);
        et_vehicle_load = v.findViewById(R.id.et_vehicle_load);
        et_vehicle_engine_no = v.findViewById(R.id.et_vehicle_engine_no);

        String hint = context.getString(R.string.upload_hint);
        SpannableString spannableString = new SpannableString(hint);

        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color._9c9c9c)), 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        ((TextView) mView.findViewById(R.id.tv_12)).setText(spannableString);
    }

    /**
     * 展开
     */
    public void fold() {
        cl_body.setVisibility(View.VISIBLE);
    }

    /**
     * 折叠
     */
    public void unFold() {
        cl_body.setVisibility(View.GONE);
    }

    public void showOrHide() {
        if (cl_body.getVisibility() == View.VISIBLE) {
            unFold();
        } else {
            fold();
        }
    }

    public void setOnShowOrHideListener(OnClickListener listener) {
        iv_folding.setOnClickListener(listener);
    }


    private void initListener(Context context) {
        iv_vehicle_type_des.setOnClickListener(v -> {new CarTypeDialog(context).show(); });
        iv_vehicle_use_type_des.setOnClickListener(v -> {new CarTypeDialog(context).show(); });

        iv_folding.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showOrHide();
            }
        });

        iv_select_vehicle_type.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                creatPicker(new String[]{"五型货车", "四型货车", "三型货车", "二型货车", "一型货车"}, new OnPickListener() {
                    @Override
                    public void onOptionsSelect(String str) {
                        tv_vehicle_type.setText(str);
                    }
                });
            }
        });
        iv_select_vehicle_use_type.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                creatPicker(new String[]{"营业货车", "非营业货车"}, new OnPickListener() {
                    @Override
                    public void onOptionsSelect(String str) {
                        tv_vehicle_use_type.setText(str);
                    }
                });
            }
        });
        iv_select_vehicle_color.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                creatPicker(new String[]{"黄", "红", "蓝", "黑", "白", "灰", "青", "银"}, new OnPickListener() {
                    @Override
                    public void onOptionsSelect(String str) {
                        tv_vehicle_color.setText(str);
                    }
                });
            }
        });
        iv_select_vehicle_brand_color.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                creatPicker(new String[]{"黄", "红", "黑", "白", "渐变绿", "黄绿双拼", "蓝白渐变"}, new OnPickListener() {
                    @Override
                    public void onOptionsSelect(String str) {
                        tv_vehicle_brand_color.setText(str);
                    }
                });
            }
        });

    }

    private void creatPicker(String[] strings, OnPickListener listener) {
        List<String> data = Arrays.asList(strings);
        new PickerViewHelper().creat(((Activity) getContext()), data, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                listener.onOptionsSelect(data.get(options1));
            }
        });
    }
}
