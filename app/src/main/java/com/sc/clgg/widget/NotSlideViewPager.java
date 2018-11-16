package com.sc.clgg.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.sc.clgg.R;

import androidx.viewpager.widget.ViewPager;

public class NotSlideViewPager extends ViewPager {
    public boolean isScroll = false;

    public NotSlideViewPager(Context context) {
        super(context);
    }

    public NotSlideViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NotSlideViewPager);
         /*获取布局中设置的属性*/
        isScroll = array.getBoolean(R.styleable.NotSlideViewPager_canSlide, false);
        array.recycle();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (isScroll) {
            return super.onTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (isScroll) {
            return super.onInterceptTouchEvent(ev);
        }
        return false;
    }
}