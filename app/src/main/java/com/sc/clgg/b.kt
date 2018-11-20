package com.sc.clgg

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author：lvke
 * @date：2018/11/10 14:38
 */
object b {

    @JvmStatic
    fun main(args: Array<String>) {
        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L) // 无阻塞的等待1秒钟（默认时间单位是毫秒）
            println("World!") // 在延迟后打印输出
        }
        println("Hello,") // 主线程的协程将会继续等待
        Thread.sleep(2000L) // 阻塞主线程2秒钟来保证 JVM 存活
    }


}