package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2019/6/19 13:56
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class Passport(var identify: A? = null, var success: Boolean = false) : Parcelable {

    @Parcelize
    data class A(var words_result: B? = null) : Parcelable {

        @Parcelize
        data class B(var 社会信用代码: C? = null, var 姓名: C? = null, var 公民身份号码: C? = null) : Parcelable {

            @Parcelize
            data class C(var words: String? = "") : Parcelable

        }

    }
}
