package com.github.admarc.controller;

import com.github.admarc.controller.request.LoginRequest;
import com.github.admarc.controller.response.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.endpoint.TokenEndpoint;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;

@RestController
public class AuthenticationController {
    ClientDetailsService clientDetailsService;
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

    AuthenticationController(ClientDetailsService clientDetailsService, TokenEndpoint tokenEndpoint) {
        this.clientDetailsService = clientDetailsService;
        this.tokenEndpoint = tokenEndpoint;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest)
            throws HttpRequestMethodNotSupportedException {
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("grant_type", grantType);
        parameters.put("password", loginRequest.getPassword());
        parameters.put("scope", scopeRead + " " + scopeWrite);
        parameters.put("username", loginRequest.getIdentifier());

        ClientDetails clientDetails = clientDetailsService.loadClientByClientId(clientId);

        org.springframework.security.core.userdetails.User userPrincipal =
                new org.springframework.security.core.userdetails.User(
                        clientId,
                        clientSecret,
                        clientDetails.getAuthorities()
                );

        UsernamePasswordAuthenticationToken principal = new UsernamePasswordAuthenticationToken(
                userPrincipal,
                clientSecret,
                clientDetails.getAuthorities()
        );

        try{
            ResponseEntity<OAuth2AccessToken> accessToken = tokenEndpoint.postAccessToken(principal, parameters);
            return ResponseEntity.ok(new AuthenticationResponse(accessToken.getBody().toString()));
        } catch (InvalidGrantException exception) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }
}
