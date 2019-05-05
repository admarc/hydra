package com.github.admarc;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class CucumberStepDefinitions {

    @LocalServerPort
    protected int port;

    @Autowired
    protected TestRestTemplate restTemplate;

    public static final String url = "http://localhost";

    protected String resourceName = "";

    public ResponseEntity<String> getResponse() {
        return restTemplate.getForEntity(getResourceUrl(), String.class);
    }

    private String getResourceUrl() {
        return url + ":" + this.port + "/" + resourceName;
    }
}