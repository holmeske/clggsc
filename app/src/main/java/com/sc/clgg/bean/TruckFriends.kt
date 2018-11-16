package com.sc.clgg.bean

import android.annotation.SuppressLint
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author：lvke
 * @date：2018/6/20 13:24
 */
@SuppressLint("ParcelCreator")
@Parcelize
data class TruckFriend(var code: Int = 0,
                       var msg:String?="",
                       var pageInfo: PageInfo? = null,
                       var success: Boolean = false) : Parcelable {
    @Parcelize
    data class PageInfo(var pageNum: Int = 0,
                        var pageSize: Int = 0,
                        var size: Int = 0,
                        var orderBy: Int = 0,
                        var startRow: Int = 0,
                        var endRow: Int = 0,
                        var total: Int = 0,
                        var pages: Int = 0,
                        var firstPage: Int = 0,
                        var prePage: Int = 0,
                        var nextPage: Int = 0,
                        var lastPage: Int = 0,
                        var isFirstPage: Boolean = false,
                        var isLastPage: Boolean = false,
                        var hasPreviousPage: Boolean = false,
                        var hasNextPage: Boolean = false,
                        var navigatePages: Int = 0,
                        var navigatepageNums: Int = 0,
                        var list: List<A>? = null) : Parcelable

    @Parcelize
    data class A(var id: Int = 0,
                 var userId: Int = 0,
                 var message: String? = "",
                 var createTime: String? = "",
                 var dealTime: String? = "",
                 var nickName: String? = "",
                 var headImg: String? = "",
                 var clientSign: String? = "",
                 var images: String? = "",
                 var isShowFront: String? = "",
                 var type: Int = 0,
                 var driverCircleCommentList: ArrayList<Commen>? = null,
                 var driverCircleLaudList: List<Laud>? = null,
                 var driverCircleImagesList: List<Image>? = null,
                 var isLike:Boolean=false) : Parcelable

    @Parcelize
    data class Commen(var id: Int = 0,
                      var circleMessageId: Int = 0,
                      var commentUserId: Int = 0,
                      var userId: Int = 0,
                      var createTime: String? = "",
                      var isRead: Int = 0,
                      var comment: String? = "",
                      var isAvailable: Int = 0,
                      var nickName: String? = "") : Parcelable

    @Parcelize
    data class Laud(var id: Int = 0,
                    var circleMessageId: Int = 0,
                    var laudUserId: Int = 0,
                    var userId: Int = 0,
                    var createTime: String? = "",
                    var isRead: Int = 0,
                    var isAvailable: Int = 0,
                    var nickName: String? = "") : Parcelable

    @Parcelize
    data class Image(var id: Int = 0,
                     var userId: Int = 0,
                     var circleMessageId: Int = 0,
                     var imgUrl: String? = "") : Parcelable
}

@SuppressLint("ParcelCreator")
@Parcelize
data class InteractiveDetail(var code: Int = 0,
                       var msg:String?="",
                       var detail: A? = null,
                       var success: Boolean = false) : Parcelable {


    @Parcelize
    data class A(var id: Int = 0,
                 var userId: Int = 0,
                 var message: String? = "",
                 var createTime: String? = "",
                 var dealTime: String? = "",
                 var nickName: String? = "",
                 var headImg: String? = "",
                 var clientSign: String? = "",
                 var images: String? = "",
                 var isShowFront: String? = "",
                 var type: Int = 0,
                 var driverCircleCommentList: List<Commen>? = null,
                 var driverCircleLaudList: List<Laud>? = null,
                 var driverCircleImagesList: List<Image>? = null,
                 var isLike:Boolean=false) : Parcelable

    @Parcelize
    data class Commen(var id: Int = 0,
                      var circleMessageId: Int = 0,
                      var commentUserId: Int = 0,
                      var userId: Int = 0,
                      var createTime: String? = "",
                      var isRead: Int = 0,
                      var comment: String? = "",
                      var isAvailable: Int = 0,
                      var nickName: String? = "",
                      var headImg: String? = "") : Parcelable

    @Parcelize
    data class Laud(var id: Int = 0,
                    var circleMessageId: Int = 0,
                    var laudUserId: Int = 0,
                    var userId: Int = 0,
                    var createTime: String? = "",
                    var isRead: Int = 0,
                    var isAvailable: Int = 0,
                    var nickName: String? = "") : Parcelable

    @Parcelize
    data class Image(var id: Int = 0,
                     var userId: Int = 0,
                     var circleMessageId: Int = 0,
                     var imgUrl: String? = "") : Parcelable
}

