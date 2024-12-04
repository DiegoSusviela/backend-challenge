package com.directa24.directa24_back_end_dev_challenge.exception;

public class MovieApiException extends RuntimeException {
    public MovieApiException(String message) {
        super(message);
    }

    public MovieApiException(String message, Throwable cause) {
        super(message, cause);
    }
}