package com.alibaba.ttl.spi;

import javax.annotation.Nonnull;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class TtlAttachmentDelegate implements TtlAttachment {
    private volatile ConcurrentMap<String, Object> attachment = new ConcurrentHashMap<String, Object>();

    @Override
    public void setTtlAttachment(String key, Object value) {
        attachment.put(key, value);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getTtlAttachment(String key) {
        return (T) attachment.get(key);
    }

    public static void setAutoWrapper(Object ttlAttachment) {
        if (!(ttlAttachment instanceof TtlAttachment)) return;
        ((TtlAttachment) ttlAttachment).setTtlAttachment(TtlAttachment.KEY_IS_AUTO_WRAPPER, true);
    }

    public static boolean isAutoWrapper(@Nonnull TtlAttachment ttlAttachment) {
        return ttlAttachment.getTtlAttachment(TtlAttachment.KEY_IS_AUTO_WRAPPER);
    }
}
