package com.sc.clgg.bean

import android.annotation.SuppressLint
import java.util.*

/**
 * @author：lvke
 * @date：2018/2/24 15:03
 */
@SuppressLint("ParcelCreator")
data class StoreInfoDetailBean(var result: ArrayList<StoreDetailBean>, var svresult: ArrayList<ServiceBean>, var newsresult: ArrayList<NewsBean>) : BaseBean()

