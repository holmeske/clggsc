package com.sc.clgg.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import com.sc.clgg.R;

/**
 * @author：lvke
 * @date：2018/10/18 16:09
 */
public class CertificationProgressView extends ConstraintLayout {
    private View mView;

    public CertificationProgressView(Context context) {
        super(context);
        init();
    }

    public CertificationProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CertificationProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mView = LayoutInflater.from(getContext()).inflate(R.layout.view_certification_progress, this, false);
        addView(mView);
    }

    private void proressTo2() {
        mView.findViewById(R.id.line_1).setBackground(ContextCompat.getDrawable(getContext(), R.color._ee8031));
        mView.findViewById(R.id.iv_2).setSelected(true);
        mView.findViewById(R.id.tv_2).setSelected(true);
    }

    private void proressTo3() {
        proressTo2();
        mView.findViewById(R.id.line_2).setBackground(ContextCompat.getDrawable(getContext(), R.color._ee8031));
        mView.findViewById(R.id.iv_3).setSelected(true);
        mView.findViewById(R.id.tv_3).setSelected(true);
    }

    private void proressTo4() {
        proressTo3();
        mView.findViewById(R.id.line_3).setBackground(ContextCompat.getDrawable(getContext(), R.color._ee8031));
        mView.findViewById(R.id.iv_4).setSelected(true);
        mView.findViewById(R.id.tv_4).setSelected(true);
    }

    public void setProress(int i) {
        switch (i) {
            case 2:
                proressTo2();
                break;
            case 3:
                proressTo3();
                break;
            case 4:
                proressTo4();
                break;
            default:
                break;
        }
    }
}
