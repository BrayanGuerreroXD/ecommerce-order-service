package com.test.ecommerceorderservice.infrastructure.enums.exceptions;

public enum OtherExceptionEnum {

    SIGNATURE_TOKEN_VERIFY(ExceptionEnum.SECURITY_EXCEPTION.getCode(), ExceptionEnum.SECURITY_EXCEPTION.getValue(), "The Token's Signature resulted invalid when verified"),
    TOKEN_EXPIRED(ExceptionEnum.SECURITY_EXCEPTION.getCode(), ExceptionEnum.SECURITY_EXCEPTION.getValue(), "The token has expired"),
    INVALID_TOKEN(ExceptionEnum.SECURITY_EXCEPTION.getCode(), ExceptionEnum.SECURITY_EXCEPTION.getValue(), "Invalid token"),
    TOKEN_IS_NULL(ExceptionEnum.SECURITY_EXCEPTION.getCode(), ExceptionEnum.SECURITY_EXCEPTION.getValue(), "The bearer token is null");

    private final String code;
    private final String value;

    private final String message;

    OtherExceptionEnum(String code, String value, String message) {
        this.code = code;
        this.value = value;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
