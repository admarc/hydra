package com.github.admarc.steps;

import cucumber.api.java.en.Given;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;

import java.util.HashMap;

public class AuthenticationStepDefinitions {
    ClientDetailsService clientDetailsService;
    RequestStepDefinitions requestStepDefinitions;
    TokenEndpoint tokenEndpoint;

    @Value("${security.jwt.client-id}")
    private String clientId;

    @Value("${security.jwt.client-secret}")
    private String clientSecret;

    @Value("${security.jwt.grant-type}")
    private String grantType;

    @Value("${security.jwt.scope-read}")
    private String scopeRead;

    @Value("${security.jwt.scope-write}")
    private String scopeWrite;

    AuthenticationStepDefinitions(
            ClientDetailsService clientDetailsService,
            RequestStepDefinitions requestStepDefinitions,
            TokenEndpoint tokenEndpoint
    ) {
        this.clientDetailsService = clientDetailsService;
        this.requestStepDefinitions = requestStepDefinitions;
        this.tokenEndpoint = tokenEndpoint;
    }

    @Given("I'm authenticated as {string} with password {string}")
    public void i_m_authenticated_as(String username, String password) throws HttpRequestMethodNotSupportedException {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", grantType);
        parameters.put("password", password);
        parameters.put("scope", scopeRead + " " + scopeWrite);
        parameters.put("username", username);

        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

        User userPrincipal = new User(
                clientId,
                clientSecret,
                clientDetails.getAuthorities()
        );

        UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(
                userPrincipal,
                clientSecret,
                clientDetails.getAuthorities()
        );

        ResponseEntity<OAuth2AccessToken> accessToken = tokenEndpoint.postAccessToken(principal, parameters);

        requestStepDefinitions.setHeader("Authorization", "Bearer " + accessToken.getBody());
    }
}