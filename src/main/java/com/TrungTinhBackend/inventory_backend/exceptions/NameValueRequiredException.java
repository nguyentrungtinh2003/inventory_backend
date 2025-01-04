package com.TrungTinhBackend.inventory_backend.exceptions;

public class NameValueRequiredException extends RuntimeException {
    public NameValueRequiredException(String message) {
        super(message);
    }
}
