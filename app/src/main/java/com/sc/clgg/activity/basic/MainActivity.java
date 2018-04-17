//package com.sc.clgg.activity.basic;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.suppLort.v7.app.AppCompatActivity;
//import android.text.TextUtils;
//import android.view.KeyEvent;
//import android.view.View;
//import android.widget.TextView;
//
//import com.sc.clgg.R;
//import com.sc.clgg.activity.fragment.TransportManageFragment;
//import com.sc.clgg.activity.fragment.UserSettingsFragment;
//import com.sc.clgg.activity.fragment.VehicleManageFragment;
//import com.sc.clgg.adapter.FragmentAdapter;
//import com.sc.clgg.bean.MessageEvent;
//import com.sc.clgg.dialog.ExitDialog;
//import com.sc.clgg.dialog.SetGpsDialog;
//import com.sc.clgg.util.ConfigUtil;
//import com.sc.clgg.widget.NotSlideViewPager;
//
//import org.greenrobot.eventbus.EventBus;
//import org.greenrobot.eventbus.Subscribe;
//import org.greenrobot.eventbus.ThreadMode;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import butterknife.BindViews;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import toolbox.helper.AppHelper;
//
//
///**
// * 创建时间：2017/7/31 11:20
// *
// * @author lvke
// */
//public class MainActivity extends AppCompatActivity {
//    @BindView(R.id.viewpager)
//    NotSlideViewPager mViewPager;
//
//    @BindView(R.id.tv_unread)
//    TextView mUnReadTv;
//
//    @BindViews({R.id.tv_truck_manage, R.id.tv_transport_manage, R.id.tv_user_setttings})
//    List<TextView> textViews;
//
//    private SetGpsDialog setGpsDialog;
//    /**
//     * 标记当前底部的index
//     */
//    private int currenMainTabIndex;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        ButterKnife.bind(this);
//
//        List<Fragment> fragmentList = new ArrayList<>();
//        fragmentList.add(new VehicleManageFragment());
//        fragmentList.add(new TransportManageFragment());
//        fragmentList.add(new UserSettingsFragment());
//        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragmentList);
//
//        mViewPager.setOffscreenPageLimit(2);
//        mViewPager.setAdapter(fragmentAdapter);
//
//        setGpsDialog = new SetGpsDialog(this);
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (!AppHelper.isGpsOpen(getApplicationContext()) && !setGpsDialog.getDialog().isShowing()) {
//            setGpsDialog.show();
//        }
//    }
//
//    private void showTab(int showIndex, List<TextView> textViews) {
//        for (TextView tv : textViews) {
//            if (textViews.indexOf(tv) == showIndex) {
//                tv.setSelected(true);
//            } else {
//                tv.setSelected(false);
//            }
//        }
//    }
//
//    public int getCurrenMainTabIndex() {
//        return currenMainTabIndex;
//    }
//
//    public void setCurrenMainTabIndex(int currenMainTabIndex) {
//        this.currenMainTabIndex = currenMainTabIndex;
//    }
//
//    @OnClick({R.id.tv_truck_manage, R.id.tv_transport_manage, R.id.tv_user_setttings})
//    public void a(View view) {
//        if (view.getId() == R.id.tv_truck_manage) {
//            mViewPager.setCurrentItem(0, false);
//            setCurrenMainTabIndex(0);
//            showTab(0, textViews);
//        } else if (view.getId() == R.id.tv_transport_manage) {
//            mViewPager.setCurrentItem(1, false);
//            setCurrenMainTabIndex(1);
//            showTab(1, textViews);
//        } else if (view.getId() == R.id.tv_user_setttings) {
//            if (TextUtils.isEmpty(new ConfigUtil().getUserid())) {
//                startActivity(new Intent(this, LoginActivity.class));
//            } else {
//                mViewPager.setCurrentItem(2, false);
//                setCurrenMainTabIndex(2);
//                showTab(2, textViews);
//            }
//        }
//    }
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        EventBus.getDefault().register(this);
//    }
//
//    @Override
//    protected void onStop() {
//        EventBus.getDefault().unregister(this);
//        super.onStop();
//    }
//
//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onMessageEvent(MessageEvent event) {
//        int messageNumber = 99;
//        if (event.getValue() > messageNumber) {
//            mUnReadTv.setText(R.string.more_than_99);
//        } else {
//            mUnReadTv.setText(String.valueOf(event.getValue()));
//        }
//    }
//
//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//
//            new ExitDialog(this).show("退出提示", "确定退出车轮滚滚？", (arg0, arg1) -> System.exit(0));
//
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
//}
