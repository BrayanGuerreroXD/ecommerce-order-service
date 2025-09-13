package com.test.ecommerceorderservice.infrastructure.web.dto;

import com.test.ecommerceorderservice.infrastructure.enums.exceptions.ExceptionCodeEnum;
import com.test.ecommerceorderservice.infrastructure.enums.exceptions.OtherExceptionEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private ErrorDetailResponse errors;

    public ErrorResponse(ExceptionCodeEnum exceptionCodeEnum) {
        this.errors = new ErrorDetailResponse(
                exceptionCodeEnum.getCode(),
                exceptionCodeEnum.getDescription(),
                Collections.singletonList(exceptionCodeEnum.getMessage())
        );
    }

    public ErrorResponse(ExceptionCodeEnum exceptionCodeEnum, Long id) {
        this.errors = new ErrorDetailIdResponse(
                exceptionCodeEnum.getCode(),
                exceptionCodeEnum.getDescription(),
                Collections.singletonList(exceptionCodeEnum.getMessage()),
                id
        );
    }

    public ErrorResponse(ExceptionCodeEnum exceptionCodeEnum, String keyword) {
        this.errors = new ErrorDetailKeywordResponse(exceptionCodeEnum.getCode(),
                exceptionCodeEnum.getDescription(),
                Collections.singletonList(exceptionCodeEnum.getMessage()), keyword);
    }

    public ErrorResponse(OtherExceptionEnum otherExceptionEnum) {
        this.errors = new ErrorDetailResponse(
                otherExceptionEnum.getCode(),
                otherExceptionEnum.getValue(),
                Collections.singletonList(otherExceptionEnum.getMessage())
        );
    }
}

