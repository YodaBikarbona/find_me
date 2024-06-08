package com.findme.exceptions;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.text.MessageFormat;
import java.time.Instant;
import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String STATUS = "ERROR";

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponse(STATUS, Instant.now(), ex.getMessage(), Collections.emptyList()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(AuthorizationException ex) {
        return new ResponseEntity<>(new ErrorResponse(STATUS, Instant.now(), ex.getMessage(), Collections.emptyList()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
        return new ResponseEntity<>(new ErrorResponse(STATUS, Instant.now(), ex.getMessage(), Collections.emptyList()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleInternalServerErrorException(InternalServerErrorException ex) {
        return new ResponseEntity<>(new ErrorResponse(STATUS, Instant.now(), ex.getMessage(), Collections.emptyList()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        String message = MessageFormat.format("Invalid field: {0}, rejected value: {1}", getErrorField(ex), getRejectedValue(ex));
        return new ResponseEntity<>(new ErrorResponse(STATUS, Instant.now(), message, getErrors(ex)), HttpStatus.BAD_REQUEST);
    }

    private Object getErrorField(MethodArgumentNotValidException ex) {
        return Optional.ofNullable(ex.getFieldError())
                .map(FieldError::getField)
                .orElse("Unknown");
    }

    private Object getRejectedValue(MethodArgumentNotValidException ex) {
        return Optional.ofNullable(ex.getFieldError())
                .map(FieldError::getRejectedValue)
                .orElse("Unknown");
    }

    private List<String> getErrors(MethodArgumentNotValidException ex) {
        return ex.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ErrorResponse(String status, Instant timestamp, String message, List<String> errors) { }

}
