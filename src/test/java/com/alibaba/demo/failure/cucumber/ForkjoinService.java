package com.alibaba.demo.failure.cucumber;

import com.alibaba.ttl.threadpool.TtlExecutors;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

/**
 * @author AUTO_BEAR
 */
public class ForkjoinService {
    ForkJoinPool pool = new ForkJoinPool();
    ExecutorService excutor = TtlExecutors.getTtlExecutorService(Executors.newFixedThreadPool(4));

    public void printPool(List<String> list) {

        Map<String, String> mdcmap = TTLContext.getCopyOfContextMap();
        System.out.println("socUetExecutor的MDC:" + mdcmap.get("givenFilename"));
//        list.parallelStream().forEach(x -> printit2(x, mdcmap));

        try {
            pool.submit(() -> list.parallelStream().forEach(x -> {
                this.printit2(x, mdcmap);
            })).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }


    }
//    public void printPool3(List<String> list)  {
//        List<CompletableFuture<?>> futures = new LinkedList<>();
//        for(String x:list){
//            Future<?> response =CompletableFuture.runAsync(()->{this.printit(x);},excutor);
//            futures.add((CompletableFuture<?>) response);
//        }
//        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).handle((value, exception) -> {
//            if (value != null) {
//                return value;
//            } else {
//                return new String();
//            }
//        }).join();

//        LockSupport
//
//        CountDownLatch countDownLatch=new CountDownLatch(10);
//        countDownLatch.countDown();
//        countDownLatch.await();
//
//        CyclicBarrier barrier=new CyclicBarrier(10);
//        barrier.await();
//        barrier.reset();

    //    }
//    public void printit(String it) {
//        try {
//            Thread.sleep(1000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        System.out.println("it:"+it+"MDC:"+ .get("givenFilename")+"Thread:"+Thread.currentThread().getName());
//
//    }
    public void printit2(String it, Map<String, String> map) {
//        TTLContext.setContextMap(map);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("it:" + it + "MDC:" + TTLContext.get("givenFilename") + "Thread:" + Thread.currentThread().getName());

    }


}
