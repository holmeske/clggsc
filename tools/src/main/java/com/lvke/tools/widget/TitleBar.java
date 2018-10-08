package com.lvke.tools.widget;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lvke.tools.R;

/**
 * @author：lvke
 * @date：2017/10/31 11:19
 */

public class TitleBar extends LinearLayout {
    private TextView mTvTitle, mTvLeft, mTvRight;

    public TitleBar(Context context) {
        super(context);
        init(context);
    }

    public TitleBar(final Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);

        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TitleBar);

        String title = array.getString(R.styleable.TitleBar_title);
        String leftText = array.getString(R.styleable.TitleBar_left_text);
        int leftImg = array.getResourceId(R.styleable.TitleBar_left_img, 0);
        String rightText = array.getString(R.styleable.TitleBar_right_text);
        int rightImg = array.getResourceId(R.styleable.TitleBar_right_img, 0);

        array.recycle();

        if (!TextUtils.isEmpty(leftText)) {
            mTvLeft.setText(leftText);
        } else if (leftImg != 0) {
            mTvLeft.setBackgroundResource(leftImg);
        }

        if (!TextUtils.isEmpty(title)) {
            mTvTitle.setText(title);
        }

        if (!TextUtils.isEmpty(rightText)) {
            mTvRight.setText(rightText);
        } else if (rightImg != 0) {
            mTvRight.setBackgroundResource(rightImg);
        }

        mTvLeft.setOnClickListener(v -> {
            if (context instanceof Activity) {
                ((Activity) context).finish();
            }
        });
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.titlebar, this, false);
        mTvLeft = view.findViewById(R.id.tv_left);
        mTvTitle = view.findViewById(R.id.tv_center);
        mTvRight = view.findViewById(R.id.tv_right);
        addView(view, new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
    }
}
