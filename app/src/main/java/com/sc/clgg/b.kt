package com.sc.clgg

import java.util.*

fun main() {
    val data = ArrayList<Float>()
    (Math.ceil((Collections.max(data) / 100f).toDouble()) * 100f).toFloat()
    print(sum(1, 2))
}

fun sum(a: Int, b: Int) = a + b