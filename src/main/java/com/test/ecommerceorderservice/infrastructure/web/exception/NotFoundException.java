package com.test.ecommerceorderservice.infrastructure.web.exception;

import com.test.ecommerceorderservice.infrastructure.enums.exceptions.ExceptionCodeEnum;
import com.test.ecommerceorderservice.infrastructure.web.dto.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class NotFoundException extends RuntimeException {
    private final transient ErrorResponse errorResponse;

    public NotFoundException(ExceptionCodeEnum exceptionCodeEnum) {
        this.errorResponse = new ErrorResponse(exceptionCodeEnum);
    }
}
