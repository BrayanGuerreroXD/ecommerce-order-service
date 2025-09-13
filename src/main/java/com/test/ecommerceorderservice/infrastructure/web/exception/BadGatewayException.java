package com.test.ecommerceorderservice.infrastructure.web.exception;

import com.test.ecommerceorderservice.infrastructure.enums.exceptions.ExceptionCodeEnum;
import com.test.ecommerceorderservice.infrastructure.web.dto.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BadGatewayException extends RuntimeException {

    private final transient ErrorResponse errorResponse;

    public BadGatewayException(ExceptionCodeEnum exceptionCodeEnum) {
        this.errorResponse = new ErrorResponse(exceptionCodeEnum);
    }

}
