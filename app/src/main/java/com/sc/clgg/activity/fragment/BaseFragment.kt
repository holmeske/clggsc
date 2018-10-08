package com.sc.clgg.activity.fragment


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.View
import com.sc.clgg.tool.helper.LogHelper

open class BaseFragment : Fragment() {
    private var isViewCreated: Boolean = false // 标识fragment视图已经初始化完毕
    //private var hasFetchData: Boolean = false // 标识已经触发过懒加载数据

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser && isViewCreated) {
            onLazyFetchData()
        }
    }

    open fun onLazyFetchData() {
        LogHelper.e("onLazyFetchData() ------>" + this.javaClass.simpleName)
    }

}
