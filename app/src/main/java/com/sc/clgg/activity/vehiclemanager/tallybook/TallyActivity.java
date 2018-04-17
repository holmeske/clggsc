package com.sc.clgg.activity.vehiclemanager.tallybook;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.sc.clgg.R;
import com.sc.clgg.activity.presenter.TallyPresenter;
import com.sc.clgg.adapter.SpendingSourcesAdapter;
import com.sc.clgg.adapter.TextViewAdapter;
import com.sc.clgg.base.BaseActivity;
import com.sc.clgg.bean.IorderListBean;
import com.sc.clgg.bean.ItemOrderBean;
import com.sc.clgg.http.HttpCallBack;
import com.sc.clgg.http.HttpRequestHelper;
import com.sc.clgg.util.Tools;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import tool.helper.AppHelper;
import tool.helper.CalendarHelper;
import tool.helper.MeasureUtils;
import tool.widget.Triangle;

public class TallyActivity extends BaseActivity {

    @BindView(R.id.et_money) EditText mEtMoney;
    @BindView(R.id.rv_type) RecyclerView mRvType;
    @BindView(R.id.rv_waybill) RecyclerView mRvWaybill;

    @BindView(R.id.triangle) Triangle mTriangle;

    @BindView(R.id.titlebar_right_text) TextView tv_right;
    @BindView(R.id.titlebar_title) TextView mTitlebarTitle;
    @BindView(R.id.ll_tally_type) LinearLayout mLlTallyType;
    @BindView(R.id.tv_save) TextView mTvSave;
    @BindView(R.id.tv_choose_hint) TextView mTvChooseHint;

    private TextViewAdapter adapter;
    private SpendingSourcesAdapter mSpendingSourcesAdapter;
    private Handler mHandler;
    private String costType = "";
    private String waybillNo = "";
    private String money = "";
    private TimePickerView mTimePickerView;
    private TimePickerView.Builder mBuilder;
    private Calendar startCalendar = Calendar.getInstance();//开始日期
    private Calendar mSelectedCalendar = Calendar.getInstance();//系统当前时间
    private String time;
    private int businessType = 2;

    @OnClick({R.id.tv_save, R.id.titlebar_right_text, R.id.ll_tally_type})
    void a(View v) {
        switch (v.getId()) {
            case R.id.tv_save:
                save(businessType);
                break;
            case R.id.titlebar_right_text:
                mTimePickerView.show();
                break;

            case R.id.ll_tally_type:
                PopupWindow mPopupWindow = new PopupWindow();
                View view = View.inflate(this, R.layout.view_choose_tally_type, null);
                mPopupWindow.setContentView(view);
                view.findViewById(R.id.tv_expend).setOnClickListener(view1 -> {
                    businessType = 2;
                    mTvChooseHint.setText("请选择支出来源");
                    mTitlebarTitle.setText(R.string.expend);
                    adapter.refresh(TallyPresenter.getExpendTypeList());
                    loadData(time, time);
                    costType = "";
                    waybillNo = "";
                    money = "";
                    if (mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                });
                view.findViewById(R.id.tv_income).setOnClickListener(view2 -> {
                    businessType = 1;
                    mTvChooseHint.setText("请选择收入来源");
                    mTitlebarTitle.setText(R.string.income);
                    adapter.refresh(TallyPresenter.getIncomeTypeList());
                    loadData(time, time);
                    costType = "";
                    waybillNo = "";
                    money = "";
                    if (mPopupWindow.isShowing()) {
                        mPopupWindow.dismiss();
                    }
                });
                mPopupWindow.setOutsideTouchable(true);
                mPopupWindow.setFocusable(true);
                mPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mPopupWindow.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
                mPopupWindow.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
                mPopupWindow.showAsDropDown(mTriangle, -MeasureUtils.dp2px(this, 15), 0);
                break;
            default:
                break;
        }

    }

    private void save(int businessType) {
        money = mEtMoney.getText().toString().trim();
        if (TextUtils.isEmpty(costType)) {
            Tools.Toast("请选择支出类型");
            return;
        }
        if (TextUtils.isEmpty(money)) {
            Tools.Toast("请输入金额");
            return;
        }
        if (Double.parseDouble(money) == 0) {
            Tools.Toast("输入金额不能为零");
            return;
        }
        HttpRequestHelper.rememberA(businessType, costType, money, waybillNo, time, new HttpCallBack() {
            @Override
            public void onStart() {
                super.onStart();
                showProgressDialog();
            }

            @Override
            public void onSuccess(String body) {

                Map<String, Object> map = (Map<String, Object>) JSON.parse(body);
                if ((boolean) map.get("success")) {
                    Tools.Toast("记账成功");
                    finish();
                } else {
                    Tools.Toast(String.valueOf(map.get("msg")));
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                hideProgressDialog();
            }
        });
    }

    @Override
    protected int layoutRes() {
        return R.layout.activity_tally;
    }

    @Override
    protected void init() {
        initDatePicker();
        mTitlebarTitle.setText(getString(R.string.expend));
        tv_right.setText(new SimpleDateFormat("MM月dd日", Locale.getDefault()).format(Calendar.getInstance().getTimeInMillis()));

        mHandler = new Handler();
        mHandler.postDelayed(() -> {
            adapter = new TextViewAdapter(TallyPresenter.getExpendTypeList());
            adapter.setStringContact(s -> costType = s);
            mRvType.setAdapter(adapter);
            mRvType.setItemAnimator(new DefaultItemAnimator());
            mRvType.setLayoutManager(new StaggeredGridLayoutManager(5, StaggeredGridLayoutManager.VERTICAL));
        }, 0);

        mSpendingSourcesAdapter = new SpendingSourcesAdapter();
        mSpendingSourcesAdapter.setStringContact(s -> waybillNo = s);

        mRvWaybill.setAdapter(mSpendingSourcesAdapter);
        mRvWaybill.setItemAnimator(new DefaultItemAnimator());
        mRvWaybill.setLayoutManager(new LinearLayoutManager(this));

        mEtMoney.setOnClickListener(v -> AppHelper.showSoftInputMethod(v.getContext(), v));
        mEtMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                saveDecimal(s, 3);
            }
        });


        time = CalendarHelper.getCurrentTime();
        loadData(time, time);
    }

    /**
     * @param s     输入的小数
     * @param place 保留小数点后几位
     */
    private void saveDecimal(Editable s, int place) {

        int length = s.length();

        int temp = s.toString().indexOf(".");

        if (temp == 0) {
            s.clear();
        }

        if (temp > 0 && length - temp > place) {
            s.delete(temp + place + 1, length);
        }

    }

    private void initDatePicker() {
        startCalendar.set(2013, 5, 18);
        mBuilder = new TimePickerView.Builder(this, (date, v) -> {
            mSelectedCalendar.setTime(date);

            tv_right.setText(new SimpleDateFormat("MM月dd日", Locale.getDefault()).format(date));

            time = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date);
            loadData(time, time);
        }).setType(new boolean[]{true, true, true, false, false, false})
                .setLabel("", "", "", "", "", "")
                .isCenterLabel(false)
                .setDividerColor(Color.DKGRAY)
                .setContentSize(21)
                .setDate(mSelectedCalendar)
                .setRangDate(startCalendar, Calendar.getInstance())
                .setBackgroundId(R.color.white) //设置外部遮罩颜色
                .setDecorView(null);
        mTimePickerView = mBuilder.build();
    }

    private void loadData(String from, String to) {
        HttpRequestHelper.getWaybillList(from, to, new HttpCallBack() {
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
                IorderListBean bean = new Gson().fromJson(body, IorderListBean.class);
                List<ItemOrderBean> dataList = bean.getWaybillList();
                mSpendingSourcesAdapter.refresh(dataList);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();


        mHandler.removeCallbacksAndMessages(null);
    }

}
