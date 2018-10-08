package com.sc.clgg.tool.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import com.sc.clgg.application.App;

public class SharedPreferencesHelper {

    private static SharedPreferencesHelper mSharedPreferencesHelper;

    private String NAME = "sp";
    private int MODE = Context.MODE_PRIVATE;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private synchronized static SharedPreferencesHelper getIntence() {
        if (mSharedPreferencesHelper == null) {
            mSharedPreferencesHelper = new SharedPreferencesHelper();
        }
        return mSharedPreferencesHelper;
    }

    public static SharedPreferences.Editor editor(Context context) {
        return getIntence().getSharedPreferencesEditor(context);
    }

    public static SharedPreferences.Editor editor(String name) {
        if (TextUtils.isEmpty(name)) {
            throw new NullPointerException(" params is incorrect ------ name ");
        }
        return getIntence().getSharedPreferencesEditor(App.getInstance(), name);
    }

    public static SharedPreferences.Editor editor(Context context, String name) {
        if (TextUtils.isEmpty(name)) {
            throw new NullPointerException(" params is incorrect ------ name ");
        }
        return getIntence().getSharedPreferencesEditor(context, name);
    }

    public static SharedPreferences SharedPreferences(Context context) {
        return getIntence().getSharedPreferences(context);
    }

    public static SharedPreferences SharedPreferences() {
        return getIntence().getSharedPreferences(App.getInstance());
    }

    public static SharedPreferences SharedPreferences(Context context, String name) {
        if (TextUtils.isEmpty(name)) {
            throw new NullPointerException(" params is incorrect ------ name ");
        }
        return getIntence().getSharedPreferences(context, name);
    }

    private SharedPreferences.Editor getSharedPreferencesEditor(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(NAME, MODE);
        }
        if (mEditor == null) {
            mEditor = mSharedPreferences.edit();
        }
        return mEditor;
    }

    private SharedPreferences.Editor getSharedPreferencesEditor(Context context, String name) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(NAME, MODE);
        }
        if (mEditor == null) {
            mEditor = mSharedPreferences.edit();
        }
        return mEditor;
    }

    private SharedPreferences getSharedPreferences(Context context) {
        if (mSharedPreferences == null) {
            mSharedPreferences = context.getSharedPreferences(NAME, MODE);
        }
        return mSharedPreferences;
    }

    private SharedPreferences getSharedPreferences(Context context, String name) {
        return context.getSharedPreferences(name, MODE);
    }

}
