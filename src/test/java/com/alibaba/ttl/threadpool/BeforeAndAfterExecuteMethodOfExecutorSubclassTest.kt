package com.alibaba.ttl.threadpool

import com.alibaba.support.junit.conditional.ConditionalIgnoreRule
import com.alibaba.support.junit.conditional.ConditionalIgnoreRule.ConditionalIgnore
import com.alibaba.support.junit.conditional.IsAgentRun
import com.alibaba.support.junit.conditional.NoAgentRun
import com.alibaba.ttl.TtlRunnable
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class MyThreadPool : ThreadPoolExecutor(10, 20, 10, TimeUnit.SECONDS, LinkedBlockingQueue()) {
    val list = CopyOnWriteArrayList<Boolean>()

    override fun afterExecute(r: Runnable, t: Throwable?) {
        list.add(r is TtlRunnable)
        super.afterExecute(r, t)
    }

    override fun beforeExecute(t: Thread, r: Runnable) {
        list.add(r is TtlRunnable)

        super.beforeExecute(t, r)
    }
}

class BeforeAndAfterExecuteMethodOfExecutorSubclassTest {
    @Test
    @ConditionalIgnore(condition = NoAgentRun::class)
    fun underAgent() {
        val myThreadPool = MyThreadPool()

        (0 until 10).map {
            myThreadPool.execute { Thread.sleep(1) }
        }

        Thread.sleep(100)

        assertEquals(20, myThreadPool.list.size)
        assertTrue(myThreadPool.list.all { !it })
    }

    @Test
    @ConditionalIgnore(condition = IsAgentRun::class)
    fun noAgent() {
        val myThreadPool = MyThreadPool()

        val ttlThreadPool = myThreadPool.let {
            it.setKeepAliveTime(10, TimeUnit.SECONDS)
            TtlExecutors.getTtlExecutorService(it)
        }!!

        (0 until 10).map {
            ttlThreadPool.execute { Thread.sleep(1) }
        }

        Thread.sleep(100)

        assertEquals(20, myThreadPool.list.size)
        assertTrue(myThreadPool.list.all { it })
    }

    @Rule
    @JvmField
    val rule = ConditionalIgnoreRule()
}
