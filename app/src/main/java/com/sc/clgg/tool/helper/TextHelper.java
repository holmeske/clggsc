package com.sc.clgg.tool.helper;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import androidx.annotation.ColorRes;
import androidx.core.content.ContextCompat;

public enum TextHelper {
    instance;

    /**
     * @param context 上下文
     * @param s1      字符串
     * @param dp1     textSize 单位px
     * @param id1     颜色
     * @param s2      字符串
     * @param dp2     textSize 单位px
     * @param id2     颜色
     * @return
     */
    public Spannable setTextStyle(Context context, String s1, int dp1, @ColorRes int id1, String s2, int dp2, @ColorRes int id2) {

        Spannable spannable = new SpannableString(s1 + s2);
        spannable.setSpan(new AbsoluteSizeSpan(MeasureHelper.dp2px(context, dp1)), 0, s1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, id1)), 0, s1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        if (!TextUtils.isEmpty(s2) && MeasureHelper.dp2px(context, dp2) > 0) {
            spannable.setSpan(new AbsoluteSizeSpan(MeasureHelper.dp2px(context, dp2)), s1.length(), spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, id2)), s1.length(), spannable.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    public String zeroPadding(int value) {
        if (value >= 0 && value < 10) {
            return "0" + value;
        } else {
            return String.valueOf(value);
        }
    }

    public Spannable setSpannable(Context context, String[] strArray, @ColorRes int[] colorArray, int[] spArray) {
        StringBuilder sb = new StringBuilder();
        if (strArray.length == colorArray.length && colorArray.length == spArray.length) {
        } else {
            throw new RuntimeException("数组长度不相等");
        }
        for (Integer i : spArray) {
            if (i <= 0) {
                break;
            }
        }
        for (String s : strArray) {
            if (TextUtils.isEmpty(s)) {
                break;
            }
            sb.append(s);
        }
        Spannable spannable = new SpannableString(sb);
//        LogHelper.e("sb = " + sb + "             长度为：" + sb.length());

        for (int i = 0, size = strArray.length; i < size; i++) {
            int startPosition;
            if (i == 0) {
                startPosition = 0;
            } else {
                startPosition = getLength(strArray, i - 1);
            }
            int endPosition = getLength(strArray, i);

//            LogHelper.e("startPosition = " + startPosition + "  endPosition = " + endPosition);
            spannable.setSpan(new AbsoluteSizeSpan(MeasureHelper.sp2px(context, spArray[i])), startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, colorArray[i])), startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        }
        return spannable;
    }

    public Spannable setSpannable(Context context, String[] strArray, @ColorRes int[] colorArray, int[] spArray, int[] textStyles) {
        StringBuilder sb = new StringBuilder();
        if (strArray.length == colorArray.length && colorArray.length == spArray.length) {
        } else {
            throw new RuntimeException("数组长度不相等");
        }
        for (Integer i : spArray) {
            if (i <= 0) {
                break;
            }
        }
        for (String s : strArray) {
            if (TextUtils.isEmpty(s)) {
                break;
            }
            sb.append(s);
        }
        Spannable spannable = new SpannableString(sb);
//        LogHelper.e("sb = " + sb + "             长度为：" + sb.length());

        for (int i = 0, size = strArray.length; i < size; i++) {
            int startPosition;
            if (i == 0) {
                startPosition = 0;
            } else {
                startPosition = getLength(strArray, i - 1);
            }
            int endPosition = getLength(strArray, i);

//            LogHelper.e("startPosition = " + startPosition + "  endPosition = " + endPosition);
            spannable.setSpan(new AbsoluteSizeSpan(MeasureHelper.sp2px(context, spArray[i])), startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            spannable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(context, colorArray[i])), startPosition, endPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

            spannable.setSpan(new StyleSpan(textStyles[i]), startPosition, endPosition, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannable;
    }

    private int getLength(String[] strings, int position) {
        int length = 0;
        for (int i = 0; i <= position; i++) {
            length += strings[i].length();
        }
        return length;
    }

}
