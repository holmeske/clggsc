package com.sc.clgg.base;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.sc.clgg.R;

import androidx.annotation.Nullable;

import static com.sc.clgg.util.PotatoKt.statusBarHeight;

/**
 * @author lvke
 */
@SuppressLint("Registered")
public class BaseImmersionActivity extends BaseAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ImmersionBar.with(this).init();
    }

    public void initTitle(String title) {
        if (title != null && findViewById(R.id.titlebar_title) != null) {
            ((TextView) findViewById(R.id.titlebar_title)).setText(title);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (findViewById(R.id.titlebar_left) != null) {
            findViewById(R.id.titlebar_left).setOnClickListener(v -> finish());
        }
        if (findViewById(R.id.titlebar_top) != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                findViewById(R.id.titlebar_top).setVisibility(View.GONE);
            } else {
                findViewById(R.id.titlebar_top).setLayoutParams(
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, statusBarHeight(this)));
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        ImmersionBar.with(this).init();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImmersionBar.with(this).destroy();
    }
}
