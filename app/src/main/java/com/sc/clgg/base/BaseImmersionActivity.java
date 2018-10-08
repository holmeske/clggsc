package com.sc.clgg.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gyf.barlibrary.ImmersionBar;
import com.sc.clgg.R;

import butterknife.Unbinder;

import static com.sc.clgg.util.PotatoKt.statusBarHeight;

/**
 * @author：lvke
 * @date：2018/5/14 14:10
 */
public class BaseImmersionActivity extends BaseAppCompatActivity {
    public Unbinder unbinder;

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
            findViewById(R.id.titlebar_left).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }
        if (findViewById(R.id.titlebar_top) != null) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                findViewById(R.id.titlebar_top).setVisibility(View.GONE);
            } else {
                findViewById(R.id.titlebar_top)
                        .setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, statusBarHeight(this)));
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null && unbinder != Unbinder.EMPTY) {
            unbinder.unbind();
        }
        ImmersionBar.with(this).destroy();
    }
}
