package com.sc.clgg.tool.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.sc.clgg.R;

/**
 * CreateDate：2017/9/28 12:18
 *
 * @author lvke
 */

public class ShapeTextView extends AppCompatTextView {
    /**
     * 底部分割线画笔
     */
    private Paint mLinePaint;
    /**
     * 背景填充色
     */
    private @ColorInt int solid;
    /**
     * 默认的背景
     */
    private GradientDrawable defaultGradientDrawable;
    /**
     * 底部分割线颜色
     */
    private @ColorInt int dividerColor;

    {
        mLinePaint = new Paint();
        setLayerType(LAYER_TYPE_HARDWARE, getPaint());
        getPaint().setAntiAlias(true);
    }

    public ShapeTextView(Context context) {
        super(context);
    }

    public ShapeTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ShapeTextView);

        solid = array.getColor(R.styleable.ShapeTextView_solid, 0);
        dividerColor = array.getColor(R.styleable.ShapeTextView_divider_color, 0);
        @ColorInt int stroke_color = array.getColor(R.styleable.ShapeTextView_stroke_color, 0);
        @ColorInt int pressed_color = array.getColor(R.styleable.ShapeTextView_pressed_color, 0);

        float stroke_width = array.getDimension(R.styleable.ShapeTextView_stroke_width, 0);

        float corners = array.getDimension(R.styleable.ShapeTextView_corners, 0);
        float topLeftRadius = array.getDimension(R.styleable.ShapeTextView_topLeftRadius, 0);
        float topRightRadius = array.getDimension(R.styleable.ShapeTextView_topRightRadius, 0);
        float bottomLeftRadius = array.getDimension(R.styleable.ShapeTextView_bottomLeftRadius, 0);
        float bottomRightRadius = array.getDimension(R.styleable.ShapeTextView_bottomRightRadius, 0);

        int shape = array.getInteger(R.styleable.ShapeTextView_shape, 0);
        array.recycle();

        defaultGradientDrawable = new GradientDrawable();
        defaultGradientDrawable.setCornerRadius(corners);
        if (getBackground() instanceof ColorDrawable) {
            ColorDrawable colordDrawable = (ColorDrawable) getBackground();
            defaultGradientDrawable.setColor(colordDrawable.getColor());
        } else {
            defaultGradientDrawable.setColor(solid);
        }
        defaultGradientDrawable.setStroke((int) stroke_width, stroke_color);
        setShape(defaultGradientDrawable, shape);

        if (pressed_color != 0) {
            GradientDrawable pressedGradientDrawable = new GradientDrawable();
            pressedGradientDrawable.setCornerRadius(corners);
            pressedGradientDrawable.setStroke((int) stroke_width, stroke_color);
            pressedGradientDrawable.setColor(pressed_color);
            setShape(pressedGradientDrawable, shape);

            StateListDrawable mStateListDrawable = new StateListDrawable();
            mStateListDrawable.addState(new int[]{android.R.attr.state_pressed}, pressedGradientDrawable);
            //注意里面的“-”号，当XML的设定是false时，就需要使用资源符号的负值来设定。
            mStateListDrawable.addState(new int[]{-android.R.attr.state_pressed}, defaultGradientDrawable);
            setBackgroundDrawable(mStateListDrawable);
        } else {
            setBackgroundDrawable(defaultGradientDrawable);
        }

    }

    public ShapeTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public int getSolidColor() {
        return super.getSolidColor();
    }

    public void setCornerRadius(float radius) {
        this.solid = solid;
        defaultGradientDrawable.setCornerRadius(radius);
        setBackgroundDrawable(defaultGradientDrawable);
    }

    /**
     * @param solid 背景填充色
     */
    public void setSolid(@ColorInt int solid) {
        this.solid = solid;
        defaultGradientDrawable.setColor(solid);
        setBackgroundDrawable(defaultGradientDrawable);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (dividerColor != 0) {
            mLinePaint.setColor(dividerColor);
            canvas.drawLine(0, ((float) getMeasuredHeight() - 0.4f), getMeasuredWidth(), ((float) getMeasuredHeight() - 0.4f), mLinePaint);
        }
    }

    private void setShape(GradientDrawable gradientDrawable, int shape) {
        switch (shape) {
            case 0:
                gradientDrawable.setShape(GradientDrawable.RECTANGLE);
                break;
            case 1:
                gradientDrawable.setShape(GradientDrawable.OVAL);
                break;
            case 2:
                gradientDrawable.setShape(GradientDrawable.LINE);
                break;
            case 3:
                gradientDrawable.setShape(GradientDrawable.RING);
                break;
            default:
                break;
        }
    }

}
