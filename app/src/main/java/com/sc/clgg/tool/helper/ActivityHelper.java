package com.sc.clgg.tool.helper;

import android.app.Activity;
import android.content.Intent;

import com.sc.clgg.R;

import java.lang.ref.WeakReference;

/**
 * Author：lvke
 */

public class ActivityHelper {

    public static void startActivityScale(Activity activity, Intent intent) {
        WeakReference<Activity> weakReference = new WeakReference<>(activity);
        weakReference.get().startActivity(intent);
        weakReference.get().overridePendingTransition(R.anim.scale_in, R.anim.alpha_out);
    }

    public static void startAcScale(Activity activity, Class cls) {
        WeakReference<Activity> weakReference = new WeakReference<>(activity);
        weakReference.get().startActivity(new Intent(weakReference.get(), cls));
        weakReference.get().overridePendingTransition(R.anim.scale_in, R.anim.alpha_out);
    }

    public static void startAcMove(Activity activity, Intent intent) {
        WeakReference<Activity> weakReference = new WeakReference<>(activity);
        weakReference.get().startActivity(intent);
        weakReference.get().overridePendingTransition(R.anim.push_right_in, R.anim.scale_out);
    }

    public static void startAcMove(Activity activity, Class cls) {
        WeakReference<Activity> weakReference = new WeakReference<>(activity);
        weakReference.get().startActivity(new Intent(weakReference.get(), cls));
        weakReference.get().overridePendingTransition(R.anim.push_right_in, R.anim.scale_out);
    }

    public static void finishAcMove(Activity activity) {
        WeakReference<Activity> weakReference = new WeakReference<>(activity);
        weakReference.get().finish();
        weakReference.get().overridePendingTransition(R.anim.scale_in, R.anim.push_right_out);
    }

}
