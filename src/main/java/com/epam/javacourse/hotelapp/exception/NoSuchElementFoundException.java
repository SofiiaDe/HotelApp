package com.epam.javacourse.hotelapp.exception;

/**
 * An exception which provides information on an error when a requested element is not found.
 */
public class NoSuchElementFoundException extends RuntimeException {

    public NoSuchElementFoundException(String message) {
        super(message);
    }

    public NoSuchElementFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NoSuchElementFoundException(final Throwable cause) {
        super(cause);
    }
}
