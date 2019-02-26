package com.sc.clgg.bean

/**
 * 4 微信支付
 */
data class WxPayEvent(var value: Int)

/**
 * 卡友圈评论、发动态
 */
data class CommentEvent(var value: Int)

/**
 * 返回“车联网”首页
 */
data class CarNetEvent(var value: Int)