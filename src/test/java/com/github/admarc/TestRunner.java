package com.github.admarc;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "progress"},
        glue = {"com.github.admarc.steps","com.github.admarc.config"},
        features = { "src/test/resources/features" })
public class TestRunner {
}
