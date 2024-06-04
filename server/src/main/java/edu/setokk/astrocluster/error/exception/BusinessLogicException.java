package edu.setokk.astrocluster.error.exception;

import org.springframework.http.HttpStatus;

public class BusinessLogicException extends RuntimeException {
    private final HttpStatus statusCode;

    public BusinessLogicException(HttpStatus statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public HttpStatus getStatusCode() {
        return statusCode;
    }
}
