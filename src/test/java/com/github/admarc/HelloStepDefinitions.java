package com.github.admarc;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class HelloStepDefinitions extends CucumberStepDefinitions {

    @Given("I want to try the application")
    public void i_want_to_try_the_application() {
        this.resourceName = "hello";
    }

    @Then("I want to see hello message")
    public void i_want_to_see_hello_message() {
        ResponseEntity<String> response = getResponse();
        assert response.getStatusCode() == HttpStatus.OK;
        assert response.getBody().equals("Greetings from Spring Boot!") == true;
    }
}