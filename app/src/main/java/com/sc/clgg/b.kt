package com.sc.clgg

import com.sc.clgg.bean.CertificationInfo


fun main() {
    var certificationInfo = CertificationInfo()
    certificationInfo.certType = "营业执照"

    certificationInfo?.certType?.let {
        fun updateCertType(string: String) {
            certificationInfo?.certType = string
        }
        when (it) {
            "身份证含临时身份证" -> updateCertType("101")
            "护照" -> updateCertType("102")
            "港澳居民来往内地通行证" -> updateCertType("103")
            "台湾居民来往大陆通行证" -> updateCertType("104")
            "统一社会信用代码证书" -> updateCertType("201")
            "组织机构代码证" -> updateCertType("202")
            "营业执照" -> updateCertType("203")
            else -> {
            }
        }
    }

    println(certificationInfo?.certType)
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