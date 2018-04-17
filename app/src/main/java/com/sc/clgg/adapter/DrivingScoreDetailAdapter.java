package com.sc.clgg.adapter;

import android.app.Activity;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.base.BaseAdNet;
import com.sc.clgg.bean.Behavior;
import com.sc.clgg.util.TimeHelper;

import java.util.ArrayList;

/**
 * @author lvke
 */
public class DrivingScoreDetailAdapter extends BaseAdNet<Behavior> {

    public DrivingScoreDetailAdapter(Activity activity) {
        super(activity);
    }

    @Override
    protected View setConvertView(View convertView, int position) {
        convertView = mInflater.inflate(R.layout.item_driving_score_detail, null);

        TextView tv_behavior = (TextView) convertView.findViewById(R.id.tv_behavior);
        TextView tv_occur_time = (TextView) convertView.findViewById(R.id.tv_occur_time);
        TextView tv_last_time = (TextView) convertView.findViewById(R.id.tv_last_time);

        final Behavior bean = mDatas.get(position);

        if (null != bean) {
            if (!TextUtils.isEmpty(bean.getBehavior() + "")) {
                tv_behavior.setText(behavior(bean.getBehavior()));
            }
            if (!TextUtils.isEmpty(bean.getLasting() + "")) {
                tv_last_time.setText("持续时长：" + bean.getLasting() + "秒");
                if (!TextUtils.isEmpty(bean.getOccurtime() + "")) {
                    tv_occur_time.setText(TimeHelper.long2time(TimeHelper.JAVA_TIME_FORAMTER_2, bean.getOccurtime()));
                }
            }
        }

        return convertView;
    }

    private String behavior(int status) {
        switch (status) {
            case 0:
                return "急加速";
            case 1:
                return "急减速";
            case 2:
                return "超速";
            case 3:
                return "怠速空调";
            default:
                return "未知";
        }
    }

    public void setData(ArrayList<Behavior> datas) {
        mDatas = datas;
        notifyDataSetChanged();
    }

}
