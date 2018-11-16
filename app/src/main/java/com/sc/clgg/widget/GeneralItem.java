package com.sc.clgg.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.tool.helper.MeasureHelper;

import androidx.constraintlayout.widget.ConstraintLayout;


/**
 * @author：lvke
 * @date：2018/9/26 13:42
 */
public class GeneralItem extends ConstraintLayout {
    private View mView;

    public GeneralItem(Context context) {
        super(context);
        init(context, null);
    }

    public GeneralItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public GeneralItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        mView = LayoutInflater.from(context).inflate(R.layout.view_general_item, this, true);
        TextView tv_name = mView.findViewById(R.id.tv_name);
        View v_divider_top = mView.findViewById(R.id.divider_top);
        View v_divider_bottom = mView.findViewById(R.id.divider_bottom);
        ImageView iv_arrow = mView.findViewById(R.id.iv_arrow);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.GeneralItem);

        try {
            String text = array.getString(R.styleable.GeneralItem_item_text);
            Drawable drawable = array.getDrawable(R.styleable.GeneralItem_item_icon);
            Drawable arrow = array.getDrawable(R.styleable.GeneralItem_item_arrow);
            boolean divider_top = array.getBoolean(R.styleable.GeneralItem_item_divider_top, false);
            boolean divider_bottom = array.getBoolean(R.styleable.GeneralItem_item_divider_bottom, false);
            if (text != null) {
                tv_name.setText(text);
            }
            if (drawable != null) {
                tv_name.setCompoundDrawablePadding(MeasureHelper.dp2px(context, 15));
                tv_name.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
            }
            if (arrow != null) {
                iv_arrow.setImageDrawable(arrow);
            }
            if (divider_top) {
                v_divider_top.setVisibility(View.VISIBLE);
            }
            if (divider_bottom) {
                v_divider_bottom.setVisibility(View.VISIBLE);
            }
        } finally {
            array.recycle();
        }

    }


}
