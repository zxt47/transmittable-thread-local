package com.alibaba.ttl.spi;

/**
 * a Ttl marker/tag interface, for ttl enhanced class, for example {@code TTL wrapper}.
 */
public interface TtlAttachment extends TtlEnhanced {
    void setTtlAttachment(String key, Object value);

    <T> T getTtlAttachment(String key);
}
