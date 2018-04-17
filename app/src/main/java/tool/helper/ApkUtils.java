package tool.helper;

import android.content.Context;
import android.content.pm.PackageInfo;

/**
 * Author：lvke
 * CreateDate：2017/9/5 11:10
 */

public class ApkUtils {
    /**
     * 获取版本号
     */
    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取版本名称
     */
    public static String getVersionName(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        } catch (Exception e) {
            e.printStackTrace();
            return "0";
        }
    }

    /**
     * 安装包信息
     */
    private PackageInfo getPackageInfo(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
