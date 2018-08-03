package com.alibaba.ttl.threadpool.agent

import com.alibaba.ttl.threadpool.agent.TtlAgent.splitCommaColonStringToKV
import org.junit.Test

import org.junit.Assert.*

class TtlAgentTest {
    @Test
    fun test_splitCommaColonStringToKV() {
        val kvs = splitCommaColonStringToKV("ttl.logger:JUC")
        assertEquals(mapOf("ttl.logger" to "JUC"), kvs)
    }
}
