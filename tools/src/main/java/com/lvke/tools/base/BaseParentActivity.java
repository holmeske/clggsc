package com.lvke.tools.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * @author：lvke
 * @date：2017/10/31 15:49
 */

public class BaseParentActivity extends AppCompatActivity {
    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Log.v("ac", "onAttachedToWindow()");
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.v("ac", "onCreate()");
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        Log.v("ac", "onPostCreate()");
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Log.v("ac", "onPostResume()");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.v("ac", "onRestart()");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.v("ac", "onStart()");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.v("ac", "onResume()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v("ac", "onPause()");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.v("ac", "onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.v("ac", "onDestroy()");
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        Log.v("ac", "onDetachedFromWindow()");
    }
}
