package com.desofs.backend.exceptions;

public class RateLimitException extends Exception {
    public RateLimitException(String message) {
        super(message);
    }
}
