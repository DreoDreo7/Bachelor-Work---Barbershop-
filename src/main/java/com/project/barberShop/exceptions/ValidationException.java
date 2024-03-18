package com.project.barberShop.exceptions;

public class ValidationException extends BaseServiceException {
    public ValidationException(String message) {
        super(ErrorCode.VALIDATION_ERROR, message);
    }
}

