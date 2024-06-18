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

import java.time.Instant;
import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final String STATUS = "ERROR";
    private static final String REQUEST_ID_HEADER = "X-Request-ID";
    private static final String UNKNOWN = "Unknown";

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponse(getRequestId(request), STATUS, Instant.now(), ex.getMessage(), Collections.emptyList()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handlerBadRequestException(BadRequestException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponse(getRequestId(request), STATUS, Instant.now(), ex.getMessage(), Collections.emptyList()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthorizationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleAuthorizationException(AuthorizationException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponse(getRequestId(request), STATUS, Instant.now(), ex.getMessage(), Collections.emptyList()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponse(getRequestId(request), STATUS, Instant.now(), ex.getMessage(), Collections.emptyList()), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleInternalServerErrorException(InternalServerErrorException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponse(getRequestId(request), STATUS, Instant.now(), ex.getMessage(), Collections.emptyList()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoPermissionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> handleNoPermissionException(NoPermissionException ex, HttpServletRequest request) {
        return new ResponseEntity<>(new ErrorResponse(getRequestId(request), STATUS, Instant.now(), ex.getMessage(), Collections.emptyList()), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Object errorField = getErrorField(ex);
        Object rejectedValue = getRejectedValue(ex);
        String message = ex.getAllErrors().getFirst().getDefaultMessage();;
        return new ResponseEntity<>(new ErrorResponse(getRequestId(request), STATUS, Instant.now(), message, getErrors(ex)), HttpStatus.BAD_REQUEST);
    }

    private Object getErrorField(MethodArgumentNotValidException ex) {
        return Optional.ofNullable(ex.getFieldError())
                .map(FieldError::getField)
                .orElse(UNKNOWN);
    }

    private Object getRejectedValue(MethodArgumentNotValidException ex) {
        return Optional.ofNullable(ex.getFieldError())
                .map(FieldError::getRejectedValue)
                .filter(rejectedValue -> !rejectedValue.toString().startsWith("org.springframework"))
                .orElse(UNKNOWN);
    }

    private List<String> getErrors(MethodArgumentNotValidException ex) {
        return ex.getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .toList();
    }

    private String getRequestId(HttpServletRequest request) {
        return (String) request.getAttribute(REQUEST_ID_HEADER);
    }

    @JsonInclude(JsonInclude.Include.NON_NULL)
    public record ErrorResponse(String requestId, String status, Instant timestamp, String message, List<String> errors) { }

}
