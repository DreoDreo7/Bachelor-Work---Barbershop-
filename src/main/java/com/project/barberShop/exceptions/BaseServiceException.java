package com.project.barberShop.exceptions;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BaseServiceException extends RuntimeException {

    private ErrorCode errorCode;

    public BaseServiceException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
}
