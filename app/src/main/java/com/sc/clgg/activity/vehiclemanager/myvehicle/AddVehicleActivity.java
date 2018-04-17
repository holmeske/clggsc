package com.sc.clgg.activity.vehiclemanager.myvehicle;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.ReplacementTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sc.clgg.R;
import com.sc.clgg.base.BaseAppCompatActivity;
import com.sc.clgg.bean.BaseBean;
import com.sc.clgg.dialog.AddVehicleDialog;
import com.sc.clgg.dialog.VehicleAttributeDialog;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;
import com.sc.clgg.util.Tools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import tool.helper.ActivityHelper;
import tool.helper.LogHelper;

/**
 * 车辆发布
 *
 * @author lvke
 */
public class AddVehicleActivity extends BaseAppCompatActivity {
    @BindView(R.id.et_truck_number) EditText et_truck_number;
    @BindView(R.id.et_truck_weight) EditText et_truck_weight;
    @BindView(R.id.et_truck_freamNumber) EditText et_truck_freamNumber;
    @BindView(R.id.tv_truck_type) TextView tv_truck_type;
    @BindView(R.id.tv_truck_length) TextView tv_truck_length;
    private Unbinder unbinder;
    private String mTruckTypeCode;
    private String mTruckLengthCode;

    /**
     * 选择车辆长度的对话框
     */
    private VehicleAttributeDialog mVehicleAttributeDialog;

    private VehicleAttributeDialog.ChooseListener mChooseListener = new VehicleAttributeDialog.ChooseListener() {

        @Override
        public void getDate(int type, String name, String code) {
            if (type == 1) {
                tv_truck_type.setText(name);
                mTruckTypeCode = code;
            } else {
                tv_truck_length.setText(name);
                mTruckLengthCode = code;
            }
        }
    };
    /**
     * 添加车辆确认框
     */
    private AddVehicleDialog mAddVehicleDialog;
    /**
     * 车牌号
     */
    private String vehicleno;
    /**
     * 载重
     */
    private String loadcapacity;
    /**
     * 车架号
     */
    private String freamNumber;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.toString().startsWith("0")) {
                et_truck_weight.setText(null);
            }

            if (s.toString().startsWith(".")) {
                et_truck_weight.setText(null);
            }

            if (s.toString().contains(".")) {
                if (s.toString().indexOf(".") > 4) {
                        /* 显示小数点前的位数 */
                    et_truck_weight.setText(s.toString().substring(0, 4));
                    et_truck_weight.setSelection(s.length() - 3);
                }

					/* 限制小数点后的位数 */
                if (s.length() - 1 - s.toString().indexOf(".") > 1) {
                    s = s.toString().subSequence(0, s.toString().indexOf(".") + 2);
                    et_truck_weight.setText(s);
                    et_truck_weight.setSelection(s.length());
                }
            } else if (s.toString().length() > 4) {
                et_truck_weight.setText(s.toString().substring(0, 4));
                et_truck_weight.setSelection(s.length() - 1);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle(getString(R.string.truck_add_truck));
        setContentView(R.layout.activity_add_vehicle);
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);

        et_truck_number.setTransformationMethod(new AllCapTransformationMethod());
        et_truck_weight.addTextChangedListener(mTextWatcher);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != Unbinder.EMPTY) {
            unbinder.unbind();
        }
    }

    private void addTruck() {
        vehicleno = et_truck_number.getText().toString().trim();
        loadcapacity = et_truck_weight.getText().toString();
        if (Tools.isNull(loadcapacity)) {
            Tools.Toast(getString(R.string.truck_add_truck_hint_3));
            return;
        }

        freamNumber = et_truck_freamNumber.getText().toString();
        if (Tools.isNull(freamNumber)) {
            Tools.Toast(getString(R.string.truck_add_truck_hint_7));
            return;
        }
        if (freamNumber.length() != 8) {
            Tools.Toast(getString(R.string.truck_add_truck_fream_hint_0));
            return;
        }
        mAddVehicleDialog = new AddVehicleDialog(this, v -> {
            mAddVehicleDialog.dismiss();
            if (R.id.btn_yes == v.getId()) {
                releaseVehicle();
            }
        });
        mAddVehicleDialog.show();
    }

    private void releaseVehicle() {
        LogHelper.e("freamNumber = " + freamNumber);
        LogHelper.e("vehicleno = " + vehicleno);
        LogHelper.e("mTruckTypeCode = " + mTruckTypeCode);
        LogHelper.e("mTruckLengthCode = " + mTruckLengthCode);
        LogHelper.e("loadcapacity = " + loadcapacity);

        HttpRequestHelper.releaseVehicle(freamNumber, vehicleno, mTruckTypeCode, mTruckLengthCode, loadcapacity, new HttpCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showProgressDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideProgressDialog();
            }

            @Override
            public void onSuccess(String body) {
                if (TextUtils.isEmpty(body)) {
                    return;
                }

                BaseBean bean = new Gson().fromJson(body, BaseBean.class);

                if (null != bean) {
                    if (!Tools.isNull(bean.getErrorCode())) {
                        switch (bean.getErrorCode()) {
                            case "TRUCK1008":
                                Tools.Toast("发布车辆失败");
                                break;
                            case "TRUCK1009":
                                Tools.Toast("保存路线信息失败");
                                break;
                            case "TRUCK1013":
                                Tools.Toast("保存认证信息失败");
                                break;
                            default:
                                Tools.Toast("同步D-TMS时失败");
                                break;
                        }
                    } else {
                        Tools.Toast(getString(R.string.succ_add_truck));
                        setResult(0);
                        ActivityHelper.finishAcMove(AddVehicleActivity.this);
                    }
                } else {
                    Tools.Toast(getString(R.string.err_add_truck));
                }
            }

            @Override
            public void onError(String body) {
                super.onError(body);
                Tools.Toast("发布车辆失败");
            }
        });
    }

    public void onBtnClick(View v) {
        switch (v.getId()) {
            case R.id.tv_truck_type:
                if (null == mVehicleAttributeDialog) {
                    mVehicleAttributeDialog = new VehicleAttributeDialog(this, mChooseListener);
                }
                mVehicleAttributeDialog.show(1);
                break;

            case R.id.tv_truck_length:
                if (null == mVehicleAttributeDialog) {
                    mVehicleAttributeDialog = new VehicleAttributeDialog(this, mChooseListener);
                }
                mVehicleAttributeDialog.show(2);
                break;

            case R.id.btn_submit:
                addTruck();
                break;

            default:
                break;
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