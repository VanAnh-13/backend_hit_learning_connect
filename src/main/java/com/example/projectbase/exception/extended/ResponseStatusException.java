package com.example.projectbase.exception.extended;

import org.springframework.http.HttpStatus;

public class ResponseStatusException extends RuntimeException {
  private String message;

  private HttpStatus status;

  private String[] params;

  public ResponseStatusException(String message) {
    super(message);
    this.status = HttpStatus.NOT_FOUND;
    this.message = message;
  }

  public ResponseStatusException(HttpStatus status, String message) {
    super(message);
    this.status = status;
    this.message = message;
  }

  public ResponseStatusException(String message, String[] params) {
    super(message);
    this.status = HttpStatus.NOT_FOUND;
    this.message = message;
    this.params = params;
  }

  public ResponseStatusException(HttpStatus status, String message, String[] params) {
    super(message);
    this.status = status;
    this.message = message;
    this.params = params;
  }

}
