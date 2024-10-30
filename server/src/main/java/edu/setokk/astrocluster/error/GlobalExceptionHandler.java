package edu.setokk.astrocluster.error;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(BusinessLogicException.class)
    public ResponseEntity<ErrorMessage> handleBusinessLogicException(BusinessLogicException e) {
        return new ResponseEntity<>(createErrorsMap(e.getErrorMessages()), e.getHttpStatus());
    }

    /**
     * Catches and handles 'MethodArgumentNotValidException' exceptions (from jakarta constraints)
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
