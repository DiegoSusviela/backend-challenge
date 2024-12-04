package com.directa24.directa24_back_end_dev_challenge.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandler.class);

    /**
     * Handles MovieApiException, which occurs when there is an error related to the Movie API. Logs the error and
     * returns a BAD_REQUEST response with the exception message.
     *
     * @param ex the MovieApiException containing the error message
     * @return a ResponseEntity with HTTP status BAD_REQUEST and the exception message in the body
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(MovieApiException.class)
    public ResponseEntity<String> handleMovieApiException(MovieApiException ex) {
        logger.error("Movie API exception occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(ex.getMessage());
    }

    /**
     * Handles MethodArgumentTypeMismatchException, which occurs when a request parameter cannot be converted to the
     * expected type.
     *
     * @param ex the exception
     * @return a ResponseEntity with an error message
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex) {
        logger.error("MethodArgumentTypeMismatchException: Invalid value for parameter '{}'. Error: {}", ex.getName(),
                ex.getMessage());

        String parameterName = ex.getName();
        String errorMessage = String.format("Invalid value for parameter '%s'. Please provide a valid value.",
                parameterName);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles MissingServletRequestParameterException, which occurs when a required request parameter is missing from
     * the request.
     *
     * @param ex the exception
     * @return a ResponseEntity with an error message
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleMissingParameter(MissingServletRequestParameterException ex) {
        logger.error("MissingServletRequestParameterException: The parameter '{}' is missing.", ex.getParameterName());

        String parameterName = ex.getParameterName();
        String errorMessage = String.format("The parameter '%s' is required and is missing.", parameterName);
        return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles any other general exceptions in the application.
     *
     * @param ex the exception
     * @return a generic error message
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGenericException(Exception ex) {
        logger.error("Unexpected exception occurred: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }
}
