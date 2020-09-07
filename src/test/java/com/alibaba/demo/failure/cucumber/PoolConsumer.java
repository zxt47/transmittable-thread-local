package com.alibaba.demo.failure.cucumber;


import java.util.ArrayList;
import java.util.List;

/**
 * @author AUTO_BEAR
 */
public class PoolConsumer {
    ForkjoinTest test = new ForkjoinTest();

    public void execute(String s) {
        System.out.println(System.currentTimeMillis());
        long begin = System.currentTimeMillis();
        List<String> list = new ArrayList<>();
        list.add(s + "1");
        list.add(s + "2");
        list.add(s + "3");
        list.add(s + "4");
        list.add(s + "5");
        list.add(s + "6");
        list.add(s + "7");
        list.add(s + "8");
        list.add(s + "9");
        list.add(s + "10");
        for (int i = 0; i < 10; i++) {
            test.execute(list);
        }
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("大循环花费时间" + (System.currentTimeMillis() - begin));
    }
}


