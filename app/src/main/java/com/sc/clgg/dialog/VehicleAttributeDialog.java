package com.sc.clgg.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.sc.clgg.R;
import com.sc.clgg.application.App;
import com.sc.clgg.bean.OptionsListBean;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;
import com.sc.clgg.util.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import tool.helper.LogHelper;

/**
 * 选择车辆属性的对话框   ：车长  车名称
 *
 * @author lvke
 */
public class VehicleAttributeDialog extends Dialog {
    private ListView mListView;
    private Activity mActivity;
    private ChooseListener mChooseListener;
    private ProgressBar mProgressBar;
    private List<Map<String, String>> mVehicleLengthMapList = new ArrayList<>();
    private List<Map<String, String>> mVehicleTypeMapList = new ArrayList<>();

    public VehicleAttributeDialog(Activity activity, ChooseListener listener) {
        super(activity, R.style.dialog_base);
        mActivity = activity;
        mChooseListener = listener;
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_vehicle_attribute);
        mListView = (ListView) findViewById(R.id.lv_truck_property);
        mProgressBar = (ProgressBar) findViewById(R.id.pb);
    }

    /**
     * @param type 1 获取“车辆类型”集合    2 获取“车辆长度”集合
     */
    public void show(int type) {
        super.show();
        mListView.setAdapter(null);
        mListView.setOnItemClickListener(null);
        if (type == 1) {
            getVehicleType();
        } else {
            getVehicleLength();
        }
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        LogHelper.e("onAttachedToWindow");
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        LogHelper.e("onDetachedFromWindow");
    }

    private void getVehicleLength() {
        mVehicleLengthMapList.clear();
        HttpRequestHelper.getVehicleLength(new HttpCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(String body) {
                if (TextUtils.isEmpty(body)) {
                    return;
                }
                OptionsListBean bean = new Gson().fromJson(body, OptionsListBean.class);
                if (null == bean) {
                    return;
                }
                List<OptionsListBean.Options> optionsList = bean.getCodeList();

                for (OptionsListBean.Options options : optionsList) {
                    Map<String, String> map = new HashMap<>();
                    map.put("code", options.getCode());
                    map.put("codeId", options.getCodeId());
                    map.put("codeNameC", options.getCodeNameC());
                    mVehicleLengthMapList.add(map);
                }
                LogHelper.e("mVehicleLengthMapList  ==  " + JSON.toJSONString(mVehicleLengthMapList));

                mListView.setAdapter(new SimpleAdapter(mActivity, mVehicleLengthMapList, R.layout.item_text, new String[]{"codeNameC"}, new int[]{R.id.tv_text}));

                mListView.setOnItemClickListener((parent, view, position, id) -> {
                    try {
                        LogHelper.e(JSON.toJSONString(mVehicleLengthMapList));
                        if (null != mChooseListener && mVehicleLengthMapList.size() > 0) {
                            mChooseListener.getDate(2, optionsList.get(position).getCodeNameC(), optionsList.get(position).getCode());
                            dismiss();
                        }
                    } catch (Exception e) {
                        LogHelper.e(e);
                    }
                });
            }

            @Override
            public void onError(String body) {
                super.onError(body);
                Tools.Toast(App.getInstance().getString(R.string.network_anomaly));
            }
        });

    }

    private void getVehicleType() {
        mVehicleTypeMapList.clear();
        HttpRequestHelper.getDataByType(new HttpCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                mProgressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onSuccess(String body) {
                if (TextUtils.isEmpty(body)) {
                    return;
                }
                OptionsListBean bean = new Gson().fromJson(body, OptionsListBean.class);
                if (bean == null) {
                    return;
                }
                List<OptionsListBean.Options> optionsList = bean.getCodeList();
                if (optionsList == null || optionsList.isEmpty()) {
                    return;
                }
                for (OptionsListBean.Options options : optionsList) {
                    Map<String, String> map = new HashMap<>();
                    map.put("code", options.getCode());
                    map.put("codeId", options.getCodeId());
                    map.put("codeNameC", options.getCodeNameC());
                    mVehicleTypeMapList.add(map);
                }
                LogHelper.e("mVehicleTypeMapList  ==  " + JSON.toJSONString(mVehicleTypeMapList));

                mListView.setAdapter(new SimpleAdapter(mActivity, mVehicleTypeMapList, R.layout.item_text, new String[]{"codeNameC"}, new int[]{R.id.tv_text}));

                mListView.setOnItemClickListener((parent, view, position, id) -> {

                    if (null != mChooseListener && mVehicleTypeMapList.size() > 0) {
                        mChooseListener.getDate(1, optionsList.get(position).getCodeNameC(), optionsList.get(position).getCode());
                        dismiss();
                    }
                });
            }
        });
    }


    /**
     * 选择坚挺毁掉
     */
    public interface ChooseListener {
        void getDate(int type, String str, String position);
    }
}
