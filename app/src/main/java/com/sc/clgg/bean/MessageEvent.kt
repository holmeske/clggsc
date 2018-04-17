package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/8 10:18
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class MessageEvent(var value: Int) : Parcelable