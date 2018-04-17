package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import android.support.annotation.DrawableRes
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/24 15:25
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class TypeBean(@DrawableRes var unCheckedResId: Int = 0,
                    @DrawableRes var checkedResId: Int = 0,
                    var name: String? = null,
                    var isChecked: Boolean = false,
                    var id: String? = null) : Parcelable

