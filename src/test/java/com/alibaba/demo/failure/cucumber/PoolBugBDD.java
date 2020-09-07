package com.alibaba.demo.failure.cucumber;


import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;

import java.io.IOException;

/**
 * @author Zxt
 */
public class PoolBugBDD {
    public String newday = "";
    PoolConsumer consumer = new PoolConsumer();

    @Given("^给定一个代号:(.*)$")
    public void givenFilename(String Filename) throws IOException {
        TTLContext.put("givenFilename", Filename);
        newday = Filename + "newday";
    }

    @When("^开始进行Debug$")
    public void markTag() {
        consumer.execute(newday);
        TTLContext.clear();
        try {
            Thread.sleep(1000 * 5);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
