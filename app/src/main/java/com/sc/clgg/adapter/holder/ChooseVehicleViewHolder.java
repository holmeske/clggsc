package com.sc.clgg.adapter.holder;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sc.clgg.R;

public class ChooseVehicleViewHolder extends BaseRecyclerViewHolder {

    public TextView carno, tv_car_status;

    public View view_driver;

    public LinearLayout mainLayout;

    public ChooseVehicleViewHolder(View view) {
        super(view);
        carno = (TextView) view.findViewById(R.id.tv_car_no);
        tv_car_status = (TextView) view.findViewById(R.id.tv_car_status);
        view_driver = view.findViewById(R.id.view_driver);
        mainLayout = (LinearLayout) view.findViewById(R.id.linear_content);
    }

}
