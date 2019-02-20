package com.sc.clgg.mvvm.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.sc.clgg.bean.Banner
import com.sc.clgg.bean.VersionInfoBean

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

    private lateinit var versionInfo: MutableLiveData<VersionInfoBean>

    fun getVersionInfo(): MutableLiveData<VersionInfoBean> {
        if (!::versionInfo.isInitialized) {
            versionInfo = MutableLiveData()
        }
        return versionInfo
    }
}
