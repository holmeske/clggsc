package com.sc.clgg.adapter.holder;


import android.support.v7.widget.RecyclerView;
import android.view.View;

public class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    private View mView;

    public BaseRecyclerViewHolder(View view) {
        super(view);
        mView = view;
    }

    public View getView() {
        return mView;
    }
}
