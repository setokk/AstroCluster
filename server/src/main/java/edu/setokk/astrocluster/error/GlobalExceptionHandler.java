package edu.setokk.astrocluster.error;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * This class is used to handle all business logic exceptions thrown by the application at runtime. 
 * Its purpose is to eliminate 'try catches' and not complicate error handling on the controller side.
 * When an exception is thrown it goes through the handler. 
 * From there, we prepare the error message(s) and send them to the client.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Catches and handles {@link BusinessLogicException} exceptions
     * @param e the {@link BusinessLogicException} exception that was thrown
     */
    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<Map<String, List<String>>> handleBusinessLogicException(BusinessLogicException e) {
        return new ResponseEntity<>(createErrorsMap(e.getErrorMessages()), e.getHttpStatus());
    }

    /**
     * Catches and handles {@link MailException} exceptions
     * @param e the {@link MailException} exception that was thrown
     */
    @ExceptionHandler(MailException.class)
    public ResponseEntity<Map<String, List<String>>> handleMailException(MailException e) {
        return new ResponseEntity<>(createErrorsMap(Collections.singletonList(e.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Catches and handles {@link MethodArgumentNotValidException} exceptions (from jakarta constraints)
     * @param e the {@link MethodArgumentNotValidException} exception that was thrown
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, List<String>>> handleValidation(MethodArgumentNotValidException e) {
        List<String> errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();

        return new ResponseEntity<>(createErrorsMap(errors), HttpHeaders.EMPTY, HttpStatus.BAD_REQUEST);
    }

    /**
     * Catches and handles {@link ConstraintViolationException} exceptions (from jakarta constraints)
     * @param e the {@link ConstraintViolationException} exception that was thrown
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Map<String, List<String>>> handleConstraintViolation(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        List<String> errors = violations.stream()
                .map(ConstraintViolation::getMessage)
                .toList();

        return new ResponseEntity<>(createErrorsMap(errors), HttpHeaders.EMPTY, HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> createErrorsMap(List<String> errors) {
        return Collections.singletonMap("errors", errors);
    }
}
