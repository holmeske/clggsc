package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/2/24 14:52
 */
@SuppressLint("ParcelCreator")
data class OptionsListBean(var codeList: List<Options>? = null) : BaseBean() {
    @Parcelize
    data class Options(var code: String? = null,
                       var codeId: String? = null,
                       var codeNameC: String? = null) : Parcelable
}

