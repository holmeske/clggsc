package com.sc.clgg.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.bean.CertificationInfo;
import com.sc.clgg.tool.helper.LogHelper;

import androidx.constraintlayout.widget.ConstraintLayout;

import static org.jetbrains.anko.ToastsKt.toast;


/**
 * @author lvke
 */
public class VehicleImageView extends ConstraintLayout {
    private View mView;
    private TextView tv_car_title, tv_delete, tv_vehicle_license_hint, tv_vehicle_front_hint;
    private ImageView iv_expand, iv_vehicle_license, iv_vehicle_front;
    private LinearLayout ll_body, ll_hint_1, ll_hint_2;
    private String vehicleLicense = "";

    private CertificationInfo.Car car = new CertificationInfo.Car();

    public VehicleImageView(Context context) {
        super(context);
        init(context);
    }

    public VehicleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public VehicleImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mView = LayoutInflater.from(context).inflate(R.layout.view_vehicle_image, this, false);
        addView(mView);

        initView(context, mView);
        initListener(context);
    }

    private void initView(Context context, View v) {
        tv_car_title = v.findViewById(R.id.tv_car_title);
        tv_delete = v.findViewById(R.id.tv_delete);
        tv_vehicle_license_hint = v.findViewById(R.id.tv_vehicle_license_hint);
        tv_vehicle_front_hint = v.findViewById(R.id.tv_vehicle_front_hint);

        iv_expand = v.findViewById(R.id.iv_expand);
        iv_vehicle_license = v.findViewById(R.id.iv_vehicle_license);
        iv_vehicle_front = v.findViewById(R.id.iv_vehicle_front);

        ll_body = v.findViewById(R.id.ll_body);
        ll_hint_1 = v.findViewById(R.id.ll_hint_1);
        ll_hint_2 = v.findViewById(R.id.ll_hint_2);

        initListener(context);
    }

    /**
     * 设置车辆标题
     */
    public void setVehicleTitle(String title) {
        tv_car_title.setText(title);
    }

    /**
     * 第一辆车的默认设置
     */
    public void setFirstVehicle() {
        ll_hint_1.setVisibility(View.VISIBLE);
        ll_hint_2.setVisibility(View.VISIBLE);

        tv_delete.setVisibility(View.GONE);
    }

    /**
     * 设置行驶证监听
     *
     * @param listener
     */
    public void setVehicleLicense(OnClickListener listener) {
        iv_vehicle_license.setOnClickListener(listener);
    }

    /**
     * 设置车正面照监听
     *
     * @param listener
     */
    public void setVehicleFront(OnClickListener listener) {
        iv_vehicle_front.setOnClickListener(listener);
    }

    public void OnDeleteListener(OnClickListener listener) {
        tv_delete.setOnClickListener(listener);
    }

    public CertificationInfo.Car getCar() {
        return car;
    }

    private void initListener(Context context) {
        iv_expand.setOnClickListener(v -> {
            if (ll_body.getVisibility() == View.VISIBLE) {
                ll_body.setVisibility(View.GONE);
            } else {
                ll_body.setVisibility(View.VISIBLE);
            }
            LogHelper.e("getRotation() = " + v.getRotation());
            v.setRotation(v.getRotation() + 180);
            LogHelper.e("getRotation() = " + v.getRotation());
        });
    }

    /**
     * 输入校验
     */
    public boolean checkThrough(Context context) {
        if (car.getVehicleLicenseImg().isEmpty()) {
            toast(context, "请上传行驶证照片(主副页一起上传)");
            return false;
        }
        if (car.getVehicleFrontImg().isEmpty()) {
            toast(context, "请上传车辆正面照片");
            return false;
        }
        return true;
    }

}
