package com.sc.clgg.base;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 适配器 基类
 *
 * @author ZhangYi 2014-3-28 下午9:31:56
 */
public abstract class BaseAdNet<T> extends BaseAdapter {
    protected Activity mActivity;
    protected LayoutInflater mInflater;
    protected List<T> mDatas;

    public BaseAdNet(Activity activity) {
        mActivity = activity;
        mInflater = LayoutInflater.from(mActivity);
    }

    @Override
    public int getCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    @Override
    public T getItem(int arg0) {
        return mDatas.get(arg0);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return setConvertView(convertView, position);
    }

    protected abstract View setConvertView(View convertView, int position);
}
