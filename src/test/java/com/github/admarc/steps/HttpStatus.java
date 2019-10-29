package com.github.admarc.steps;

public enum HttpStatus {
    SUCCESSFUL(200),
    INVALID(400),
    UNAUTHORIZED(401),
    FORBIDDEN(403);

    private final int value;

    HttpStatus(int value) {
        this.value = value;
    }

    public int getStatusCode() {
        return this.value;
    }
}
