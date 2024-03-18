package com.project.barberShop.exceptions;

public class ResourceNotFoundException extends BaseServiceException {
    public ResourceNotFoundException(String message) {
        super(ErrorCode.RESOURCE_NOT_FOUND, message);
    }
}
