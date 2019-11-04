package com.github.admarc.controller.request;

import javax.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank
    private String identifier;

    @NotBlank
    private String password;

    public String getIdentifier() {
        return identifier;
    }

    public String getPassword() {
        return password;
    }
}
