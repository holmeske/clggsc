package com.sc.clgg.widget;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

public class HomeButton extends LinearLayout {

    private Handler mHandler;

    private HomeButtonOnClickListener mHomeButtonOnClickListener;

    public HomeButton(Context context) {
        super(context);
        this.setOnClickListener(v -> {
            if (mHomeButtonOnClickListener != null) {
                mHandler.postDelayed(() -> mHomeButtonOnClickListener.onClick(v), 250);
            }
        });
    }

    public HomeButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.setOnClickListener(v -> {
            if (mHomeButtonOnClickListener != null) {
                mHandler.postDelayed(() -> mHomeButtonOnClickListener.onClick(v), 200);
            }
        });
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            startAnimationDown(HomeButton.this);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            startAnimationUp(HomeButton.this);
            /*if (mHomeButtonOnClickListener != null) {
                mHandler.postDelayed(new Runnable() {
					@Override
					public void run() {
						mHomeButtonOnClickListener.onClick();
					}
				}, 250);
			}*/
        }
        return super.onTouchEvent(event);
    }

    private void startAnimationDown(final LinearLayout r) {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        final Animation scaleAnimation = new ScaleAnimation(1.0f, 0.9f, 1.0f,
                0.9f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(150);
        scaleAnimation.setFillAfter(true);
        mHandler.post(new Runnable() {

            @Override
            public void run() {
                r.startAnimation(scaleAnimation);
            }
        });
    }

    private void startAnimationUp(final LinearLayout r) {
        if (mHandler == null) {
            mHandler = new Handler();
        }
        final Animation scaleAnimation = new ScaleAnimation(0.9f, 1.0f, 0.9f,
                1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(150);
        scaleAnimation.setFillAfter(true);
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {
                r.startAnimation(scaleAnimation);

            }
        }, 50);
    }

    public void setHomeButtonOnClickListener(HomeButtonOnClickListener mHomeButtonOnClickListener) {
        this.mHomeButtonOnClickListener = mHomeButtonOnClickListener;
    }


    public interface HomeButtonOnClickListener {
        void onClick(View v);
    }
}
