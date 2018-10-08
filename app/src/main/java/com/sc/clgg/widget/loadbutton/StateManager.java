package com.sc.clgg.widget.loadbutton;

import com.sc.clgg.tool.helper.LogHelper;

class StateManager {

    private boolean mIsEnabled;
    private int mProgress;

    public StateManager(CircularProgressButton progressButton) {
        mIsEnabled = progressButton.isEnabled();
        mProgress = progressButton.getProgress();
    }

    public void saveProgress(CircularProgressButton progressButton) {
        mProgress = progressButton.getProgress();
    }

    public boolean isEnabled() {
        return mIsEnabled;
    }

    public int getProgress() {
        return mProgress;
    }

    public void checkState(CircularProgressButton progressButton) {
        LogHelper.v("checkState()");
        if (progressButton.getProgress() != getProgress()) {
            progressButton.setProgress(progressButton.getProgress());
        } else if (progressButton.isEnabled() != isEnabled()) {
            progressButton.setEnabled(progressButton.isEnabled());
        }
    }
}
