package com.lvke.tools.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.lvke.tools.R;

public class DashLine extends View {
    Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    {
        PathEffect pathEffect = new DashPathEffect(new float[]{10, 5}, 0);
        paint.setPathEffect(pathEffect);
        setLayerType(LAYER_TYPE_SOFTWARE, paint);
    }

    public DashLine(Context context) {
        super(context);
    }

    public DashLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.DashLine);

        try {
            int color = array.getColor(R.styleable.DashLine_color, Color.BLACK);
            paint.setColor(color);
        } finally {
            array.recycle();
        }

    }

    public DashLine(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        canvas.drawCircle(300, 300, 200, paint);
        canvas.drawLine(0, getMeasuredHeight() / 2, getMeasuredWidth(), getMeasuredHeight() / 2, paint);
    }
}
