package com.alibaba.demo.failure.cucumber;


import io.cucumber.core.cli.Main;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;


/**
 * @author AUTO_BEAR
 */
@RunWith(Cucumber.class)
@CucumberOptions(features = "classpath:联测.feature", plugin = {"junit:target/cucumber-report.xml",
    "json:target/cucumber-report.json", "pretty", "html:target/site/cucumber"})
public class PoolBugTest {
    //    public static final String features_dir = System.getProperty("user.dir")
//        +"src/test/resources/联测.feature";
    public static void main(String[] args) {
        Main.run(args, Thread.currentThread().getContextClassLoader());
    }
}

