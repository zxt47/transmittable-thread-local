package com.alibaba.ttl.threadpool;

import com.alibaba.ttl.TransmittableThreadLocal;
import com.alibaba.ttl.TtlCallable;
import com.alibaba.ttl.TtlRunnable;
import com.alibaba.ttl.spi.TtlAttachmentDelegate;
import com.alibaba.ttl.spi.TtlEnhanced;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;

/**
 * {@link TransmittableThreadLocal} Wrapper of {@link Executor},
 * transmit the {@link TransmittableThreadLocal} from the task submit time of {@link Runnable}
 * to the execution time of {@link Runnable}.
 *
 * @author Jerry Lee (oldratlee at gmail dot com)
 * @since 0.9.0
 */
class ExecutorTtlWrapper implements Executor, TtlEnhanced {
    private final Executor executor;

    ExecutorTtlWrapper(@Nonnull Executor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(@Nonnull Runnable command) {
        executor.execute(getTtlRunnableWithAttachments(command));
    }

    @Nonnull
    public Executor unwrap() {
        return executor;
    }

    static TtlRunnable getTtlRunnableWithAttachments(Runnable command) {
        final TtlRunnable ttlRunnable = TtlRunnable.get(command);
        TtlAttachmentDelegate.setAutoWrapper(ttlRunnable);
        return ttlRunnable;
    }

    static <T> TtlCallable<T> getTtlCallableWithAttachments(@Nonnull Callable<T> task) {
        final TtlCallable<T> ttlCallable = TtlCallable.get(task);
        TtlAttachmentDelegate.setAutoWrapper(ttlCallable);
        return ttlCallable;
    }

    <T> List<TtlCallable<T>> getTtlCallableWithAttachments(Collection<? extends Callable<T>> tasks) {
        final List<TtlCallable<T>> list = TtlCallable.gets(tasks);
        if (list != null) for (TtlCallable<T> callable : list) {
            if (callable != null) {
                TtlAttachmentDelegate.setAutoWrapper(callable);
            }
        }
        return list;
    }
}
