package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/24 15:01
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class StoreDetailBean(var ID: Int = 0,// 店铺ID
                           var NAME: String? = null,// 店铺名称
                           var PICTURE: String? = null,// 店铺图片
                           var NUM: Int = 0,// 收藏数
                           var ADDRESS: String? = null,// 地址
                           var PHONENUM: String? = null,// 紧急电话
                           var UPDATE2: String? = null,// 店铺营业时间
                           var PHONE: String? = null,// 电话
                           var CONTACT: String? = null,// 紧急联系人
                           var STATUS: Int = 0,// 店铺可用状态
                           var COLLECTIONSTATUS: Int = 0  // 店铺收藏状态
) : Parcelable

