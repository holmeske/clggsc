//package com.sc.clgg.activity.fragment;
//
//import android.content.Context;
//import android.os.Environment;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.bumptech.glide.Glide;
//import com.sc.clgg.R;
//import com.sc.clgg.activity.contact.TruckManageContact;
//import com.sc.clgg.activity.presenter.TruckManagePresenter;
//import com.sc.clgg.activity.vehiclemanager.FinancialAftermarketActivity;
//import com.sc.clgg.activity.vehiclemanager.drivingscore.DrivingScoreActivity;
//import com.sc.clgg.activity.vehiclemanager.maintenance.MaintenanceHomeActivity;
//import com.sc.clgg.activity.vehiclemanager.monitor.VehicleMonitorActivity;
//import com.sc.clgg.activity.vehiclemanager.myvehicle.MyVehicleActivity;
//import com.sc.clgg.activity.vehiclemanager.tallybook.MyTallyBookActivity;
//import com.sc.clgg.application.App;
//import com.sc.clgg.bean.SingleBean;
//import com.sc.clgg.bean.VersionInfoBean;
//import com.sc.clgg.util.Tools;
//import com.sc.clgg.util.UpdateApkUtil;
//import com.sc.clgg.widget.HomeButton;
//import com.youth.banner.Banner;
//import com.youth.banner.loader.ImageLoader;
//
//import java.io.File;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import toolbox.helper.ActivityHelper;
//import toolbox.helper.LogHelper;
//import toolbox.helper.MeasureUtils;
//
//import static com.sc.clgg.application.App.screenWidth;
//
///**
// * 创建时间：2017/7/31 10:06
// * 车辆管理
// *
// * @author lvke
// */
//
//public class VehicleManageFragment extends BaseFragment implements TruckManageContact {
//
//    @BindView(R.id.banner) Banner mBanner;
//    @BindView(R.id.ll_one) LinearLayout llOne;
//    @BindView(R.id.ll_two) LinearLayout llTwo;
//
//    private HomeButton.HomeButtonOnClickListener mHomeButtonOnClickListener = v -> {
//        switch (v.getId()) {
//            case R.id.hb_vehicle_monitor:
//                ActivityHelper.startAcScale(getActivity(), VehicleMonitorActivity.class);
//                break;
//            case R.id.hb_my_vehicle:
//                ActivityHelper.startAcScale(getActivity(), MyVehicleActivity.class);
//                break;
//            case R.id.hb_driving_score:
//                ActivityHelper.startAcScale(getActivity(), DrivingScoreActivity.class);
//                break;
//            case R.id.hb_maintenance_home:
//                ActivityHelper.startAcScale(getActivity(), MaintenanceHomeActivity.class);
//                break;
//            case R.id.hb_my_tallybook:
//                ActivityHelper.startAcScale(getActivity(), MyTallyBookActivity.class);
//                break;
//            case R.id.hb_financial_after_market:
//                ActivityHelper.startAcScale(getActivity(), FinancialAftermarketActivity.class);
//                break;
//
//            default:
//                break;
//        }
//    };
//
//    @Override
//    protected void init() {
//        super.init();
//        ((TextView) getView().findViewById(R.id.tv_title)).setText(R.string.accom_car);
//
//        Tools.getScreenInfo(App.getInstance());
//
//        initOnClick();
//
//        TruckManagePresenter presenter = new TruckManagePresenter(this);
//        presenter.checkUpdate();
//        presenter.addressArrange(App.getInstance());
//
//        List<Integer> list = new ArrayList<>();
//        list.add(R.drawable.banner_01);
//        //banner 原始宽高：1242px * 692px     屏幕宽高:width*height
//        mBanner.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 692 * screenWidth / 1242));
//        mBanner.setImages(list).setImageLoader(new GlideImageLoader()).setOnBannerListener(position -> LogHelper.e("点击了第" + position + "张")).start();
//    }
//
//    @Override
//    public int LayoutRes() {
//        return R.layout.fragment_vehicle_manage;
//    }
//
//    private void initOnClick() {
//        int height = new MeasureUtils().getScreenWH(App.getInstance())[1];
//        float h = height - MeasureUtils.dp2px(App.getInstance(), 44 + 231 + 45);
//
//        ((LinearLayout.LayoutParams) llOne.getLayoutParams()).height = (int) (h / 2);
//
//        ((LinearLayout.LayoutParams) llTwo.getLayoutParams()).height = (int) (h / 2);
//
//        ((HomeButton) mView.findViewById(R.id.hb_vehicle_monitor)).setHomeButtonOnClickListener(mHomeButtonOnClickListener);
//        ((HomeButton) mView.findViewById(R.id.hb_my_vehicle)).setHomeButtonOnClickListener(mHomeButtonOnClickListener);
//        ((HomeButton) mView.findViewById(R.id.hb_driving_score)).setHomeButtonOnClickListener(mHomeButtonOnClickListener);
//
//        ((HomeButton) mView.findViewById(R.id.hb_maintenance_home)).setHomeButtonOnClickListener(mHomeButtonOnClickListener);
//        ((HomeButton) mView.findViewById(R.id.hb_my_tallybook)).setHomeButtonOnClickListener(mHomeButtonOnClickListener);
//        ((HomeButton) mView.findViewById(R.id.hb_financial_after_market)).setHomeButtonOnClickListener(mHomeButtonOnClickListener);
//    }
//
//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isViewCreated) {
//            if (isVisibleToUser) {
//                //开始轮播
//                mBanner.startAutoPlay();
//            } else {
//                //结束轮播
//                mBanner.stopAutoPlay();
//            }
//        }
//    }
//
//    @Override
//    public void getVersionInfo(VersionInfoBean bean) {
//        String path = Environment.getExternalStorageDirectory().getPath() + Environment.getDataDirectory() + File.separator
//                + getActivity().getPackageName() + Environment.getDownloadCacheDirectory() + File.separator + "clggsc.apk";
//
//        LogHelper.e(path);
//
//        SingleBean singleBean = bean.getSingle();
//        if (singleBean != null) {
//            new UpdateApkUtil().checkUpdateInfo(getActivity(), singleBean.getCode(), singleBean.getType(), singleBean.getUrl(), false);
//        }
//    }
//
//    private class GlideImageLoader extends ImageLoader {
//        @Override
//        public void displayImage(Context context, Object path, ImageView imageView) {
//            /*注意：
//             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
//             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
//             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
//             切记不要胡乱强转！*/
//
//            Glide.with(context).load(path).into(imageView);//Glide 加载图片简单用法
//
//            /*//Picasso 加载图片简单用法
//            Picasso.with(context).load(path).into(imageView);
//
//            //用fresco加载图片简单用法，记得要写下面的createImageView方法
//            Uri uri = Uri.parse((String) path);
//            imageView.setImageURI(uri);*/
//        }
//
//    }
//}
