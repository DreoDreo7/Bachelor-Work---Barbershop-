package com.project.barberShop.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(BaseServiceException ex) {
        ErrorResponse errorResponse = new ErrorResponse(ex.getMessage(), ex.getErrorCode().toString());
        HttpStatus status = determineStatus(ex.getErrorCode());
        return new ResponseEntity<>(errorResponse, status);
    }

    private HttpStatus determineStatus(ErrorCode errorCode) {
        return switch (errorCode) {
            case RESOURCE_NOT_FOUND -> HttpStatus.OK;
            case VALIDATION_ERROR -> HttpStatus.BAD_REQUEST;
            case CONFLICT -> HttpStatus.CONFLICT;
        };
    }
}

