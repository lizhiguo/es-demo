package com.qp.esdemo

import kotlinx.coroutines.*
import org.junit.jupiter.api.Test

class BaseCoroutines {
    @Test
    fun `桥接阻塞与非阻塞的世界`(){
        GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L)
            println("World!")
        }
        println("Hello,") // 主线程中的代码会立即执行
        runBlocking {     // 但是这个表达式阻塞了主线程
            delay(2000L)  // ……我们延迟 2 秒来保证 JVM 的存活
        }
    }

    @Test
    fun `等待一个作业`() = runBlocking{
       val job =  GlobalScope.launch { // 在后台启动一个新的协程并继续
            delay(1000L)
            println("World!")
        }
        println("Hello,") // 主线程中的代码会立即执行
        job.join()// 等待直到子协程执行结束
    }
    @Test
    fun `作用域构建器`() = runBlocking{
        launch {
            delay(200L)
            println("Task from runBlocking")
        }
        coroutineScope { // 创建一个协程作用域
            launch {
                delay(500L)
                println("Task from nested launch")
            }
            delay(100L)
            println("Task from coroutine scope") // 这一行会在内嵌 launch 之前输出
        }
        println("Coroutine scope is over") // 这一行在内嵌 launch 执行完毕后才输出
    }
    @Test
    fun `提取函数重构`() = runBlocking{
        launch { doWorld() }
        println("Hello,")
        //输出
        //Hello,
        //World!
    }
    // 这是你的第一个挂起函数
    suspend fun doWorld() {
        delay(1000L)
        println("World!")
    }

    @Test
    fun `经典多线程任务替换`() = runBlocking{
        //它启动了 10 万个协程，并且在 5 秒钟后，每个协程都输出一个点。
        repeat(100_000) { // 启动大量的协程
            launch {
                delay(5000L)
                print(".")
            }
        }
    }
    @Test
    fun `全局协程像守护线程`() = runBlocking{
        GlobalScope.launch {
            repeat(1000) { i ->
                println("I'm sleeping $i ...")
                delay(500L)
            }
        }
        delay(1300L) // 在延迟后退出
        //运行这个程序并看到它输出了以下三行后终止：
        /*I'm sleeping 0 ...
        I'm sleeping 1 ...
        I'm sleeping 2 ...*/
        //在 GlobalScope 中启动的活动协程并不会使进程保活。它们就像守护线程。
    }
}