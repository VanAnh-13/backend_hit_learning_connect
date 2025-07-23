package com.example.projectbase.exception.extended;

import org.springframework.http.HttpStatus;

public class AccessDeniedException extends RuntimeException {
    private String message;

    private HttpStatus status;

    private String[] params;

    public AccessDeniedException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.message = message;
    }

    public AccessDeniedException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public AccessDeniedException(String message, String[] params) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.message = message;
        this.params = params;
    }

    public AccessDeniedException(HttpStatus status, String message, String[] params) {
        super(message);
        this.status = status;
        this.message = message;
        this.params = params;
    }
}
