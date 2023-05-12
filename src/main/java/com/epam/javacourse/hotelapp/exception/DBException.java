package com.epam.javacourse.hotelapp.exception;

/**
 * * An exception which provides information on a database error.
 */
public class DBException extends AppException {

    public DBException(String message, Throwable cause) {
        super(message, cause);
    }

}
