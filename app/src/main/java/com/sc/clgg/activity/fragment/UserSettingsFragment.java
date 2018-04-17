//package com.sc.clgg.activity.fragment;
//
//import android.content.DialogInterface;
//import android.content.Intent;
//import android.text.TextUtils;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.sc.clgg.R;
//import com.sc.clgg.activity.basic.LoginActivity;
//import com.sc.clgg.activity.contact.UserSettingContact;
//import com.sc.clgg.activity.presenter.UserSettingPresenter;
//import com.sc.clgg.activity.usersettings.AboutUsActivity;
//import com.sc.clgg.activity.usersettings.ModifyPasswordActivity;
//import com.sc.clgg.bean.SingleBean;
//import com.sc.clgg.bean.VersionInfoBean;
//import com.sc.clgg.config.ConstantValue;
//import com.sc.clgg.dialog.ExitDialog;
//import com.sc.clgg.util.ConfigUtil;
//import com.sc.clgg.util.Tools;
//import com.sc.clgg.util.UpdateApkUtil;
//
//import butterknife.BindString;
//import butterknife.BindView;
//import butterknife.OnClick;
//import toolbox.helper.ActivityHelper;
//
///**
// * 作者：lvke
// * 创建时间：2017/7/31 14:33
// * 用户设置
// */
//
//public class UserSettingsFragment extends BaseFragment implements UserSettingContact {
//    @BindString(R.string.network_anomaly)
//    String network_anomaly;
//
//    @BindView(R.id.tv_name)
//    TextView tv_name;
//    private UserSettingPresenter presenter;
//
//    @OnClick(R.id.tv_change_password)
//    void e() {
//        startAcMove(new Intent(getActivity(), ModifyPasswordActivity.class));
//    }
//
//    @OnClick(R.id.tv_custom_service)
//    void c() {
//        Tools.callPhone(ConstantValue.SERVICE_TEL, getActivity());
//    }
//
//    @OnClick(R.id.tv_about)
//    void d() {
//        startAcMove(new Intent(getActivity(), AboutUsActivity.class));
//    }
//
//    @OnClick(R.id.exitTxt)
//    void b() {
//        new ExitDialog(getActivity()).show("温馨提示", "确定退出登录？", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface arg0, int arg1) {
//                new ConfigUtil().reset();
//                startActivity(new Intent(getActivity(), LoginActivity.class));
//                getActivity().finish();
//            }
//        });
//    }
//
//    @OnClick(R.id.tv_check_apk_update)
//    void a() {
//        presenter.checkUpdate();
//    }
//
//    @Override
//    protected void init() {
//        super.init();
//        presenter = new UserSettingPresenter(this);
//
//        ConfigUtil mConfigUtil = new ConfigUtil();
//
//        if (!TextUtils.isEmpty(mConfigUtil.getMobile())) {
//            tv_name.setText(mConfigUtil.getMobile());
//        } else {
//            if (!TextUtils.isEmpty(mConfigUtil.getUsername())) {
//                tv_name.setText(mConfigUtil.getUsername());
//            }
//        }
//    }
//
//    @Override
//    public int LayoutRes() {
//        return R.layout.fragment_user_settings;
//    }
//
//    @Override
//    public void getVersionInfo(VersionInfoBean bean) {
//        if (bean == null) {
//            Toast.makeText(getActivity(), network_anomaly, Toast.LENGTH_SHORT).show();
//        } else {
//            SingleBean singleBean = bean.getSingle();
//            if (singleBean != null) {
//                new UpdateApkUtil().checkUpdateInfo(getActivity(), singleBean.getCode(), singleBean.getType(), singleBean.getUrl(), true);
//            }
//        }
//    }
//
//    @Override
//    public void onError(String msg) {
//        Toast.makeText(getActivity(), network_anomaly, Toast.LENGTH_SHORT).show();
//    }
//
//    protected void startAcMove(Intent intent) {
//        startActivity(intent);
//        getActivity().overridePendingTransition(R.anim.push_right_in, R.anim.scale_out);
//    }
//}
