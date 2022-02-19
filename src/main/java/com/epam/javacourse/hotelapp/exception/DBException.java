package com.epam.javacourse.hotelapp.exception;

/**
 *  * An exception which provides information on a database error.
 */
public class DBException extends AppException {

    public DBException() {
        super();
    }

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

    public DBException(String message) {
        super(message);
    }
}
