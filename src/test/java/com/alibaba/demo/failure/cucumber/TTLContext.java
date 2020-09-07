package com.alibaba.demo.failure.cucumber;

import com.alibaba.ttl.TransmittableThreadLocal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * TTL上下文与MDC作用相同
 *
 * @author ZXT
 * @version 1.0
 * @date 2020/9/7 13:31
 * @since 1.0
 */
public class TTLContext {
    static final TransmittableThreadLocal<Map<String, String>> transmittableThreadLocal = new TransmittableThreadLocal<>();

    public static ThreadLocal<Map<String, String>> getThreadLocal() {
        return transmittableThreadLocal;
    }

    public static Map<String, String> getCopyOfContextMap() {
        Map<String, String> hashMap = transmittableThreadLocal.get();
        if (hashMap == null) {
            return new HashMap<>();
        } else {
            return new HashMap<>(hashMap);
        }
    }

    public static String get(String key) {
        final Map<String, String> map = transmittableThreadLocal.get();
        if ((map != null) && (key != null)) {
            return map.get(key);
        } else {
            return null;
        }
    }

    public static void setContextMap(Map<String, String> contextMap) {

        Map<String, String> newMap = Collections.synchronizedMap(new HashMap<String, String>());
        newMap.putAll(contextMap);

        // the newMap replaces the old one for serialisation's sake
        transmittableThreadLocal.set(newMap);
    }

    public static void put(String key, String val) {
        if (key == null) {
            throw new IllegalArgumentException("key cannot be null");
        }

        Map<String, String> oldMap = transmittableThreadLocal.get();

        if (oldMap == null) {
            Map<String, String> newMap = new HashMap<>();
            newMap.put(key, val);
            transmittableThreadLocal.set(newMap);
        } else {
            oldMap.put(key, val);
        }
    }

    public static void clear() {
        transmittableThreadLocal.remove();
    }
}
