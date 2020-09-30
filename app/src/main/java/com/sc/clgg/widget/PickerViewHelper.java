package com.sc.clgg.widget;

import android.app.Activity;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.sc.clgg.R;
import com.sc.clgg.bean.PickerBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @author：lvke
 * @date：2018/10/16 15:24
 */
public class PickerViewHelper {

    /**
     * 条件选择器
     * @param context 上下文
     * @param data 数组
     * @param listener
     * @return
     */
    public OptionsPickerView creat(Activity context,List<String> data, OnOptionsSelectListener listener) {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                if (listener != null) {
                    listener.onOptionsSelect(options1, option2, options3, v);
                }
            }
        }).setCancelColor(ContextCompat.getColor(context, R.color.transparent))
                .setSubmitText("完成")
                .setSubCalSize(15)
                .setSubmitColor(ContextCompat.getColor(context, R.color.black_333))
                .setLineSpacingMultiplier(2.5f)
                .setContentTextSize(15)
                .setTextColorOut(ContextCompat.getColor(context, R.color._9c9c9c))
                .setTextColorCenter(ContextCompat.getColor(context, R.color.blue))
                .setDividerColor(ContextCompat.getColor(context, R.color._ccc))
                .build();
        pvOptions.setPicker(data);
        pvOptions.show();
        return pvOptions;
    }

    /**
     * 条件选择器
     *
     * @param context  上下文
     * @param data     数组
     * @param listener
     * @return
     */
    public OptionsPickerView creatBean(Activity context, List<PickerBean> data, OnOptionsSelectListener listener) {
        List<String> list = new ArrayList<String>();
        for (PickerBean bean : data) {
            list.add(bean.getName());
        }
        OptionsPickerView pvOptions = new OptionsPickerBuilder(context, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                if (listener != null) {
                    listener.onOptionsSelect(options1, option2, options3, v);
                }
            }
        }).setCancelColor(ContextCompat.getColor(context, R.color.transparent))
                .setSubmitText("完成")
                .setSubCalSize(15)
                .setSubmitColor(ContextCompat.getColor(context, R.color.black_333))
                .setLineSpacingMultiplier(2.5f)
                .setContentTextSize(15)
                .setTextColorOut(ContextCompat.getColor(context, R.color._9c9c9c))
                .setTextColorCenter(ContextCompat.getColor(context, R.color.blue))
                .setDividerColor(ContextCompat.getColor(context, R.color._ccc))
                .build();
        pvOptions.setPicker(list);
        pvOptions.show();
        return pvOptions;
    }
}
