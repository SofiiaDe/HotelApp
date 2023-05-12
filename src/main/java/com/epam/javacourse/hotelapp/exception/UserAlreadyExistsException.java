package com.epam.javacourse.hotelapp.exception;

/**
 * Exception thrown by system in case someone tries to register with already
 * existing email in the system.
 */

public class UserAlreadyExistsException extends Exception {

    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
