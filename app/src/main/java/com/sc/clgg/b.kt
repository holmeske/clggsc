package com.sc.clgg


fun main() {
    bar(null)
    bar("42")
    var s = "37011801220102886102"
    println(s.substring(s.length - 4))
}

fun bar(x: String?) {
    if (!x.isNullOrEmpty()) {
        println("length of '$x' is ${x.length}") // 哇，已经智能转换为非空！
    }
}