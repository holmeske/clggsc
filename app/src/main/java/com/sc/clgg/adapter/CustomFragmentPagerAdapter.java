package com.sc.clgg.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Lifecycle;

import java.util.List;

/**
 * @author：lvke
 * @date：2019/5/31 13:24
 */
public class CustomFragmentPagerAdapter extends FragmentPagerAdapter {
    private Fragment mCurrentPrimaryItem;
    private FragmentTransaction ft;
    private List<Fragment> fragmentList;

    public CustomFragmentPagerAdapter(@NonNull FragmentManager fm, int behavior, List<Fragment> fragmentList) {
        super(fm, behavior);
        this.fragmentList = fragmentList;
        ft = fm.beginTransaction();
    }

    @Override
    public void setPrimaryItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        super.setPrimaryItem(container, position, object);
        Fragment fragment = (Fragment) object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                ft.setMaxLifecycle(mCurrentPrimaryItem, Lifecycle.State.STARTED);
            }
            ft.setMaxLifecycle(fragment, Lifecycle.State.RESUMED);
            mCurrentPrimaryItem = fragment;
        }
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }
}
