package com.sc.clgg.activity.vehiclemanager.maintenance;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.sc.clgg.R;
import com.sc.clgg.activity.contact.BusinessInfoContact;
import com.sc.clgg.activity.presenter.BusinessInfoPresenter;
import com.sc.clgg.adapter.NewsInfoAdapter;
import com.sc.clgg.adapter.ServiceInfoAdapter;
import com.sc.clgg.application.App;
import com.sc.clgg.base.BaseActivity;
import com.sc.clgg.bean.NewsBean;
import com.sc.clgg.bean.ServiceBean;
import com.sc.clgg.bean.StoreInfoBean;
import com.sc.clgg.bean.StoreInfoDetailBean;
import com.sc.clgg.util.ConfigUtil;
import com.sc.clgg.util.DialogUtil;
import com.sc.clgg.util.MapUtil;
import com.sc.clgg.util.Tools;
import com.sc.clgg.view.ScrollViewListView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;
import tool.helper.LogHelper;

public class BusinessInfoActivity extends BaseActivity implements BusinessInfoContact {
    @BindView(R.id.news_lv) ScrollViewListView news_lv;
    @BindView(R.id.business_lv) ScrollViewListView business_lv;
    @BindView(R.id.company_img) ImageView company_img;// 左侧图片
    @BindView(R.id.company_name) TextView company_name;// 公司名称
    @BindView(R.id.company_instance) TextView company_instance;// 距离
    @BindView(R.id.company_position) TextView company_position;// 位置
    @BindView(R.id.company_phone) TextView company_phone;// 电话
    @BindView(R.id.company_person) TextView company_person;// 联系人
    @BindView(R.id.tv_tel) TextView tv_tel;// 联系人
    @BindView(R.id.tv_map) TextView tv_map;// 联系人
    @BindView(R.id.tv_service_type) TextView tv_service_type;// 服务类型

    @BindView(R.id.line_tel) LinearLayout line_tel;// 打电话
    @BindView(R.id.line_map) LinearLayout line_map;// 查看地图

    private StoreInfoBean sb;
    private ServiceInfoAdapter bInfoAdapter;
    private NewsInfoAdapter nInfoAdapter;
    private ArrayList<ServiceBean> svresult;
    private ArrayList<NewsBean> newsresult;

    private BusinessInfoPresenter presenter;
    private Class cls;

    @Override
    protected int layoutRes() {
        return R.layout.activity_business_info;
    }

    @Override
    protected void init() {
        presenter = new BusinessInfoPresenter(this);

        cls = getIntent().getParcelableExtra("cls");
//        LogHelper.v("主界面的 = " + cls.getSimpleName());

        LogHelper.v("obj = " + App.mLocationBean.getObj());
        sb = new Gson().fromJson(App.mLocationBean.getObj(), StoreInfoBean.class);
        if (sb == null) {
            return;
        }
        updateUI();
        consumeTimeTask();
    }

    private void updateUI() {

        presenter.loadData(new ConfigUtil().getUserid(), sb.getId());

        Glide.with(this).setDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.picture).error(R.drawable.picture)).load(sb.getHead()).into(company_img);

        company_name.setText(sb.getName());

        company_instance.setText(getString(R.string.placeholders_two, Tools.calculateTwoDotsDistance(sb.getLat(), sb.getLng()), "km"));

        company_position.setText(sb.getAddress());

        company_phone.setText(Tools.isNull(sb.getPhone()) ? getString(R.string.now_no_phone_number) : sb.getPhone());

        if (Tools.isNull(sb.getContact())) {
            company_person.setText(getString(R.string.placeholders_two, getString(R.string.connection_person), getString(R.string.now_no)));
            line_tel.setBackgroundResource(R.drawable.radius_blue_white);
            tv_tel.setTextColor(ContextCompat.getColor(this, R.color.text_color_selecter));
        } else {
            if (Tools.isNull(sb.getPhonenum())) {
                company_person.setText(getString(R.string.placeholders_two, getString(R.string.connection_person), sb.getContact()));
                line_tel.setBackgroundResource(R.drawable.radius_blue_white);
                tv_tel.setTextColor(ContextCompat.getColor(this, R.color.text_color_selecter));
            } else {
                company_person.setText(getString(R.string.placeholders_two, getString(R.string.connection_person), sb.getContact() + "(" + sb.getPhonenum() + ")"));
            }
        }

        if (cls == MaintenanceActivity.class) {
            tv_service_type.setText("维修服务");
        } else if (cls == FuelGasActivity.class) {
            tv_service_type.setText("今日油价");
        } else if (cls == LogisticsParkActivity.class) {
            tv_service_type.setText("停车服务");
        }
    }

    private void consumeTimeTask() {
        line_tel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Tools.isNull(sb.getPhonenum())) {
                    Tools.callPhone(sb.getPhonenum(), BusinessInfoActivity.this);
                } else if (!Tools.isNull(sb.getPhone())) {
                    Tools.callPhone(sb.getPhone(), BusinessInfoActivity.this);
                }
            }
        });

        // 经纬度是否有效
        if (sb.getLat() <= 0 || sb.getLng() <= 0) {
            tv_map.setTextColor(ContextCompat.getColor(this, R.color.text_color_selecter));
        } else {
            // 地图导航
            line_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<ServiceBean> list = MapUtil.getMapApp(BusinessInfoActivity.this);
                    if (!list.isEmpty()) {
                        DialogUtil.showCustomDialog(BusinessInfoActivity.this, sb, App.screenWidth, ActionBar.LayoutParams.WRAP_CONTENT, R.style.Theme_dialog, list);
                    }
                }
            });
        }

        // 新闻信息
        newsresult = new ArrayList<>();
        nInfoAdapter = new NewsInfoAdapter(this);
        nInfoAdapter.setmDatas(newsresult);
        news_lv.setAdapter(nInfoAdapter);

        // 服务信息
        svresult = new ArrayList<>();
        bInfoAdapter = new ServiceInfoAdapter(this);
        bInfoAdapter.setmDatas(svresult);
        business_lv.setAdapter(bInfoAdapter);
    }

    @OnClick({R.id.company_position, R.id.company_phone, R.id.company_person})
    void a(View v) {
        switch (v.getId()) {
            case R.id.company_position:
            case R.id.company_phone:
            case R.id.company_person:
                if (App.mLocationBean.getFlag()) {
                    // 地图页面
                    Intent intent = new Intent(this, MapActivity.class);
                    if (cls == MaintenanceActivity.class) {
                        intent.putExtra("title", getString(R.string.maintenance));
                    } else if (cls == FuelGasActivity.class) {
                        intent.putExtra("title", getString(R.string.fuel_gas));
                    } else if (cls == LogisticsParkActivity.class) {
                        intent.putExtra("title", getString(R.string.logistics_park));
                    }
                    intent.putExtra("cls", cls);
                    startActivity(intent);
                    overridePendingTransition(R.anim.push_right_in, R.anim.scale_out);
                    finish();
                } else {
                    onBackPressed();
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(R.anim.push_right_in, R.anim.alpha_out);
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter = null;
    }

    @Override
    public void onSuccess(StoreInfoDetailBean bean) {
        newsresult = bean.getNewsresult();
        nInfoAdapter.setmDatas(newsresult);
        nInfoAdapter.notifyDataSetChanged();

        if (cls != null && cls != FuelGasActivity.class) {
            svresult = bean.getSvresult();
            bInfoAdapter.setmDatas(svresult);
            bInfoAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onError(String msg) {
        Toast.makeText(getApplicationContext(), R.string.network_anomaly, Toast.LENGTH_SHORT).show();
    }
}
