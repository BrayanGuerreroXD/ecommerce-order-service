package com.test.ecommerceorderservice.infrastructure.web.exception;

import com.test.ecommerceorderservice.infrastructure.enums.exceptions.ExceptionCodeEnum;
import com.test.ecommerceorderservice.infrastructure.enums.exceptions.OtherExceptionEnum;
import com.test.ecommerceorderservice.infrastructure.web.dto.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class ForbiddenException extends RuntimeException {
    private final transient ErrorResponse errorResponse;

    public ForbiddenException(ExceptionCodeEnum exceptionCodeEnum) {
        this.errorResponse = new ErrorResponse(exceptionCodeEnum);
    }

    public ForbiddenException(OtherExceptionEnum otherExceptionEnum) {
        this.errorResponse = new ErrorResponse(otherExceptionEnum);
    }
}
