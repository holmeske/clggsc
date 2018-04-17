package com.sc.clgg.adapter;

import android.app.Activity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.base.BaseAdNet;
import com.sc.clgg.bean.Report;

import java.util.ArrayList;

/**
 * 选择车辆 适配器
 *
 * @author ZhangYi 2014-12-4 14:08:39
 */
public class MileageAdapter extends BaseAdNet<Report> {

    private boolean isMile = false;

    private int progressMax = 0;

    public MileageAdapter(Activity activity) {
        super(activity);
    }

    @Override
    protected View setConvertView(View convertView, int position) {
        convertView = mInflater.inflate(R.layout.item_mileage, null);
        TextView tv_windex = (TextView) convertView.findViewById(R.id.tv_windex);  //第几周
        ProgressBar color_progressBar = (ProgressBar) convertView.findViewById(R.id.color_progressBar);
        TextView tv_count = (TextView) convertView.findViewById(R.id.tv_count);  //
        TextView tv_count_type = (TextView) convertView.findViewById(R.id.tv_count_type);  //
        TextView tv_start_t_end_t = (TextView) convertView.findViewById(R.id.tv_start_t_end_t);  //
        TextView tv_bp_1 = (TextView) convertView.findViewById(R.id.tv_bp_1);  //
        TextView tv_bp_2 = (TextView) convertView.findViewById(R.id.tv_bp_2);  //
        TextView tv_bp_3 = (TextView) convertView.findViewById(R.id.tv_bp_3);  //
        TextView tv_bp_4 = (TextView) convertView.findViewById(R.id.tv_bp_4);  //
        Report bean = null;
        if (mDatas.get(position) != null) {
            bean = mDatas.get(position);
        }
        if (!isMile) {
            tv_bp_4.setVisibility(View.GONE);
            tv_bp_3.setVisibility(View.GONE);
            tv_bp_1.setText("平均油耗 ：");
            tv_count_type.setText("升");
        }

        if (bean != null && bean.getStartWeekDate() != null && bean.getEndWeekDate() != null) {
            tv_start_t_end_t.setText(bean.getStartWeekDate() + " - " + bean.getEndWeekDate());
        }

        tv_windex.setText("第" + (position + 1) + "周");
        color_progressBar.setMax(progressMax);
        if (null != bean) {
            if (!isMile) {
                color_progressBar.setProgress((int) bean.getOilcost()); //油耗
                tv_count.setText(bean.getOilcost() + "");
                tv_bp_2.setText(bean.getOilcostphkm() + "升/百公里");
            } else {
                color_progressBar.setProgress((int) bean.getWorkingmileage()); //里程
                tv_count.setText(bean.getWorkingmileage() + "");
                tv_bp_2.setText(bean.getRunningTime() + "小时");
                tv_bp_4.setText(bean.getIdlepercentage() + "%");
            }
        } else {
            tv_count.setText("0");
            if (isMile) {
                tv_bp_2.setText("0小时");
                tv_bp_4.setText("0%");
            } else {
                tv_bp_2.setText("0升/百公里");
            }
        }
        return convertView;
    }

    public void setData(ArrayList<Report> datas, boolean isMile, int progressMax) {
        mDatas = datas;
        this.isMile = isMile;
        this.progressMax = progressMax;
        notifyDataSetChanged();
    }

}
