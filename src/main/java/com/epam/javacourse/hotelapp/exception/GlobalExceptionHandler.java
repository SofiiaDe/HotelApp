package com.epam.javacourse.hotelapp.exception;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    public GlobalExceptionHandler() {
        super();
    }

    // 400
    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleBadRequest(final DataIntegrityViolationException exception, final WebRequest request) {
        final String bodyOfResponse = "Something went wrong. Error 400.";
        logger.error("DataIntegrityViolationException has occurred", exception);
        return handleExceptionInternal(exception, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(final HttpMessageNotReadableException exception, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        final String bodyOfResponse = "Something went wrong. Error 400.";
        logger.error("HttpMessageNotReadableException has occurred", exception);
        return handleExceptionInternal(exception, bodyOfResponse, headers, HttpStatus.BAD_REQUEST, request);
    }

    @Override
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            final MethodArgumentNotValidException exception, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {

        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.UNPROCESSABLE_ENTITY.value(),
                "Validation error. Check 'errors' field for details."
        );

        for (FieldError fieldError : exception.getBindingResult().getFieldErrors()) {
            errorResponse.addValidationError(fieldError.getField(),
                    fieldError.getDefaultMessage());
        }
        logger.error("MethodArgumentNotValidException has occurred", exception);
        return ResponseEntity.unprocessableEntity().body(errorResponse);
    }


    // 404
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NoSuchElementFoundException.class)
    protected ResponseEntity<Object> handleNoSuchElementFoundException(NoSuchElementFoundException notFoundException, final WebRequest request) {
        logger.error("Failed to find the requested element", notFoundException);
        return buildErrorResponse(notFoundException, HttpStatus.NOT_FOUND, request);
    }


    // 409
    @ExceptionHandler({InvalidDataAccessApiUsageException.class, DataAccessException.class})
    protected ResponseEntity<Object> handleConflict(final RuntimeException exception, final WebRequest request) {
        logger.error("409 Status Code exception has occurred", exception);

        final String bodyOfResponse = "Something went wrong. Error 409.";
        return handleExceptionInternal(exception, bodyOfResponse, new HttpHeaders(), HttpStatus.CONFLICT, request);
    }


    // 500
    @ExceptionHandler({NullPointerException.class, IllegalArgumentException.class, IllegalStateException.class})
    public ResponseEntity<Object> handleInternal(final RuntimeException exception, final WebRequest request) {
        logger.error("500 Status Code exception has occurred", exception);
        final String bodyOfResponse = "Something went wrong. Error 500.";
        return handleExceptionInternal(exception, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }

    @ExceptionHandler({AccessDeniedException.class})
    public ResponseEntity<Object> handleAccessDeniedException(Exception exception, WebRequest request) {
        logger.error("AccessDeniedException has occurred", exception);
        return new ResponseEntity<>(
                "Access denied", new HttpHeaders(), HttpStatus.FORBIDDEN);
    }


    @Value("${hotel.trace:false}")
    private boolean printStackTrace;


    @ExceptionHandler(AppException.class)
    public ResponseEntity<Object> handleAppExceptions(Exception exception, WebRequest request) {
        // casting the generic Exception e to CustomErrorException
        AppException appException = (AppException) exception;

        logger.error("AppException has occurred", appException);
        return buildErrorResponse(appException,
                appException.getMessage(),
                appException.getStatus(),
                request);
    }

    /**
     * a fallback method to cover all remaining cases
     * @return ResponseEntity<Object>
     */
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<Object> handleAllUncaughtException(
            Exception exception,
            WebRequest request
    ) {
        logger.error("Unknown error occurred", exception);
        return buildErrorResponse(
                exception,
                "Unknown error occurred",
                HttpStatus.INTERNAL_SERVER_ERROR,
                request
        );
    }


    @Override
    public ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            Object body,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request) {

        return buildErrorResponse(ex, status, request);
    }

    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            HttpStatus httpStatus,
            WebRequest request
    ) {
        return buildErrorResponse(
                exception,
                exception.getMessage(),
                httpStatus,
                request);
    }

    private ResponseEntity<Object> buildErrorResponse(
            Exception exception,
            String message,
            HttpStatus httpStatus,
            WebRequest request
    ) {
        ErrorResponse errorResponse = new ErrorResponse(
                httpStatus.value(),
                message
        );

        if(printStackTrace ){//&& isTraceOn(request)
            errorResponse.setStackTrace(ExceptionUtils.getStackTrace(exception));
        }
        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

//    private boolean isTraceOn(WebRequest request) {
//        String [] value = request.getParameterValues(TRACE);
//        return Objects.nonNull(value)
//                && value.length > 0
//                && value[0].contentEquals("true");
//    }

}
