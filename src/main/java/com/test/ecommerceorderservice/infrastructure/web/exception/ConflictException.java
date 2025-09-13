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
public class ConflictException extends RuntimeException {

    private final transient ErrorResponse errorResponse;

    public ConflictException(ExceptionCodeEnum exceptionCodeEnum) {
        this.errorResponse = new ErrorResponse(exceptionCodeEnum);
    }

    public ConflictException(OtherExceptionEnum exceptionCodeEnum) {
        this.errorResponse = new ErrorResponse(exceptionCodeEnum);
    }

    public ConflictException(ExceptionCodeEnum exceptionCodeEnum, Long id) {
        this.errorResponse = new ErrorResponse(exceptionCodeEnum, id);
    }

    public ConflictException(ExceptionCodeEnum exceptionCodeEnum, String keyword) {
        this.errorResponse = new ErrorResponse(exceptionCodeEnum, keyword);
    }
    
}
