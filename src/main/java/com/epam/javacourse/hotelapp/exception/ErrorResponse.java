package com.epam.javacourse.hotelapp.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.http.HttpStatus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    //    indicating when the error occurred
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private Date timestamp;

    private int code;

    private String status;
    private String message;
    private String stackTrace;
    Object data;
    private List<ValidationError> errors;

    public ErrorResponse() {
        timestamp = new Date();
    }

    public ErrorResponse(
            HttpStatus httpStatus,
            String message
    ) {
        this();
        this.code = httpStatus.value();
        this.status = httpStatus.name();
        this.message = message;
    }

    public ErrorResponse(
            HttpStatus httpStatus,
            String message,
            String stackTrace
    ) {
        this(
                httpStatus,
                message
        );

        this.stackTrace = stackTrace;
    }

    public ErrorResponse(
            HttpStatus httpStatus,
            String message,
            String stackTrace,
            Object data
    ) {
        this(
                httpStatus,
                message,
                stackTrace
        );

        this.data = data;
    }

    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public String getMessage() {
        return message;
    }

    public String getStackTrace() {
        return stackTrace;
    }

    public void setStackTrace(String stackTrace) {
        this.stackTrace = stackTrace;
    }

    public List<ValidationError> getErrors() {
        return errors;
    }

    public void setErrors(List<ValidationError> errors) {
        this.errors = errors;
    }

    private static class ValidationError {
        private final String field;
        private final String message;

        public ValidationError(String field, String message) {
            this.field = field;
            this.message = message;
        }

        public String getField() {
            return field;
        }

        public String getMessage() {
            return message;
        }
    }

    public void addValidationError(String field, String message){
        if(Objects.isNull(errors)){
            errors = new ArrayList<>();
        }
        errors.add(new ValidationError(field, message));
    }
}
