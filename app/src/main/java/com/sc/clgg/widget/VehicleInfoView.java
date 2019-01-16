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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.activity.contact.OnPickListener;
import com.sc.clgg.bean.CertificationInfo;
import com.sc.clgg.tool.helper.LogHelper;
import com.sc.clgg.util.PotatoKt;

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
    private LinearLayout ll_body;
    private View mView;
    private TextView tv_title, tv_vehicle_brand_color, tv_vehicle_color, tv_vehicle_type,
            tv_vehicle_use_type;
    private ImageView iv_folding;
    private EditText et_vehicle_no, et_vehicle_master, et_vehicle_master_address,
            et_vehicle_license_type, et_vehicle_license_brand_model, et_vehicle_vin, et_vehicle_engine_no, et_vehicle_wheel_amount, et_vehicle_axle_amount;

    private CertificationInfo.Car car = new CertificationInfo.Car();
    private Context mContext;

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
        mContext = context;
        mView = LayoutInflater.from(context).inflate(R.layout.view_vehicle_info, this, false);
        addView(mView);

        initView(context, mView);
        initListener(context);
    }

    /**
     * 输入校验
     */
    public boolean checkThrough(Context context) {
        if (car.getCarNoColor().isEmpty()) {
            toast(context, "请选择车牌颜色");
            return false;
        }
        if (car.getCarColor().isEmpty()) {
            toast(context, "请选择车辆颜色");
            return false;
        }
        if (car.getCarNo().isEmpty()) {
            toast(context, "请输入车牌号码");
            return false;
        }
        if (car.getCarOwner().isEmpty()) {
            toast(context, "请输入所有人（行驶证）");
            return false;
        }
        if (car.getAddress().isEmpty()) {
            toast(context, "请输入所有人地址（行驶证）");
            return false;
        }
        if (car.getCarType().isEmpty()) {
            toast(context, "请选择车型");
            return false;
        }
        if (car.getVehicleType().isEmpty()) {
            toast(context, "请输入行驶证车辆类型");
            return false;
        }
        if (car.getModel().isEmpty()) {
            toast(context, "请输入行驶证品牌型号");
            return false;
        }
        if (car.getFunction().isEmpty()) {
            toast(context, "请选择车辆使用性质");
            return false;
        }
        if (car.getVinCode().isEmpty()) {
            toast(context, "请输入车辆识别代号（行驶证）");
            return false;
        }
        if (car.getEngineNumber().isEmpty()) {
            toast(context, "请输入车辆发动机号（行驶证）");
            return false;
        }
        return true;
    }

    public void setVehicleName(String name) {
        tv_title.setText(name);
    }

    public CertificationInfo.Car getCar() {
        car.setCarNoColor(tv_vehicle_brand_color.getText().toString());
        car.setCarColor(tv_vehicle_color.getText().toString());
        car.setCarNo(et_vehicle_no.getText().toString());
        car.setCarOwner(et_vehicle_master.getText().toString());
        car.setAddress(et_vehicle_master_address.getText().toString());
        car.setCarType(tv_vehicle_type.getText().toString());
        car.setVehicleType(et_vehicle_license_type.getText().toString());
        car.setModel(et_vehicle_license_brand_model.getText().toString());
        car.setFunction(tv_vehicle_use_type.getText().toString());
        car.setVinCode(et_vehicle_vin.getText().toString());
        car.setEngineNumber(et_vehicle_engine_no.getText().toString());
        car.setTyreNumber(et_vehicle_wheel_amount.getText().toString());
        car.setAxleNumber(et_vehicle_axle_amount.getText().toString());
        return car;
    }

    public void setCar(CertificationInfo.Car c) {
        this.car = c;
        this.et_vehicle_no.setText(c.getCarNo());
        this.et_vehicle_master.setText(c.getCarOwner());
        this.et_vehicle_master_address.setText(c.getAddress());
        this.et_vehicle_license_type.setText(c.getVehicleType());
        this.et_vehicle_license_brand_model.setText(c.getModel());
        this.et_vehicle_vin.setText(c.getVinCode());
        this.et_vehicle_engine_no.setText(c.getEngineNumber());
    }

    private void initView(Context context, View v) {
        ll_body = v.findViewById(R.id.ll_body);
        tv_title = v.findViewById(R.id.tv_title);
        iv_folding = v.findViewById(R.id.iv_folding);

        tv_vehicle_brand_color = v.findViewById(R.id.tv_vehicle_brand_color);
        tv_vehicle_color = v.findViewById(R.id.tv_vehicle_color);
        et_vehicle_no = v.findViewById(R.id.et_vehicle_no);
        et_vehicle_master = v.findViewById(R.id.et_vehicle_master);
        et_vehicle_master_address = v.findViewById(R.id.et_vehicle_master_address);
        tv_vehicle_type = v.findViewById(R.id.tv_vehicle_type);

        et_vehicle_license_type = v.findViewById(R.id.et_vehicle_license_type);
        et_vehicle_license_brand_model = v.findViewById(R.id.et_vehicle_license_brand_model);
        tv_vehicle_use_type = v.findViewById(R.id.tv_vehicle_use_type);
        et_vehicle_vin = v.findViewById(R.id.et_vehicle_vin);
        et_vehicle_engine_no = v.findViewById(R.id.et_vehicle_engine_no);
        et_vehicle_wheel_amount = v.findViewById(R.id.et_vehicle_wheel_amount);
        et_vehicle_axle_amount = v.findViewById(R.id.et_vehicle_axle_amount);

        String hint = context.getString(R.string.upload_hint);
        SpannableString spannableString = new SpannableString(hint);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, R.color._9c9c9c)), 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    private void initListener(Context context) {

        iv_folding.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ll_body.getVisibility() == View.VISIBLE) {
                    ll_body.setVisibility(View.GONE);
                } else {
                    ll_body.setVisibility(View.VISIBLE);
                }
                LogHelper.e("getRotation() = " + v.getRotation());
                v.setRotation(v.getRotation() + 180);
                LogHelper.e("getRotation() = " + v.getRotation());
            }
        });
        tv_vehicle_brand_color.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                creatPicker(v, new String[]{"蓝", "黄", "黑", "白", "渐变绿", "黄绿双拼", "蓝白渐变"}, new OnPickListener() {
                    @Override
                    public void onOptionsSelect(String str) {
                        tv_vehicle_brand_color.setText(str);
                    }
                });
            }
        });
        tv_vehicle_color.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                creatPicker(v, new String[]{"蓝", "黄", "黑", "白", "渐变绿", "黄绿双拼", "蓝白渐变", "灰", "青", "银", "红", "棕", "紫"}, new OnPickListener() {
                    @Override
                    public void onOptionsSelect(String str) {
                        tv_vehicle_color.setText(str);
                    }
                });
            }
        });
        tv_vehicle_type.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                creatPicker(v, new String[]{"一型货车", "二型货车", "三型货车", "四型货车", "五型货车", "一型客车", "二型客车", "三型客车", "四型客车"}, new OnPickListener() {
                    @Override
                    public void onOptionsSelect(String str) {
                        tv_vehicle_type.setText(str);
                    }
                });
            }
        });
        tv_vehicle_use_type.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                creatPicker(v, new String[]{"家庭自用", "非营业车辆", "营业客车", "非营业货车", "营业货车", "特种车", "挂车"}, new OnPickListener() {
                    @Override
                    public void onOptionsSelect(String str) {
                        tv_vehicle_use_type.setText(str);
                    }
                });
            }
        });
    }

    private void creatPicker(View view, String[] strings, OnPickListener listener) {
        PotatoKt.hideSoftInputFromWindow(mContext, view);
        List<String> data = Arrays.asList(strings);
        new PickerViewHelper().creat(((Activity) getContext()), data, (options1, options2, options3, v) -> listener.onOptionsSelect(data.get(options1)));
    }

}
