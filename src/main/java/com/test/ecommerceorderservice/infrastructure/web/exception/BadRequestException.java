package com.test.ecommerceorderservice.infrastructure.web.exception;

import com.test.ecommerceorderservice.infrastructure.enums.exceptions.ExceptionCodeEnum;
import com.test.ecommerceorderservice.infrastructure.web.dto.ErrorResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class BadRequestException extends RuntimeException {

    private final transient ErrorResponse errorResponse;

    public BadRequestException(ExceptionCodeEnum exceptionCodeEnum) {
        this.errorResponse = new ErrorResponse(exceptionCodeEnum);
    }

    public BadRequestException(ExceptionCodeEnum exceptionCodeEnum, Long id) {
        this.errorResponse = new ErrorResponse(exceptionCodeEnum, id);
    }

    public BadRequestException(ExceptionCodeEnum exceptionCodeEnum, String keyword) {
        this.errorResponse = new ErrorResponse(exceptionCodeEnum, keyword);
    }

}