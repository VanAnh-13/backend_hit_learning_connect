package com.example.projectbase.exception.extended;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
public class FileStorageException extends RuntimeException {

    private String message;

    private HttpStatus status;

    private String[] params;

    public FileStorageException(String message) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.message = message;
    }

    public FileStorageException(HttpStatus status, String message) {
        super(message);
        this.status = status;
        this.message = message;
    }

    public FileStorageException(String message, String[] params) {
        super(message);
        this.status = HttpStatus.BAD_REQUEST;
        this.message = message;
        this.params = params;
    }

    public FileStorageException(HttpStatus status, String message, String[] params) {
        super(message);
        this.status = status;
        this.message = message;
        this.params = params;
    }

}