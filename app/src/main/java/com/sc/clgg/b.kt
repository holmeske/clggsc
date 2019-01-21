package com.sc.clgg

import kotlinx.coroutines.*

val threadLocal = ThreadLocal<String?>() // 声明线程局部变量
fun main()= runBlocking<Unit>{

    threadLocal.set("main")
    println("Pre-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
    val job = launch(Dispatchers.Default + threadLocal.asContextElement(value = "launch")) {
        println("Launch start, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
        yield()
        println("After yield, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")
    }
    job.join()
    println("Post-main, current thread: ${Thread.currentThread()}, thread local value: '${threadLocal.get()}'")

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