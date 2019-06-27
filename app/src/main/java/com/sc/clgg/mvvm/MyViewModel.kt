package com.sc.clgg.mvvm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sc.clgg.bean.Banner

/**
 * @author：lvke
 * @date：2018/12/5 11:19
 */
class MyViewModel : ViewModel() {
    // 创建一个 String 类型的 LiveData
    // MutableLiveData 是抽象类 LiveData 的子类，我们一般使用的是 MutableLiveData
    private lateinit var banner: MutableLiveData<Banner>

    fun getBanner(): MutableLiveData<Banner> {
        if (!::banner.isInitialized) {
            banner = MutableLiveData()
        }
        return banner
    }

}
