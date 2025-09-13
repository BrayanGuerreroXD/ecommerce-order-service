package com.test.ecommerceorderservice.infrastructure.enums.exceptions;

public enum ExceptionEnum {

    SECURITY_EXCEPTION("S01", "Security Exception"),
    JWT_EXCEPTION("J01", "JWT Exceptions"),
    INTERNAL_EXCEPTION("I01", "Internal Server Exception"),
    REQUEST_EXCEPTION("C01", "Request exception"),
    VALIDATION_EXCEPTION("C03", "Validation Exception"),
    QUERY_EXCEPTION("C02", "Query Exception"),
    NOT_FOUND_EXCEPTION("N01", "NotFound Exception");

    private final String code;
    private final String value;

    ExceptionEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }


}
