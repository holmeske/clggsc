package com.sc.clgg.mvvm.vm;

import com.sc.clgg.bean.Banner;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * @author：lvke
 * @date：2018/12/5 11:19
 */
public class MyViewModel extends ViewModel {
    private MutableLiveData<Banner> banner;

    public LiveData<Banner> getBanner() {
        if (banner == null) {
            banner = new MutableLiveData<>();
        }
        return banner;
    }

}
