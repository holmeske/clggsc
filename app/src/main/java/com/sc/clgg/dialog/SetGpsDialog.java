package com.sc.clgg.dialog;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.sc.clgg.R;
import com.sc.clgg.widget.loadbutton.CircularProgressButton;

public class SetGpsDialog extends CLGGBaseDialog {

    public CircularProgressButton markSureTxt;
    private Context mContext;
    private TextView giveUpBtn;
    private TextView promtTxt;

    public SetGpsDialog(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected View getView() {
        mView = mInflater.inflate(R.layout.dialog_gps, null);
        markSureTxt = (CircularProgressButton) mView.findViewById(R.id.markSureTxt);
        markSureTxt.setText("去设置");
        giveUpBtn = (TextView) mView.findViewById(R.id.giveUpBtn);
        promtTxt = (TextView) mView.findViewById(R.id.promtTxt);
        promtTxt.setText("定位服务—>GPS未打开，车轮滚滚需要访问您所在的位置，以便能追踪记录您的行驶路线");
        giveUpBtn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });
        markSureTxt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                mContext.startActivity(intent);
                dismiss();
            }
        });
        return mView;
    }
}
