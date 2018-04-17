package com.sc.clgg.widget.loadbutton;

import android.annotation.TargetApi;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.util.Property;

@TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
class CircularAnimatedDrawable extends Drawable implements Animatable {

    public static final int MIN_SWEEP_ANGLE = 30;
    private static final Interpolator ANGLE_INTERPOLATOR = new LinearInterpolator();
    private static final Interpolator SWEEP_INTERPOLATOR = new DecelerateInterpolator();
    private static final int ANGLE_ANIMATOR_DURATION = 2000;
    private static final int SWEEP_ANIMATOR_DURATION = 600;
    private final RectF fBounds = new RectF();

    private ObjectAnimator mObjectAnimatorSweep;
    private ObjectAnimator mObjectAnimatorAngle;
    private boolean mModeAppearing;
    private Paint mPaint;
    private float mCurrentGlobalAngleOffset;
    private float mCurrentGlobalAngle;
    private float mCurrentSweepAngle;
    private float mBorderWidth = 4f;
    private boolean mRunning;
    private Property<CircularAnimatedDrawable, Float> mAngleProperty = new Property<CircularAnimatedDrawable, Float>(Float.class, "angle") {
        @Override
        public Float get(CircularAnimatedDrawable object) {
            return object.getCurrentGlobalAngle();
        }

        @Override
        public void set(CircularAnimatedDrawable object, Float value) {
            object.setCurrentGlobalAngle(value);
        }
    };
    private Property<CircularAnimatedDrawable, Float> mSweepProperty = new Property<CircularAnimatedDrawable, Float>(Float.class, "arc") {
        @Override
        public Float get(CircularAnimatedDrawable object) {
//          LogHelper.e("getCurrentSweepAngle() = " + getCurrentSweepAngle());
            return object.getCurrentSweepAngle();
        }

        @Override
        public void set(CircularAnimatedDrawable object, Float value) {
//            LogHelper.e("setCurrentSweepAngle = " + value);
            object.setCurrentSweepAngle(value);
        }
    };


    public CircularAnimatedDrawable(int color, float borderWidth) {
        mBorderWidth = borderWidth;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setColor(color);

        setupAnimations();
    }

    @Override
    public void draw(Canvas canvas) {
        float startAngle = mCurrentGlobalAngle - mCurrentGlobalAngleOffset;
        float sweepAngle = mCurrentSweepAngle;

        if (!mModeAppearing) {
            startAngle = startAngle + sweepAngle;
            sweepAngle = 360 - sweepAngle - MIN_SWEEP_ANGLE;
        } else {
            sweepAngle += MIN_SWEEP_ANGLE;
        }
//        LogHelper.e("" + startAngle + "   " + sweepAngle);
        canvas.drawArc(fBounds, startAngle, sweepAngle, false, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mPaint.setColorFilter(cf);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    private void toggleAppearingMode() {
        mModeAppearing = !mModeAppearing;
        if (mModeAppearing) {
            mCurrentGlobalAngleOffset = (mCurrentGlobalAngleOffset + MIN_SWEEP_ANGLE * 2) % 360;
            //LogHelper.e("mCurrentGlobalAngleOffset = " + mCurrentGlobalAngleOffset);
        }
    }

    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        fBounds.left = bounds.left + mBorderWidth / 2f + .5f;
        fBounds.right = bounds.right - mBorderWidth / 2f - .5f;
        fBounds.top = bounds.top + mBorderWidth / 2f + .5f;
        fBounds.bottom = bounds.bottom - mBorderWidth / 2f - .5f;
    }

    private void setupAnimations() {
        //旋转的动画
        mObjectAnimatorAngle = ObjectAnimator.ofFloat(this, mAngleProperty, 360f);
        mObjectAnimatorAngle.setInterpolator(ANGLE_INTERPOLATOR);//动画插入器
        mObjectAnimatorAngle.setDuration(ANGLE_ANIMATOR_DURATION);//设置动画时间
        mObjectAnimatorAngle.setRepeatMode(ValueAnimator.RESTART);//设置动画循环模式。
        mObjectAnimatorAngle.setRepeatCount(ValueAnimator.INFINITE);//设置动画重复次数，这里-1代表无限

        mObjectAnimatorSweep = ObjectAnimator.ofFloat(this, mSweepProperty, 360f - MIN_SWEEP_ANGLE * 2);
        mObjectAnimatorSweep.setInterpolator(SWEEP_INTERPOLATOR);
        mObjectAnimatorSweep.setDuration(SWEEP_ANIMATOR_DURATION);
        mObjectAnimatorSweep.setRepeatMode(ValueAnimator.RESTART);
        mObjectAnimatorSweep.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimatorSweep.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                toggleAppearingMode();
            }
        });
    }

    @Override
    public void start() {
        if (isRunning()) {
            return;
        }
        mRunning = true;
        mObjectAnimatorAngle.start();
        mObjectAnimatorSweep.start();
        invalidateSelf();
    }

    @Override
    public void stop() {
        if (!isRunning()) {
            return;
        }
        mRunning = false;
        mObjectAnimatorAngle.cancel();
        mObjectAnimatorSweep.cancel();
        invalidateSelf();
    }

    @Override
    public boolean isRunning() {
        return mRunning;
    }

    public float getCurrentGlobalAngle() {
        return mCurrentGlobalAngle;
    }

    public void setCurrentGlobalAngle(float currentGlobalAngle) {
        mCurrentGlobalAngle = currentGlobalAngle;
        invalidateSelf();
    }

    public float getCurrentSweepAngle() {
        return mCurrentSweepAngle;
    }

    public void setCurrentSweepAngle(float currentSweepAngle) {
        mCurrentSweepAngle = currentSweepAngle;
        invalidateSelf();
    }

}
