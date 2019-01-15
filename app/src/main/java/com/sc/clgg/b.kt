package com.sc.clgg

import com.sc.clgg.bean.UserInfoBean

fun main() {

    var result2 = UserInfoBean().let {
        it?.id = "16"//这行其实是在调用setAge函数,如果这行为最后一行，则没有返回值

          it?.account
    }
    println("let 返回值 = $result2")

    /*for (i in 1..3) {
        println(i)
    }

    bar(null)
    bar("42")
//    var s = "370118012R20102886102"
//    println(s.substring(s.length - 4))

    arrayListOf(1, 3, 5, 7)
            // 过滤出集合中大于 3 的元素
            .filter { it > 3 }
            // 转换成字符串
            .map { "$it, " }
            // 循环输出
            .forEach { println(it) }*/
}

fun bar(x: String?) {
    if (!x.isNullOrEmpty()) {
        println("length of '$x' is ${x.length}") // 哇，已经智能转换为非空！
    }
}