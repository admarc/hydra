package com.github.admarc;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = { "progress"},
        features = { "src/test/resources/features" })
public class TestRunner {
}