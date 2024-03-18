package com.project.barberShop.exceptions;

public class ConflictException extends BaseServiceException {
    public ConflictException(String message) {
        super(ErrorCode.CONFLICT, message);
    }
}
