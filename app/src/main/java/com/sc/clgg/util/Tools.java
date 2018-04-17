package com.sc.clgg.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.sc.clgg.application.App;
import com.sc.clgg.config.ConstantValue;

import org.apache.http.util.EncodingUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 工具类
 *
 * @author Michael.Zhang 2013-07-31 11:43:21
 */
public class Tools {

    private static final double EARTH_RADIUS = 6378137; // 地球平均半径 单位米

    /**
     * 计算两点间的距离
     */
    public static float calculateLineDistance(Double Lat, Double Lng) {
        LatLng start = new LatLng(App.mLocationBean.getLatitude(), App.mLocationBean.getLongitude());
        LatLng end = new LatLng(Lat, Lng);
        return AMapUtils.calculateLineDistance(start, end) / 1000;
    }

    /**
     * 计算两点间的距离
     */
    public static String calculateTwoDotsDistance(Double Lat, Double Lng) {
        LatLng start = new LatLng(App.mLocationBean.getLatitude(), App.mLocationBean.getLongitude());
        LatLng end = new LatLng(Lat, Lng);
        return saveDecimal1(AMapUtils.calculateLineDistance(start, end) / 1000);
    }

    /**
     * 拨打电话
     *
     * @author ZhangYi 2014年12月5日 上午11:11:19
     */
    public static void callPhone(String telphone, Activity activity) {
        Uri uri = Uri.parse("tel:" + telphone);
        Intent intent = new Intent(Intent.ACTION_DIAL, uri);
        activity.startActivity(intent);
    }

    /**
     * 将dp类型的尺寸转换成px类型的尺寸
     */
    public static int dip2px(Context context, int dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }


    /**
     * 将时间戳转为字符串到日 yyyy-MM-dd
     *
     * @param cc_time
     * @return
     * @author Michael.Zhang 2013-08-05 14:09:17
     */
    public static String get_Day(String cc_time) {
        return getStrTime(cc_time, "yyyy-MM-dd");
    }

    /**
     * 从Assets中读取图片
     */
    public static Bitmap getImageFromAssets(Activity activity, String fileName) {
        Bitmap image = null;
        AssetManager am = activity.getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;
    }

    /**
     * 获得hashmap中value的值,以List 返回
     *
     * @param hashMap
     * @return
     * @author Michael.Zhang 2013-08-21 13:56:07
     */
    public static List<Object> getListFromHashMap(HashMap<String, String> hashMap) {
        List<Object> list = new ArrayList<Object>();
        Iterator<?> iter = hashMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            list.add(entry.getValue());
        }
        return list;
    }

    /**
     * 时间得到时间戳
     *
     * @param time
     * @return
     * @author ZhangYi 2014年7月18日 下午2:34:53
     */
    public static long getLongTime(String time) {
        SimpleDateFormat sDate = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = sDate.parse(time);
            return date.getTime();// date转成毫秒
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }


    /**
     * 安装包信息
     *
     * @author Michael.Zhang 2014-1-20 下午4:11:36
     */
    public static PackageInfo getPackageInfo(Activity activity) {
        String packageName = activity.getPackageName();
        try {
            return activity.getPackageManager().getPackageInfo(packageName, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取屏幕尺寸
     *
     * @author Michael.Zhang 2013-10-31 下午5:16:01
     */
    public static void getScreenInfo(Context context) {
        ConstantValue.SCREEN_HEIGHT = context.getResources().getDisplayMetrics().heightPixels;
        ConstantValue.SCREEN_WIDHT = context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 将时间戳转为字符串到日 yyyy-MM-dd
     *
     * @param cc_time
     * @return
     * @author Michael.Zhang 2013-08-05 14:09:17
     */
    public static String getStrDay(String cc_time) {
        return getStrTime(cc_time, "yyyy年MM月dd日");
    }

    /**
     * 将时间戳转为时分字符串
     *
     * @param cc_time
     * @return
     * @author Michael.Zhang 2013-08-05 14:09:23
     */
    public static String getStrHour(String cc_time) {
        return getStrTime(cc_time, "HH:mm");
    }

    /**
     * 将毫秒数转为时分秒
     *
     * @param cc_time 毫秒数
     * @return
     * @author ZhangYi 2014年5月9日 下午10:06:16
     */
    public static String getStrHourMinute(long cc_time, boolean isHour) {
        int total_second = (int) (cc_time / 1000); // 总秒
        int second = total_second % 60;// 剩余秒
        int minute = total_second / 60; // 总分

        if (isHour) {
            int hour = minute / 60;// 剩余时
            minute = minute % 60; // 剩余分
            return ((hour < 10) ? "0" + hour : "" + hour) + ":" + ((minute < 10) ? "0" + minute : "" + minute) + ":"
                    + ((second < 10) ? "0" + second : "" + second);
        } else {
            return ((minute < 10) ? "0" + minute : "" + minute) + ":" + ((second < 10) ? "0" + second : "" + second);
        }
    }

    /**
     * 将时间戳转为字符串到月 yyyy年MM月
     *
     * @param cc_time
     * @return
     * @author ZhangYi 2014年11月28日 下午6:45:02
     */
    public static String getStrMonth(String cc_time) {
        return getStrTime(cc_time, "yyyy年MM月");
    }

    /**
     * 将时间戳转为字符串 到分
     *
     * @param cc_time
     * @return
     * @author Michael.Zhang 2013-08-05 14:09:23
     */
    public static String getStrTime(String cc_time) {
        return getStrTime(cc_time, "yyyy年MM月dd日 HH:mm");
    }

    public static String getStrTime(String cc_time, String format) {
        if (null == cc_time) {
            cc_time = System.currentTimeMillis() + "";
        }

        if (cc_time.length() == 10) { // 单位 秒
            cc_time += "000";// 单位 毫秒
        }

        long lcc_time = Long.valueOf(cc_time);
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date(lcc_time));
    }

    /**
     * 从assets 文件夹中获取文件并读取数据
     *
     * @param fileName
     * @return
     * @author Michael.Zhang 2014-1-20 下午4:14:27
     */
    public static String getTextFromAssets(Context context, String fileName) {
        String result = null;
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 从res中的raw文件夹中获取文件并读取数据
     *
     * @param context
     * @param resId
     * @return
     * @author Michael.Zhang 2014-1-20 下午4:14:23
     */
    public static String getTextFromRaw(Context context, int resId) {
        String result = null;
        try {
            InputStream in = context.getResources().openRawResource(resId);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            result = EncodingUtils.getString(buffer, "utf-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取版本号
     *
     * @return
     * @author Michael.Zhang 2013-08-31 18:47:09
     */
    public static int getVersionCode(Activity activity) {
        int versionCode = 0;
        if (getPackageInfo(activity) != null) {
            versionCode = getPackageInfo(activity).versionCode;
        }

        return versionCode;
    }

    /**
     * 获取版本名称
     *
     * @return
     * @author Michael.Zhang 2013-08-29 22:14:37
     */
    public static String getVersionName(Activity activity) {
        String versionName = "0";
        if (getPackageInfo(activity) != null) {
            versionName = getPackageInfo(activity).versionName;
        }

        return versionName;
    }

    /**
     * 获取保存到View的Tag中的字符串
     *
     * @param v
     * @return
     */
    public static String getViewTagString(View v) {
        try {
            return v.getTag().toString();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 根据两点经纬度计算距离(<1000 单位米, >1000 单位千米)
     *
     * @param lat_a
     * @param lng_a
     * @param lat_b
     * @param lng_b
     * @return
     * @author ZhangYi 2014年9月11日 下午3:58:40
     */
    public static int gps2distance(double lat_a, double lng_a, double lat_b, double lng_b) {
        double radLat1 = lat_a * Math.PI / 180.0;
        double radLat2 = lat_b * Math.PI / 180.0;
        double a = radLat1 - radLat2;
        double b = lng_a * Math.PI / 180.0 - lng_b * Math.PI / 180.0;
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
        distance = distance * EARTH_RADIUS;

        return (int) distance;
        // if (distance >= 1000) {
        // /* >=1000 单位千米 */
        // return String.format("%.2f", distance / 1000) + "km";
        // } else {
        // /* <1000 单位米 */
        // return String.format("%.2f", distance) + "m";
        // }
    }

    /**
     * 加密身份证号
     */
    public static String IDNum(String idNum) {
        String encryption = idNum.substring(0, 1) + "****************" + idNum.substring(idNum.length() - 1);
        return encryption;
    }

    /**
     * 判断当前网络是否是3G网络.
     *
     * @param context
     * @return boolean
     * @author Michael.Zhang 2014-3-26 11:31:10
     */
    public static boolean is3G(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    /**
     * 简单的验证一下银行卡号
     *
     * @param bankCard 信用卡是16位，其他的是13-19位
     * @return
     */
    public static boolean isBankCard(String bankCard) {
        if (isNull(bankCard))
            return false;
        String pattern = "^\\d{13,19}$";
        return bankCard.matches(pattern);
    }

    /**
     * 简单验证一下车牌号
     *
     * @param car_license
     * @return
     * @author ZhangYi 2015年1月7日 上午10:49:18
     */
    public static boolean isCarLicense(String car_license) {
        if (isNull(car_license)) {
            return false;
        }
        return car_license.matches("^[\u4e00-\u9fa5]{1}[A-Z]{1}[A-Z0-9]{5}$");
    }

    /**
     * 判断时间前后
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isDateBefore(String date1, String date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 得到指定模范的时间
        Date d1;
        Date d2;
        try {
            d1 = sdf.parse(date1);
            d2 = sdf.parse(date2);
            return d1.before(d2);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 判断email格式是否正确
     *
     * @param email
     * @return
     * @author Michael.Zhang 2013-10-31 下午3:17:37
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 判断 列表是否为空
     *
     * @return true为null或空; false不null或空
     */
    public static boolean isEmptyList(List<?> list) {
        return null == list || list.size() == 0;
    }

    /**
     * 验证身份证号码
     *
     * @param idCard 身份证号码
     * @return
     * @author Michael.Zhang 2014-1-20 16:22:01
     */
    public static boolean isIdCard(String idCard) {
        if (isNull(idCard))
            return false;
        String pattern = "^[0-9]{17}[0-9|xX]{1}$";
        return idCard.matches(pattern);
    }

    /**
     * 判断图片路径
     *
     * @return
     * @author Michael.Zhang 2013-11-8 下午7:50:30
     */
    public static boolean isImgUrl(String imgUrl) {
        return isUrl(imgUrl) && (imgUrl.endsWith(".jpg") || imgUrl.endsWith(".png") || imgUrl.endsWith(".JPG") || imgUrl.endsWith(".PNG"));
    }

    /**
     * 判断是否有网络
     *
     * @param context
     * @return
     * @author Michael.Zhang 2013-12-29 16:21:05
     */
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
        return (mNetworkInfo != null && mNetworkInfo.isAvailable());
    }

    /**
     * 判断 多个字段的值否为空
     *
     * @return true为null或空; false不null或空
     * @author Michael.Zhang 2013-08-02 13:34:43
     */
    public static boolean isNull(String... ss) {
        for (int i = 0; i < ss.length; i++) {
            if (null == ss[i] || ss[i].equals("") || ss[i].equalsIgnoreCase("null")) {
                return true;
            }
        }

        return false;
    }

    /**
     * 判断 一个字段的值否为空
     *
     * @param s
     * @return
     * @author Michael.Zhang 2013-9-7 下午4:39:00
     */
    public static boolean isNull(String s) {
        return null == s || s.equals("") || s.equalsIgnoreCase("null");
    }

    /**
     * 判断正则表达式
     *
     * @return
     * @author libo 2013-08-31 18:47:09
     */
    public static String isPassNO(String pwd) {
        String res = "YES";
        if (pwd.length() < 8 || pwd.length() > 17) {
            res = "密码长度应在8-16位，当前" + pwd.length() + "位！";
        }

        if (pwd.matches("[0-9]+")) {
            res = "密码不能全部为数字";
        }

        if (pwd.matches("[a-zA-Z]+")) {
            res = "密码不能全部为字母";
        }

        String s = "[~!@#$%^&*(){}_\\-+<>?:,./;'\"，。、‘：“《》？~！@#￥%……（）]+";
        if (pwd.matches(s)) {
            res = "密码不能全部为符号";
        }
        return res;
    }

    /**
     * 验证手机号码
     *
     * @param phone
     * @return
     * @author Michael.Zhang 2014-1-20 16:22:01
     */
    public static boolean isPhone(String phone) {
        if (isNull(phone)) {
            return false;
        }
        String pattern = "^((13[0-9])|(147)|(17[0-9])|(15[0-9])|(18[0-9]))\\d{8}$";
        return phone.matches(pattern);
    }

    /**
     * 判断外部存储器是否存在
     *
     * @return
     * @author Michael.Zhang 2013-07-04 11:30:54
     */
    public static boolean isSDCardExist() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断 http 链接
     *
     * @param url
     * @return
     * @author Michael.Zhang
     */
    public static boolean isUrl(String url) {
        return null != url && url.startsWith("http://");
    }

    /**
     * 判断当前网络是否是wifi网络.
     *
     * @param context
     * @return boolean
     * @author Michael.Zhang 2014-3-26 11:31:10
     */
    public static boolean isWifi(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return (activeNetInfo != null && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI);
    }

    /**
     * MD5加密
     *
     * @param plainText
     * @return
     * @author libo 2014年7月2日 下午6:06:11
     */
    public static String MD5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0) {
                    i += 256;
                }

                if (i < 16) {
                    buf.append("0");
                }
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将px类型的尺寸转换成dp类型的尺寸
     */
    public static int px2dip(Context context, int pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * double型四舍五入
     *
     * @param scale 小数点后位数
     */
    public static Double roundDouble(double value, int scale) {
        BigDecimal b = ((0 == value) ? new BigDecimal("0") : new BigDecimal(Double.toString(value)));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 保留一位小数
     */
    public static String saveDecimal1(Object object) {
        DecimalFormat df = new DecimalFormat("#####0.0");
        return df.format(object);
    }

    /**
     * 调用系统邮件编辑器
     *
     * @author ZhangYi 2014年8月21日 下午6:05:38
     */
    public static void sendEmial(Context context, String email, String title, String content) {
        Intent data = new Intent(Intent.ACTION_SENDTO);
        data.setData(Uri.parse(email));
        data.putExtra(Intent.EXTRA_SUBJECT, title);
        data.putExtra(Intent.EXTRA_TEXT, content);
        context.startActivity(data);
    }

    /**
     * 调用系统短息编辑器
     *
     * @author ZhangYi 2014年8月21日 下午6:05:20
     */
    public static void sendSMS(Context context, String phone, String content) {
        Uri smsToUri = Uri.parse("smsto:" + phone);// 联系人电话号码
        Intent mIntent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
        mIntent.putExtra("sms_body", content);// 短信内容
        context.startActivity(mIntent);
    }

    /**
     * 弹出窗 Toast
     *
     * @param context
     * @author Michael.Zhang 2014-1-20 下午4:08:54
     */
    public static void Toast(Context context, String content) {
        if (!isNull(content) && null != context) {
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show();
        }
    }

    public static void Toast(String content) {
        if (!TextUtils.isEmpty(content)) {
            Toast.makeText(App.getInstance(), content, Toast.LENGTH_SHORT).show();
        }
    }

    public static int getWindowWidth(Activity mContext) {
        WindowManager manager = mContext.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.widthPixels;
    }

    public static int getWindowHeight(Activity mContext) {
        WindowManager manager = mContext.getWindowManager();
        DisplayMetrics outMetrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }

}
