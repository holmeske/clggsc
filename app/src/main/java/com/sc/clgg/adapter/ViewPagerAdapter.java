package com.sc.clgg.adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Author：lvke
 * CreateDate：2017/8/21 13:27
 */

public class ViewPagerAdapter extends PagerAdapter {

    List<View> views;

    public ViewPagerAdapter(List<View> views) {
        this.views = views;
    }


    @Override
    public int getCount() {
        return views == null ? 0 : views.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(views.get(position));
        return views.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
//        super.destroyItem(container, position, object);
        container.removeView(views.get(position));
    }
}
