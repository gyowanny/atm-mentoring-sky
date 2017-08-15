package com.atm;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features",
        glue ={"com.thequizz"},
        format = {"pretty", "html:target/cucumber", "json:target/cucumber.json"}
)
public class RunCukesIT {
}
