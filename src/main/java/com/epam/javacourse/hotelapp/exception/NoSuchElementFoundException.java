package com.epam.javacourse.hotelapp.exception;

public class NoSuchElementFoundException extends RuntimeException{

    public NoSuchElementFoundException(String message){
        super(message);
    }

    public NoSuchElementFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public NoSuchElementFoundException(final Throwable cause) {
        super(cause);
    }
}
