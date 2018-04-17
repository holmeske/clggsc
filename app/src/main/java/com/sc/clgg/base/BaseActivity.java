package com.sc.clgg.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Author：lvke
 * CreateDate：2017/8/11 10:08
 */

public abstract class BaseActivity extends BaseAppCompatActivity {
    private Unbinder unbinder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        initTitle();
        if (layoutRes() != 0) {
            setContentView(layoutRes());
        } else {
            throw new NullPointerException("布局文件不能为空");
        }
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        initBefore(savedInstanceState);
        init();
    }

    public void initTitle() {
    }

    public void initBefore(Bundle savedInstanceState) {
    }

    protected abstract int layoutRes();

    protected abstract void init();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (unbinder != null && unbinder != Unbinder.EMPTY) {
            unbinder.unbind();
        }
    }
}
