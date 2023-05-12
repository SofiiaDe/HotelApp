package com.epam.javacourse.hotelapp.exception;

import org.springframework.http.HttpStatus;

/**
 * An exception which provides information on an application error.
 */
public class AppException extends Exception {

    private HttpStatus status = null;

    private Object data = null;

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public AppException(
            HttpStatus status,
            String message
    ) {
        this(message);
        this.status = status;
    }

    public AppException(
            HttpStatus status,
            String message,
            Object data
    ) {
        this(
                status,
                message
        );
        this.data = data;
    }

    public AppException() {
        super();
    }

    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(String message) {
        super(message);
    }

    public AppException(Exception exception) {
        super(exception);
    }

}
