package com.sc.clgg.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.amap.api.maps.model.LatLng;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import tool.helper.LogHelper;

import static java.util.regex.Pattern.compile;

public class Utils {

    private static final char HEX_DIGITS[] = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static long lastClickTime;

    public static void print(String msg) {
        int maxLogSize = 500;
        for (int i = 0; i <= msg.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i + 1) * maxLogSize;
            end = end > msg.length() ? msg.length() : end;
            LogHelper.e(msg.substring(start, end));
        }
    }

    /**
     * 获取版本
     */
    public static int getVersionCode(Context ctx) {
        PackageInfo pinfo;
        try {
            pinfo = ctx.getPackageManager().getPackageInfo(
                    ctx.getPackageName(), 0);
        } catch (Exception e) {
            return 0;
        }
        return pinfo.versionCode;
    }

    /**
     * 获取版本名称
     */
    public static String getVersionName(Context ctx) {
        PackageInfo pinfo;
        try {

            pinfo = ctx.getPackageManager().getPackageInfo(
                    ctx.getPackageName(), 0);
        } catch (Exception e) {
            return "100";
        }
        return pinfo.versionName;
    }

    public static void openGPSSettings(Context context) {
        // 获取位置服务
        LocationManager lm = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        // 若GPS未开启
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Toast.makeText(context, "请开启GPS！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            context.startActivity(intent);
        }

    }

    public static boolean isExternalStorageMounted() {

        boolean canRead = Environment.getExternalStorageDirectory().canRead();
        boolean onlyRead = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED_READ_ONLY);
        boolean unMounted = Environment.getExternalStorageState().equals(
                Environment.MEDIA_UNMOUNTED);

        return !(!canRead || onlyRead || unMounted);
    }

    public static void saveOrUpdateFile(Bitmap bitmap, String fileName) {
        if (!isExternalStorageMounted()) {
            return;
        }
        String url = fileName;
        BufferedOutputStream os = null;
        try {
            File file = new File(url);
            if (file.exists()) {
                file.delete();
            } else {
                File dir = file.getParentFile();
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
            file.createNewFile();
            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);
        } catch (Exception e) {
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void saveOrUpdateFile(String temp, String fileName) {
        if (!isExternalStorageMounted()) {
            return;
        }
        String url = fileName;
        FileWriter fw = null;
        BufferedWriter bw = null;
        try {
            File file = new File(url);
            if (file.exists()) {
                file.delete();
            } else {
                File dir = file.getParentFile();
                if (!dir.exists()) {
                    dir.mkdirs();
                }
            }
            file.createNewFile();
            fw = new FileWriter(file.getAbsoluteFile());
            bw = new BufferedWriter(fw);
            bw.write(temp);
        } catch (Exception e) {
        } finally {
            try {
                if (bw != null) {
                    bw.close();
                }
                if (fw != null) {
                    fw.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static String getString(InputStream inputStream) {
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStreamReader != null) {
                    inputStreamReader.close();

                }
                if (reader != null) {
                    reader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    public static String getString(String filepath) {
        File file = new File(filepath);
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return getString(fileInputStream);
    }

    // 判断点击的位置是不是在view上
    public static boolean inRangeOfView(View view, MotionEvent ev) {
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        int x = location[0];
        int y = location[1];
        return !(ev.getX() < x || ev.getX() > (x + view.getWidth()) || ev.getY() < y
                || ev.getY() > (y + view.getHeight()));
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        /*
         * 可接受的电话格式有：
		 */
        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
        /*
         * 可接受的电话格式有：
		 */
        String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = compile(expression);
        Matcher matcher = pattern.matcher(inputStr);

        Pattern pattern2 = compile(expression2);
        Matcher matcher2 = pattern2.matcher(inputStr);
        if (matcher.matches() || matcher2.matches()) {
            isValid = true;
        }
        return isValid;

    }

    // 重复点击事件800毫秒
    public static boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 800) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    public static Boolean haveInternet(Context con) {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) con
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connectivityManager
                    .getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable()
                    && networkInfo.isConnected();
            return connected;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connected;
    }

    /**
     * 判断当前日期是星期几<br>
     * <br>
     *
     * @param pTime 修要判断的时间<br>
     * @return dayForWeek 判断结果<br>
     * @Exception 发生异常<br>
     */
    public static int dayForWeek(String pTime) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.setTime(format.parse(pTime));
        int dayForWeek = 0;
        if (c.get(Calendar.DAY_OF_WEEK) == 1) {
            dayForWeek = 7;
        } else {
            dayForWeek = c.get(Calendar.DAY_OF_WEEK) - 1;
        }
        return dayForWeek;
    }

    /**
     * 计算LisetVIew高度
     *
     * @param listView
     */
    public static void getTotalHeightofListView(ListView listView) {
        ListAdapter mAdapter = listView.getAdapter();
        if (mAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < mAdapter.getCount(); i++) {
            View mView = mAdapter.getView(i, null, listView);
            mView.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            // mView.measure(0, 0);
            totalHeight += mView.getMeasuredHeight();
            Log.w("HEIGHT" + i, String.valueOf(totalHeight));
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (mAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static SpannableStringBuilder putstr(String keyword, String strtext) {
        String docInfo = strtext;
        int keywordIndex = strtext.indexOf(keyword);
        SpannableStringBuilder style = new SpannableStringBuilder(docInfo);
        while (keywordIndex != -1) {
            /**
             * 背景色改变
             */
            // style.setSpan(new
            // BackgroundColorSpan(Color.RED),keywordIndex,keywordIndex+keyword.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            /**
             * 关键字颜色改变
             */
            style.setSpan(new ForegroundColorSpan(Color.RED), keywordIndex,
                    keywordIndex + keyword.length(),
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

            int tempkeywordTempIndex = keywordIndex + keyword.length();
            strtext = docInfo.substring(tempkeywordTempIndex, docInfo.length());
            keywordIndex = strtext.indexOf(keyword);
            if (keywordIndex != -1) {
                keywordIndex = keywordIndex + tempkeywordTempIndex;
            }
        }
        return style;
    }

    public static String toHexString(byte[] b) {
        StringBuilder sb = new StringBuilder(b.length * 2);
        for (int i = 0; i < b.length; i++) {
            sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);
            sb.append(HEX_DIGITS[b[i] & 0x0f]);
        }
        return sb.toString();
    }

    public static Bitmap drawableToBitmap(Drawable drawable, int rotate) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        width = Math.max(width, height);
        height = Math.max(width, height);
        Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Config.ARGB_8888
                : Config.RGB_565;
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);

        Canvas canvas = new Canvas(bitmap);
        canvas.save();
        Matrix matrix = new Matrix();
        matrix.setRotate((float) rotate, width / 2.0f, height / 2.0f);
        canvas.setMatrix(matrix);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        canvas.restore();

        return bitmap;
    }

    public static String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest
                    .getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            return toHexString(messageDigest);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static String encode(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            StringBuffer buf = new StringBuffer("");
            for (int i : b) {
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            String res = buf.toString();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    // 判断字符串是都全是数字
    public static boolean isNumeric(String str) {
        Pattern pattern = compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        return isNum.matches();
    }

    public static boolean getKaifa(Activity myActivity) {
        boolean isOpen = Settings.Secure.getInt(
                myActivity.getContentResolver(),
                Settings.Secure.ALLOW_MOCK_LOCATION, 0) != 0;
        return isOpen;
    }

    /**
     * 加密
     *
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     */
    public static byte[] encrypt(String data)
            throws UnsupportedEncodingException {
        byte[] enc = data.getBytes("UTF-8");
        for (int i = 0; i < enc.length; i++) {
            byte b = (byte) (enc[i] ^ 0xa5);
            enc[i] = (byte) ((b << 4) & 0xF0 | (b >> 4) & 0x0F);
        }
        return enc;
    }

    /**
     * 解密
     *
     * @param data
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String unEncrypt(byte[] data)
            throws UnsupportedEncodingException {
        for (int i = 0; i < data.length; i++) {
            byte b = data[i];
            data[i] = (byte) (((b << 4) & 0xF0 | (b >> 4) & 0x0F) ^ 0xA5);
        }
        return new String(data, "UTF-8");// ,"GBK");
    }

    /**
     * install slient
     *
     * @param context
     * @param filePath
     * @return 0 means normal, 1 means file not exist, 2 means other exception
     * error
     */
    public static int installSlient(Context context, String filePath) {
        File file = new File(filePath);
        if (filePath == null || filePath.length() == 0
                || (file = new File(filePath)) == null || file.length() <= 0
                || !file.exists() || !file.isFile()) {
            return 1;
        }

        String[] args = {"pm", "install", "-r", filePath};
        ProcessBuilder processBuilder = new ProcessBuilder(args);

        Process process = null;
        BufferedReader successResult = null;
        BufferedReader errorResult = null;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder errorMsg = new StringBuilder();
        int result;
        try {
            process = processBuilder.start();
            successResult = new BufferedReader(new InputStreamReader(
                    process.getInputStream()));
            errorResult = new BufferedReader(new InputStreamReader(
                    process.getErrorStream()));
            String s;

            while ((s = successResult.readLine()) != null) {
                successMsg.append(s);
            }

            while ((s = errorResult.readLine()) != null) {
                errorMsg.append(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = 2;
        } catch (Exception e) {
            e.printStackTrace();
            result = 2;
        } finally {
            try {
                if (successResult != null) {
                    successResult.close();
                }
                if (errorResult != null) {
                    errorResult.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }

        if (successMsg.toString().contains("Success")
                || successMsg.toString().contains("success")) {
            result = 0;
        } else {
            result = 2;
        }
        Log.d("installSlient", "successMsg:" + successMsg + ", ErrorMsg:"
                + errorMsg);
        return result;
    }

    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        // 源图片的高度和宽度
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width > reqWidth) {
            // 计算出实际宽高和目标宽高的比率
            final int heightRatio = Math.round((float) height
                    / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            // 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
            // 一定都会大于等于目标的宽和高。
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res,
                                                         int resId, int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static Bitmap decodeSampledBitmapFromResource(String url,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(url, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(url, options);
    }

    public static Bitmap decodeSampledBitmapFromResource(byte[] data,
                                                         int reqWidth, int reqHeight) {
        // 第一次解析将inJustDecodeBounds设置为true，来获取图片大小
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(data, 0, data.length, options);
        // 调用上面定义的方法计算inSampleSize值
        options.inSampleSize = calculateInSampleSize(options, reqWidth,
                reqHeight);
        // 使用获取到的inSampleSize值再次解析图片
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(data, 0, data.length, options);
    }

    /*
     * 得到图片字节流 数组大小
     */
    public static byte[] readStream(InputStream inStream) throws Exception {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outStream.write(buffer, 0, len);
        }
        outStream.close();
        inStream.close();
        return outStream.toByteArray();
    }

    /**
     * 隐藏软键盘
     *
     * @param activity
     */
    public static void hideSoftKeyBoard(Activity activity) {
        View view = activity.getCurrentFocus();
        InputMethodManager im = (InputMethodManager) activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromInputMethod(view.getWindowToken(), 0);

    }

    /**
     * 获取屏宽
     *
     * @param activity
     * @return
     */
    public static int getScreenWidth(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 获取屏密度
     *
     * @param activity
     * @return
     */
    public static float getScreenDensity(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

    /**
     * 获取屏高宽
     *
     * @param activity
     * @return
     */
    public static int getScreenHeight(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 得到设备屏幕密度
     *
     * @param activity
     * @return
     */
    public static float getDeviceDensity(Activity activity) {
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.density;
    }

    /**
     * 拨号
     *
     * @param context
     * @param phone
     */
    public static void dialPhone(Context context, String phone) {
        Intent intent = new Intent(Intent.ACTION_DIAL,
                Uri.parse("tel:" + phone));
        context.startActivity(intent);
    }

    /**
     * 判断经纬坐标是否正确
     *
     * @param latLng
     * @return
     */
    public static boolean isViladLatLng(LatLng latLng) {
        if (latLng.latitude < 0 || latLng.latitude > 90) {
            return false;
        }
        return !(latLng.longitude < 0 || latLng.longitude > 180);

    }

    /**
     * @param view
     * @param bitmapWidth
     * @param bitmapHeight
     * @return
     */
    public static Bitmap convertViewToBitmap(View view, int bitmapWidth, int bitmapHeight) {
        Bitmap bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Config.ARGB_8888);
        view.draw(new Canvas(bitmap));

        return bitmap;
    }

    public static Bitmap megerBitmap(Bitmap topBitmap, Bitmap bottomBitmap) {
        if (topBitmap == null && bottomBitmap != null) {
            System.out.println("topBitmap:--->null");
            return bottomBitmap;
        }

        if (topBitmap != null && bottomBitmap == null) {
            System.out.println("bottomBitmap:--->null");
            return topBitmap;
        }

        if (topBitmap == null && bottomBitmap == null) {
            System.out.println("topBitmap bottomBitmap:--->null");
            return null;
        }

        int width = Math.max(topBitmap.getWidth(), bottomBitmap.getWidth());
        int height = topBitmap.getHeight() + bottomBitmap.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        if (topBitmap.getWidth() >= bottomBitmap.getWidth()) {
            canvas.drawBitmap(topBitmap, 0, 0, null);
        } else {
            canvas.drawBitmap(topBitmap, (bottomBitmap.getWidth() - topBitmap.getWidth()) / 2, 0, null);
        }

        if (bottomBitmap.getWidth() >= topBitmap.getWidth()) {
            canvas.drawBitmap(bottomBitmap, 0, topBitmap.getHeight(), null);
        } else {
            canvas.drawBitmap(bottomBitmap, (topBitmap.getWidth() - bottomBitmap.getWidth()) / 2, topBitmap.getHeight(), null);
        }
        return bitmap;
    }

    /*
     * android图片缩放（指定大小）
     */
    private Drawable zoomDrawable(Drawable drawable, int w, int h) {
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        Bitmap oldbmp = drawableToBitmap(drawable, 0);
        Matrix matrix = new Matrix();
        float scaleWidth = ((float) w / width);
        float scaleHeight = ((float) h / height);
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,
                matrix, true);
        return new BitmapDrawable(null, newbmp);
    }
}
