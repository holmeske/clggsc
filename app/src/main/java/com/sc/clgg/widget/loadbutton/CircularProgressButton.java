package com.sc.clgg.widget.loadbutton;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;
import android.util.StateSet;

import com.sc.clgg.R;

import tool.helper.LogHelper;

public class CircularProgressButton extends AppCompatButton {

    protected static final int IDLE_STATE_PROGRESS = 0;
    protected static final int ERROR_STATE_PROGRESS = -1;
    protected static final int SUCCESS_STATE_PROGRESS = 100;

    protected StateListDrawable mIdleStateDrawable;
    private GradientDrawable mGradientDrawable;
    private CircularAnimatedDrawable mAnimatedDrawable;

    private ColorStateList defaultColorStateList;
    private ColorStateList mCompleteColorState;
    private ColorStateList mErrorColorState;

    private StateManager mStateManager;
    private State mState;
    private int backGroundDrawable;
    private int mColorIndicator;
    private int mColorIndicatorBackground;
    private int mPaddingProgress;
    private float mCornerRadius;
    private boolean mIndeterminateProgressMode = true;
    private boolean mConfigurationChanged;
    private int mStrokeWidth;
    private int mMaxProgress;
    private int mProgress;
    private boolean mMorphingInProgress;
    private boolean mDoingProsess = false;
    private CharSequence mText;
    private int mStrokeColor;

    private AttributeSet mAttrs;

    private MorphingAnimation mMorphingAnimation;
    private Type mType = Type.RED;
    private int mBackGroundColor = R.color.white;
    private OnAnimationEndListener mCompleteStateListener = new OnAnimationEndListener() {
        @Override
        public void onAnimationEnd() {
            mMorphingInProgress = false;
            mDoingProsess = false;
            mState = State.COMPLETE;
            if (mText != null) {
                setText(mText);
            }
            setBackgroundResource(backGroundDrawable);
            mStateManager.checkState(CircularProgressButton.this);
        }
    };
    private OnAnimationEndListener mIdleStateListener = new OnAnimationEndListener() {
        @Override
        public void onAnimationEnd() {
            removeIcon();
            mMorphingInProgress = false;
            mState = State.IDLE;
            mStateManager.checkState(CircularProgressButton.this);
        }
    };
    private OnAnimationEndListener mErrorStateListener = new OnAnimationEndListener() {
        @Override
        public void onAnimationEnd() {
            mMorphingInProgress = false;
            mState = State.ERROR;
            setText(mText);
            setBackgroundResource(backGroundDrawable);
            mStateManager.checkState(CircularProgressButton.this);
        }
    };
    private Context mContext;

    public CircularProgressButton(Context context) {
        super(context);
        init(context, null);
    }

    public CircularProgressButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public CircularProgressButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attributeSet) {
        mContext = context;
        mAttrs = attributeSet;
        initAttributes(context, attributeSet);

        mStrokeWidth = (int) getResources().getDimension(R.dimen.length_4);

        mMaxProgress = 100;
        mState = State.IDLE;
        mStateManager = new StateManager(this);

        if (mType == Type.RED) {

            mGradientDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.cpb_background_red).mutate();

            backGroundDrawable = R.drawable.v6_mainred_btn;

            mStrokeColor = getEnabledColor(defaultColorStateList);
        } else {
            mGradientDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.cpb_background_white).mutate();
            backGroundDrawable = R.drawable.qd_button_grey_selector;

            mStrokeColor = 0xff9e9e9e;
        }

        setBackgroundResource(backGroundDrawable);

        initIdleStateDrawable(mGradientDrawable);
    }

    /**
     * 开始
     */
    public void startLoading() {
        mDoingProsess = true;
        setProgress(1);
    }

    /**
     * 结束
     */
    public void endLoading() {
        if (mMorphingAnimation != null) {
            mMorphingAnimation.stop();
        }
        if (mAnimatedDrawable != null) {
            mAnimatedDrawable.stop();
        }
        setProgress(SUCCESS_STATE_PROGRESS);
    }

    /**
     * 按钮后方的背景颜色
     */
    public void setBackGroundColor(int color) {
        mBackGroundColor = color;
    }

    /**
     * 设置红色还是白色按钮
     */
    public void setColor(Type type) {
        mType = type;
        init(getContext(), mAttrs);
    }

    private void initAttributes(Context context, AttributeSet attributeSet) {
        TypedArray array = context.getTheme().obtainStyledAttributes(attributeSet, R.styleable.CircularProgressButton, 0, 0);
        if (array != null) {
            try {
                mCornerRadius = array.getDimension(R.styleable.CircularProgressButton_cpb_cornerRadius, 0);

                mPaddingProgress = array.getDimensionPixelSize(R.styleable.CircularProgressButton_cpb_paddingProgress, 0);

                int state = mType == Type.RED ? R.color.cpb_red_state_selector : R.color.cpb_wite_state_selector;

                defaultColorStateList = ContextCompat.getColorStateList(context, array.getResourceId(R.styleable.CircularProgressButton_cpb_selectorIdle, state));

                mCompleteColorState = ContextCompat.getColorStateList(context, array.getResourceId(R.styleable.CircularProgressButton_cpb_selectorComplete, state));

                mErrorColorState = ContextCompat.getColorStateList(context, array.getResourceId(R.styleable.CircularProgressButton_cpb_selectorError, state));

                //加载进度圆环的颜色
                mColorIndicator = array.getColor(R.styleable.CircularProgressButton_cpb_colorIndicator, 0xff36abeb);

                mColorIndicatorBackground = array.getColor(R.styleable.CircularProgressButton_cpb_colorIndicatorBackground, 0xffdedede);
            } finally {
                array.recycle();
            }
        }
    }

    @SuppressLint("WrongCall")
    @Override
    public void draw(Canvas canvas) {
        if (mProgress > 0 && mState == State.PROGRESS && !mMorphingInProgress) {
            onDraw(canvas);
        } else {
            super.draw(canvas);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mProgress > 0 && mState == State.PROGRESS && !mMorphingInProgress) {
            drawIndeterminateProgress(canvas);
        } else {
            super.onDraw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(android.view.MotionEvent event) {
        if (mDoingProsess) {
            return false;
        } else {
            return super.onTouchEvent(event);
        }
    }

    private void drawIndeterminateProgress(Canvas canvas) {
        if (mAnimatedDrawable == null) {
            mAnimatedDrawable = new CircularAnimatedDrawable(mColorIndicator, mStrokeWidth);

            int offset = (getWidth() - getHeight()) / 2;
            mAnimatedDrawable.setBounds(offset + mPaddingProgress, mPaddingProgress, getWidth() - offset - mPaddingProgress, getHeight() - mPaddingProgress);

            mAnimatedDrawable.setCallback(this);
            mAnimatedDrawable.start();
        } else {
            mAnimatedDrawable.start();
            mAnimatedDrawable.draw(canvas);
        }
    }

    @Override
    protected boolean verifyDrawable(@NonNull Drawable who) {
        return who == mAnimatedDrawable || super.verifyDrawable(who);
    }

    private GradientDrawable createDrawable(int color) {
        int stateS = mType == Type.RED ? R.drawable.cpb_background_red : R.drawable.cpb_background_white;
        GradientDrawable drawable = (GradientDrawable) ContextCompat.getDrawable(mContext, stateS).mutate();
        drawable.setColor(color);
        drawable.setCornerRadius(mCornerRadius);
        drawable.setStroke(0, color);
        return drawable;
    }

    private int getEnabledColor(ColorStateList colorStateList) {
        return colorStateList.getColorForState(new int[]{android.R.attr.state_enabled}, 0);
    }

    private int getDisabledColor(ColorStateList colorStateList) {
        return colorStateList.getColorForState(new int[]{-android.R.attr.state_enabled}, 0);
    }

    private int getPressedColor(ColorStateList colorStateList) {
        return colorStateList.getColorForState(new int[]{android.R.attr.state_pressed}, 0);
    }

    private int getFocusedColor(ColorStateList colorStateList) {
        return colorStateList.getColorForState(new int[]{android.R.attr.state_focused}, 0);
    }

    protected void initIdleStateDrawable(GradientDrawable gd) {
        mIdleStateDrawable = new StateListDrawable();
        mIdleStateDrawable.addState(new int[]{android.R.attr.state_pressed}, createDrawable(getPressedColor(defaultColorStateList)));
        mIdleStateDrawable.addState(new int[]{android.R.attr.state_focused}, createDrawable(getFocusedColor(defaultColorStateList)));
        mIdleStateDrawable.addState(new int[]{-android.R.attr.state_enabled}, createDrawable(getDisabledColor(defaultColorStateList)));
        mIdleStateDrawable.addState(StateSet.WILD_CARD, gd);
    }

    private MorphingAnimation createMorphing() {
        mMorphingInProgress = true;

        MorphingAnimation animation = new MorphingAnimation(this, mGradientDrawable);
        animation.setFromCornerRadius(mCornerRadius);
        animation.setToCornerRadius(mCornerRadius);

        animation.setFromWidth(getWidth());
        animation.setToWidth(getWidth());

        if (mConfigurationChanged) {
            LogHelper.e("------ INSTANT");
            animation.setDuration(MorphingAnimation.DURATION_INSTANT);
        } else {
            LogHelper.e("------ NORMAL");
            animation.setDuration(MorphingAnimation.DURATION_NORMAL);
        }
        mConfigurationChanged = false;
        return animation;
    }

    private MorphingAnimation createMorphingAnimation(GradientDrawable gd, float fromCorner, float toCorner, int fromWidth, int toWidth) {
        mMorphingInProgress = true;

        MorphingAnimation animation = new MorphingAnimation(this, gd);

        animation.setFromCornerRadius(fromCorner);
        animation.setToCornerRadius(toCorner);

        animation.setPadding(mPaddingProgress);

        animation.setFromWidth(fromWidth);
        animation.setToWidth(toWidth);

        if (mConfigurationChanged) {
            LogHelper.e("------ INSTANT");
            animation.setDuration(MorphingAnimation.DURATION_INSTANT);
        } else {
            LogHelper.e("------ NORMAL");
            animation.setDuration(MorphingAnimation.DURATION_NORMAL);
        }

        mConfigurationChanged = false;
        return animation;
    }

    private MorphingAnimation createMorphingAnimation(float fromCorner, float toCorner, int fromWidth, int toWidth) {
        mMorphingInProgress = true;

        MorphingAnimation animation = new MorphingAnimation(this, mGradientDrawable);

        animation.setFromCornerRadius(fromCorner);
        animation.setToCornerRadius(toCorner);

        animation.setPadding(mPaddingProgress);

        animation.setFromWidth(fromWidth);
        animation.setToWidth(toWidth);

        if (mConfigurationChanged) {
            LogHelper.e("------ INSTANT");
            animation.setDuration(MorphingAnimation.DURATION_INSTANT);
        } else {
            LogHelper.e("------ NORMAL");
            animation.setDuration(MorphingAnimation.DURATION_NORMAL);
        }

        mConfigurationChanged = false;
        return animation;
    }

    private void morphToProgress() {
        LogHelper.v("morphToProgress()");

        setBackgroundDrawable(mIdleStateDrawable);

        mMorphingAnimation = createMorphingAnimation(mCornerRadius, getHeight(), getWidth(), getHeight());

        mMorphingAnimation.setFromColor(getEnabledColor(defaultColorStateList));
        mMorphingAnimation.setToColor(getEnabledColor(defaultColorStateList));

        mMorphingAnimation.setFromStrokeColor(mStrokeColor);
        mMorphingAnimation.setToStrokeColor(mColorIndicatorBackground);

        mMorphingAnimation.setListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                mMorphingInProgress = false;
                mState = State.PROGRESS;
                mText = getText();
                setText(null);
                mStateManager.checkState(CircularProgressButton.this);
            }
        });

        mMorphingAnimation.start();
    }

    private void morphProgressToComplete() {
        LogHelper.v("morphProgressToComplete()");

        MorphingAnimation animation = createMorphingAnimation(mGradientDrawable, getHeight(), mCornerRadius, getHeight(), getWidth());
        animation.setFromColor(getEnabledColor(mCompleteColorState));
        animation.setToColor(getEnabledColor(mCompleteColorState));

        animation.setFromStrokeColor(mColorIndicator);
        animation.setToStrokeColor(mStrokeColor);

        animation.setListener(mCompleteStateListener);

        animation.start();
    }

    private void morphIdleToComplete() {
        LogHelper.v("morphIdleToComplete()");
        MorphingAnimation animation = createMorphing();

        animation.setFromColor(getEnabledColor(defaultColorStateList));
        animation.setToColor(getEnabledColor(mCompleteColorState));

        animation.setFromStrokeColor(getEnabledColor(defaultColorStateList));
        animation.setToStrokeColor(getEnabledColor(mCompleteColorState));

        animation.setListener(mCompleteStateListener);
        animation.start();
    }

    private void morphCompleteToIdle() {
        LogHelper.v("morphCompleteToIdle()");
        MorphingAnimation animation = createMorphing();

        animation.setFromColor(getEnabledColor(mCompleteColorState));
        animation.setToColor(getEnabledColor(defaultColorStateList));

        animation.setFromStrokeColor(getEnabledColor(mCompleteColorState));
        animation.setToStrokeColor(getEnabledColor(defaultColorStateList));

        animation.setListener(mIdleStateListener);

        animation.start();
    }

    private void morphErrorToIdle() {
        LogHelper.v("morphErrorToIdle()");
        MorphingAnimation animation = createMorphing();

        animation.setFromColor(getEnabledColor(mErrorColorState));
        animation.setToColor(getEnabledColor(defaultColorStateList));

        animation.setFromStrokeColor(getEnabledColor(mErrorColorState));
        animation.setToStrokeColor(getEnabledColor(defaultColorStateList));

        animation.setListener(mIdleStateListener);

        animation.start();
    }

    private void morphIdleToError() {
        LogHelper.v("morphIdleToError()");
        MorphingAnimation animation = createMorphing();

        animation.setFromColor(getEnabledColor(defaultColorStateList));
        animation.setToColor(getEnabledColor(mErrorColorState));

        animation.setFromStrokeColor(getEnabledColor(defaultColorStateList));
        animation.setToStrokeColor(getEnabledColor(mErrorColorState));

        animation.setListener(mErrorStateListener);

        animation.start();
    }

    private void morphProgressToError() {
        LogHelper.v("morphProgressToError()");
        MorphingAnimation animation = createMorphingAnimation(getHeight(), mCornerRadius, getHeight(), getWidth());

        animation.setFromColor(mBackGroundColor);
        animation.setToColor(getEnabledColor(mErrorColorState));

        animation.setFromStrokeColor(mColorIndicator);
        animation.setToStrokeColor(mStrokeColor);
        animation.setListener(mErrorStateListener);

        animation.start();
    }

    private void morphProgressToIdle() {
        LogHelper.v("morphProgressToIdle()");
        MorphingAnimation animation = createMorphingAnimation(getHeight(), mCornerRadius, getHeight(), getWidth());

        animation.setFromColor(mBackGroundColor);
        animation.setToColor(getEnabledColor(defaultColorStateList));

        animation.setFromStrokeColor(mColorIndicator);
        animation.setToStrokeColor(mStrokeColor);
        animation.setListener(new OnAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                removeIcon();
                mMorphingInProgress = false;
                mState = State.IDLE;
                //initIdleStateDrawable();
                //setBackgroundDrawable(mIdleStateDrawable);
                mStateManager.checkState(CircularProgressButton.this);
            }
        });

        animation.start();
    }

    protected void removeIcon() {
        setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
        setPadding(0, 0, 0, 0);
    }

    protected int getProgress() {
        return mProgress;
    }

    protected void setProgress(int progress) {
        mProgress = progress;

        LogHelper.v("setProgress  mProgress = " + mProgress);

        if (mMorphingInProgress || getWidth() == 0) {
            return;
        }
        mStateManager.saveProgress(this);

        if (mProgress >= mMaxProgress) {
            if (mState == State.PROGRESS) {
                morphProgressToComplete();
            } else if (mState == State.IDLE) {
                morphIdleToComplete();
            }
        } else if (mProgress > IDLE_STATE_PROGRESS) {
            if (mState == State.IDLE || mState == State.ERROR || mState == State.COMPLETE) {
                morphToProgress();
            } else if (mState == State.PROGRESS) {
                invalidate();
            }
        } else if (mProgress == ERROR_STATE_PROGRESS) {
            if (mState == State.PROGRESS) {
                morphProgressToError();
            } else if (mState == State.IDLE) {
                morphIdleToError();
            }
        } else if (mProgress == IDLE_STATE_PROGRESS) {
            if (mState == State.COMPLETE) {
                morphCompleteToIdle();
            } else if (mState == State.PROGRESS) {
                morphProgressToIdle();
            } else if (mState == State.ERROR) {
                morphErrorToIdle();
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        LogHelper.v("onLayout()  " + changed + "  " + left + "  " + top + "  " + right + "  " + bottom);
//        if (changed) {
//            setProgress(mProgress);
//        }
        if (changed) {
            if (mAnimatedDrawable != null) {
                mAnimatedDrawable.stop();
                int offset = (getWidth() - getHeight()) / 2;
                mAnimatedDrawable.setBounds(offset + mPaddingProgress, mPaddingProgress, getWidth() - offset - mPaddingProgress, getHeight() - mPaddingProgress);
                mAnimatedDrawable.start();
            }
        }
    }

    @Override
    public Parcelable onSaveInstanceState() {
        LogHelper.v("onSaveInstanceState()");
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.mProgress = mProgress;
        savedState.mIndeterminateProgressMode = mIndeterminateProgressMode;
        savedState.mConfigurationChanged = true;
        return savedState;
    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        LogHelper.v("onRestoreInstanceState()");
        if (state instanceof SavedState) {
            SavedState savedState = (SavedState) state;
            mProgress = savedState.mProgress;
            mIndeterminateProgressMode = savedState.mIndeterminateProgressMode;
            mConfigurationChanged = savedState.mConfigurationChanged;
            super.onRestoreInstanceState(savedState.getSuperState());
            setProgress(mProgress);
        } else {
            super.onRestoreInstanceState(state);
        }
    }

    private enum State {
        /**
         * 进度中 闲置 完成 错误
         */
        PROGRESS, IDLE, COMPLETE, ERROR
    }

    private enum Type {
        RED, WHITE
    }

    static class SavedState extends BaseSavedState {

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {

            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        private boolean mIndeterminateProgressMode;
        private boolean mConfigurationChanged;
        private int mProgress;

        private SavedState(Parcelable parcel) {
            super(parcel);
        }

        private SavedState(Parcel in) {
            super(in);
            mProgress = in.readInt();
            mIndeterminateProgressMode = in.readInt() == 1;
            mConfigurationChanged = in.readInt() == 1;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
            out.writeInt(mProgress);
            out.writeInt(mIndeterminateProgressMode ? 1 : 0);
            out.writeInt(mConfigurationChanged ? 1 : 0);
        }
    }
}
