package com.alibaba.demo.failure.cucumber;


import java.util.List;

/**
 * @author AUTO_BEAR
 */

public class ForkjoinTest {


    ForkjoinService service = new ForkjoinService();

    public void execute(List<String> list) {
        System.out.println(System.currentTimeMillis());
        long begin = System.currentTimeMillis();
        //隐式并行流花费时间与List呈抛物线关系，List长度越大比串行花费时间越少。
//            service.printPool(list);

        service.printPool(list);

        //问题得到复现，现在尝试使用显示的
        //串行花费的时间与List长度呈现线性关系
//        for(String l:list){
//            service.printit(l);
//        }
        System.out.println("小循环花费时间" + (System.currentTimeMillis() - begin));
    }
}
