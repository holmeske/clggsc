package com.sc.clgg.util;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.View;
import android.widget.ProgressBar;

import com.sc.clgg.R;
import com.sc.clgg.application.App;
import com.sc.clgg.tool.helper.ApkUtils;
import com.sc.clgg.tool.helper.LogHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author lvke
 */
public class UpdateApkUtil {

    private final static int DOWN_UPDATE = 1;
    private final static int DOWN_OVER = 2;
    private Context context;
    private String apkUrl;
    private ProgressBar progressBar;
    private int progress;
    private AlertDialog mDownLoadDialog;
    private String parentPath = Environment.getExternalStorageDirectory().getPath() + "/clggsc/apk/";
    private String saveFilePath = Environment.getExternalStorageDirectory().getPath() + "/clggsc/apk/" + "clgg" + ".apk";
    /**
     * 更新类型 1：手动更新 2：强制自动更新
     */
    private int type;
    /**
     * 是否继续下载
     */
    private boolean isCountinue = true;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    progressBar.setProgress(progress);
                    break;
                case DOWN_OVER:
                    mDownLoadDialog.dismiss();
                    installApk();
                    break;
            }
        }
    };

    public UpdateApkUtil() {

    }


    public void checkUpdateInfo(Context context, String versionCode, int type, String url, boolean showToast) {
        this.context = context;
        this.type = type;
        this.apkUrl = url;
        int netVerCode = Integer.parseInt(versionCode);
        if (netVerCode > 0) {
            if (ApkUtils.getVersionCode(App.getInstance()) < netVerCode) {
                showUpdatDialog();
            } else {
                if (showToast) {
                    Tools.Toast("当前已是最新版");
                }
            }
        }
    }

    private void downloadApk() {
//        String saveFilePath = Environment.getExternalStorageDirectory().getPath() + File.separator + "Android"
//                + Environment.getDataDirectory() + File.separator + context.getPackageName()
//                + Environment.getDownloadCacheDirectory().getPath() + File.separator + "clggsc.apk";

//        saveFilePath = Environment.getDataDirectory()+"/"+Environment.getDownloadCacheDirectory().getPath() + "/" + System.currentTimeMillis() + ".apk";

        new Thread(new Runnable() {

            @Override
            public void run() {
                File apk = new File(saveFilePath);
                File parentFile = new File(parentPath);
                if (!parentFile.exists()) {
                    if (parentFile.mkdirs()) {
                        requestApkUrl(apk);
                    } else {
                        LogHelper.e("创建文件夹失败");
                    }
                } else {
                    requestApkUrl(apk);
                }
            }
        }).start();
    }

    private void requestApkUrl(File apk) {
        try {
            URL url = new URL(apkUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.connect();
            int length = conn.getContentLength();
            InputStream is = conn.getInputStream();

            FileOutputStream fos = new FileOutputStream(apk);

            int count = 0;
            byte buf[] = new byte[1024];

            do {
                int numRead = is.read(buf);
                count += numRead;
                progress = (int) (((float) count / length) * 100);
                // 更新进度
                mHandler.sendEmptyMessage(DOWN_UPDATE);
                LogHelper.e("numRead = " + numRead);
                if (numRead <= 0) {
                    // 下载完成通知安装
                    mHandler.sendEmptyMessage(DOWN_OVER);
                    break;
                }
                fos.write(buf, 0, numRead);
            } while (isCountinue);

            fos.close();
            is.close();

        } catch (MalformedURLException e) {
            LogHelper.e(e);
        } catch (FileNotFoundException e) {
            LogHelper.e(e);
        } catch (IOException e) {
            LogHelper.e(e);
        } finally {
        }
    }

    /**
     * 安装apk
     *
     * @author Michael.Zhang 2014-1-9 下午2:52:56
     */
    private void installApk() {
        File apkfile = new File(saveFilePath);
        if (!apkfile.exists()) {
            return;
        }

        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri data;
        // 判断版本大于等于7.0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            data = FileProvider.getUriForFile(context, "com.sc.clgg.fileprovider", apkfile);
            // 给目标应用一个临时授权
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            data = Uri.fromFile(apkfile);
        }
        intent.setDataAndType(data, "application/vnd.android.package-archive");
        context.startActivity(intent);
    }

    private void showDownloadDialog() {
        Builder builder = new Builder(context);
        builder.setCancelable(false);
        builder.setTitle("正在下载安装包...");

        View v = View.inflate(context, R.layout.view_update_progress, null);
        progressBar = v.findViewById(R.id.progress);
        builder.setView(v);
        mDownLoadDialog = builder.create();
        mDownLoadDialog.show();

        downloadApk();
    }

    private void showUpdatDialog() {
        AlertDialog.Builder builder = new Builder(context);
        builder.setCancelable(false);
        builder.setTitle("提示");
        builder.setMessage("检测到新的版本");
        builder.setPositiveButton("更新", new OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                showDownloadDialog();
            }
        });

        if (type == 1) {
            builder.setNegativeButton("取消", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

        builder.show();
    }
}
