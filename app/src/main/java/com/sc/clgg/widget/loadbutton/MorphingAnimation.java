package com.sc.clgg.widget.loadbutton;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;

import tool.helper.LogHelper;

public class MorphingAnimation {

    public static final int DURATION_NORMAL = 400;
    public static final int DURATION_INSTANT = 1;

    private AnimatorSet animatorSet;
    private OnAnimationEndListener mListener;
    private int mDuration;
    private int mFromWidth;
    private int mToWidth;
    private int mFromColor;
    private int mToColor;
    private int mFromStrokeColor;
    private int mToStrokeColor;
    private float mFromCornerRadius;
    private float mToCornerRadius;
    private float mPadding;
    private CircularProgressButton mView;
    private GradientDrawable mGradientDrawable;

    public MorphingAnimation(CircularProgressButton v, GradientDrawable drawable) {
        mView = v;
        mGradientDrawable = drawable;
    }

    public void setDuration(int duration) {
        mDuration = duration;
    }

    public void setListener(OnAnimationEndListener listener) {
        mListener = listener;
    }

    public void setFromWidth(int fromWidth) {
        mFromWidth = fromWidth;
    }

    public void setToWidth(int toWidth) {
        mToWidth = toWidth;
    }

    public void setFromColor(int fromColor) {
        mFromColor = fromColor;
    }

    public void setToColor(int toColor) {
        mToColor = toColor;
    }

    public void setFromStrokeColor(int fromStrokeColor) {
        mFromStrokeColor = fromStrokeColor;
    }

    public void setToStrokeColor(int toStrokeColor) {
        mToStrokeColor = toStrokeColor;
    }

    public void setFromCornerRadius(float fromCornerRadius) {
        mFromCornerRadius = fromCornerRadius;
    }

    public void setToCornerRadius(float toCornerRadius) {
        mToCornerRadius = toCornerRadius;
    }

    public void setPadding(float padding) {
        mPadding = padding;
    }

    public void stop() {
        if (animatorSet != null) {
            animatorSet.cancel();
        }
    }

    public void start() {
        LogHelper.e("mFromWidth = " + mFromWidth + "   mToWidth  = " + mToWidth);
        ValueAnimator widthAnimation = ValueAnimator.ofInt(mFromWidth, mToWidth);
        widthAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                //LogHelper.e("getAnimatedValue() = " + value + "     getAnimatedFraction() = " + new DecimalFormat("0.000").format(animation.getAnimatedFraction()));

                int left;
                int right;
                int padding;

                if (mFromWidth > mToWidth) {
                    left = (mFromWidth - value) / 2;
                    right = mFromWidth - left;

                    padding = (int) (mPadding * animation.getAnimatedFraction());
                } else {
                    left = (mToWidth - value) / 2;
                    right = mToWidth - left;

                    padding = (int) (mPadding - mPadding * animation.getAnimatedFraction());
                }

                mGradientDrawable.setBounds(left + padding, padding, right - padding, mView.getHeight() - padding);

//                LogHelper.e((left + padding) + "   "
//                        + padding + "   "
//                                + (right - padding)
//                        + "   "+ (mView.getHeight() - padding)
//                );

            }
        });

        ObjectAnimator bgColorAnimation = ObjectAnimator.ofInt(mGradientDrawable, "color", mFromColor, mToColor);
        bgColorAnimation.setEvaluator(new ArgbEvaluator());

        ObjectAnimator strokeColorAnimation = ObjectAnimator.ofInt(mGradientDrawable, "strokeColor", mFromStrokeColor, mToStrokeColor);
        strokeColorAnimation.setEvaluator(new ArgbEvaluator());

        ObjectAnimator cornerAnimation = ObjectAnimator.ofFloat(mGradientDrawable, "cornerRadius", mFromCornerRadius, mToCornerRadius);

        animatorSet = new AnimatorSet();
        animatorSet.setDuration(mDuration);
        animatorSet.playTogether(widthAnimation, bgColorAnimation, strokeColorAnimation, cornerAnimation);
        animatorSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                if (mListener != null) {
                    mListener.onAnimationEnd();
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animatorSet.start();
    }

    public class HsvEvaluator implements TypeEvaluator<Integer> {
        float[] startHsv = new float[3];
        float[] endHsv = new float[3];
        float[] outHsv = new float[3];

        @Override
        public Integer evaluate(float fraction, Integer startValue, Integer endValue) {
            // 把 ARGB 转换成 HSV
            Color.colorToHSV(startValue, startHsv);
            Color.colorToHSV(endValue, endHsv);

            // 计算当前动画完成度（fraction）所对应的颜色值
            if (endHsv[0] - startHsv[0] > 180) {
                endHsv[0] -= 360;
            } else if (endHsv[0] - startHsv[0] < -180) {
                endHsv[0] += 360;
            }
            outHsv[0] = startHsv[0] + (endHsv[0] - startHsv[0]) * fraction;
            if (outHsv[0] > 360) {
                outHsv[0] -= 360;
            } else if (outHsv[0] < 0) {
                outHsv[0] += 360;
            }
            outHsv[1] = startHsv[1] + (endHsv[1] - startHsv[1]) * fraction;
            outHsv[2] = startHsv[2] + (endHsv[2] - startHsv[2]) * fraction;

            // 计算当前动画完成度（fraction）所对应的透明度
            int alpha = startValue >> 24 + (int) ((endValue >> 24 - startValue >> 24) * fraction);

            // 把 HSV 转换回 ARGB 返回
            return Color.HSVToColor(alpha, outHsv);
        }
    }

}