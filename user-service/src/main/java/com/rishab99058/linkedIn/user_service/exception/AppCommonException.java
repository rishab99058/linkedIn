package com.rishab99058.linkedIn.user_service.exception;

public class AppCommonException extends RuntimeException {
    String message;
    public AppCommonException(String message) {
        super(message);
    }
}
