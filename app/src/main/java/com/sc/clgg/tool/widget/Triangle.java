package com.sc.clgg.tool.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.sc.clgg.R;

/**
 * Author：lvke
 * CreateDate：2017/9/22 13:52
 * 三角形
 */

public class Triangle extends View {
    private Paint mPaint;
    private Path mPath;

    {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPath = new Path();
    }

    public Triangle(Context context) {
        super(context);
    }

    public Triangle(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Triangle);
        float rotation = array.getFloat(R.styleable.Triangle_rotation, 0);
        int color = array.getColor(R.styleable.Triangle_color, Color.WHITE);
        array.recycle();

        if (rotation != 0) {
            this.setRotation(rotation);
        }
        mPaint.setColor(color);
    }
    public void setRotationAngle(float rotation){
        this.setRotation(rotation);
        invalidate();
    }
    public void setColor(@ColorInt int color){
        mPaint.setColor(color);
        invalidate();
    }

    public Triangle(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        LogUtils.e("  " + getWidth() + "  " + getHeight() + "  " + getPaddingLeft() + "  " + getPaddingRight());

        canvas.save();
        mPath.moveTo(getWidth() / 2, getPaddingTop());
        mPath.lineTo(getPaddingLeft(), getHeight() - getPaddingBottom());
        mPath.lineTo(getWidth() - getPaddingRight(), getHeight() - getPaddingBottom());
        canvas.drawPath(mPath, mPaint);
        canvas.restore();

    }
}
