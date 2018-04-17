package tool.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.support.annotation.ColorInt;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.sc.clgg.R;

/**
 * CreateDate：2017/9/28 12:18
 *
 * @author lvke
 */

public class ShapeLinearLayout extends LinearLayout {
    private @ColorInt int solid;
    private GradientDrawable defaultGradientDrawable;

    public ShapeLinearLayout(Context context) {
        super(context);
    }

    public ShapeLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ShapeLinearLayout);

        solid = array.getColor(R.styleable.ShapeLinearLayout_solid, 0);
        @ColorInt int stroke_color = array.getColor(R.styleable.ShapeLinearLayout_stroke_color, 0);
        @ColorInt int pressed_color = array.getColor(R.styleable.ShapeLinearLayout_pressed_color, 0);

        float stroke_width = array.getDimension(R.styleable.ShapeLinearLayout_stroke_width, 0);
        float corners = array.getDimension(R.styleable.ShapeLinearLayout_corners, 0);
        float topLeftRadius = array.getDimension(R.styleable.ShapeLinearLayout_topLeftRadius, 0);
        float topRightRadius = array.getDimension(R.styleable.ShapeLinearLayout_topRightRadius, 0);
        float bottomLeftRadius = array.getDimension(R.styleable.ShapeLinearLayout_bottomLeftRadius, 0);
        float bottomRightRadius = array.getDimension(R.styleable.ShapeLinearLayout_bottomRightRadius, 0);
        int shape = array.getInteger(R.styleable.ShapeLinearLayout_shape, 0);

        array.recycle();

        defaultGradientDrawable = new GradientDrawable();
        defaultGradientDrawable.setCornerRadius(corners);
        defaultGradientDrawable.setColor(solid);
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

    public ShapeLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setSolid(@ColorInt int solid) {
        this.solid = solid;
        defaultGradientDrawable.setColor(solid);
        setBackgroundDrawable(defaultGradientDrawable);
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
        }
    }
}
