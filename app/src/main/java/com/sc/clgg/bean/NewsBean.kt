package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/24 14:49
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class NewsBean(var TYPE: Int = 0,// 新闻类型
                    var NEWSCONTENT: String? = null,// 新闻内容
                    var TITLE: String? = null// 新闻标题
) : Parcelable

