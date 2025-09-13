package com.test.ecommerceorderservice.infrastructure.web.exception;

import com.test.ecommerceorderservice.infrastructure.enums.exceptions.ExceptionEnum;
import com.test.ecommerceorderservice.infrastructure.web.dto.ErrorDetailResponse;
import com.test.ecommerceorderservice.infrastructure.web.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;

import java.util.Collections;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ConflictException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
        this.handlerExceptionLog(ex);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getErrorResponse());
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException ex) {
        this.handlerExceptionLog(ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getErrorResponse());
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<ErrorResponse> handleBadRequestException(NotFoundException ex) {
        this.handlerExceptionLog(ex);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getErrorResponse());
    }

    @ExceptionHandler(BadGatewayException.class)
    @ResponseStatus(HttpStatus.BAD_GATEWAY)
    public ResponseEntity<ErrorResponse> handleBadGatewayException(BadGatewayException ex) {
        this.handlerExceptionLog(ex);
        return ResponseEntity.status(HttpStatus.BAD_GATEWAY).body(ex.getErrorResponse());
    }

    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<ErrorResponse> handleBadGatewayException(UnauthorizedException ex) {
        this.handlerExceptionLog(ex);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getErrorResponse());
    }

    @ExceptionHandler(ForbiddenException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ResponseEntity<ErrorResponse> handleBadGatewayException(ForbiddenException ex) {
        this.handlerExceptionLog(ex);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ex.getErrorResponse());
    }

    @ExceptionHandler(InternalServerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleBadGatewayException(InternalServerException ex) {
        this.handlerExceptionLog(ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getErrorResponse());
    }

    // MethodArgumentNotValidException (SpringBoot Validation)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleException(MethodArgumentNotValidException ex) {
        ErrorResponse errorResponse = new ErrorResponse();

        this.handlerExceptionLog(ex);

        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.setErrors(new ErrorDetailResponse(ExceptionEnum.VALIDATION_EXCEPTION.getCode(), ExceptionEnum.VALIDATION_EXCEPTION.getValue(),
                    Collections.singletonList(fieldError.getDefaultMessage())));
        }

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // org.springframework.validation.BindException
    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleException(BindException ex) {
        this.handlerExceptionLog(ex);

        ErrorResponse errorResponse = new ErrorResponse();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errorResponse.setErrors(new ErrorDetailResponse(fieldError.getCode(), fieldError.getDefaultMessage(),
                    Collections.singletonList(fieldError.getField())));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // input json errro
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleException(HttpMessageNotReadableException ex) {
        this.handlerExceptionLog(ex);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(new ErrorDetailResponse(ExceptionEnum.VALIDATION_EXCEPTION.getCode(), ExceptionEnum.VALIDATION_EXCEPTION.getValue(),
                Collections.singletonList(ex.getMessage())));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // max upload size exception
    @ExceptionHandler(MaxUploadSizeExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleException(MaxUploadSizeExceededException ex) {
        this.handlerExceptionLog(ex);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(new ErrorDetailResponse(ExceptionEnum.VALIDATION_EXCEPTION.getCode(), ExceptionEnum.VALIDATION_EXCEPTION.getValue(),
                Collections.singletonList(ex.getMessage())));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // multipart exception
    @ExceptionHandler(MultipartException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleException(MultipartException ex) {
        this.handlerExceptionLog(ex);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(new ErrorDetailResponse(ExceptionEnum.VALIDATION_EXCEPTION.getCode(), ExceptionEnum.VALIDATION_EXCEPTION.getValue(),
                Collections.singletonList(ex.getMessage())));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // IllegalArgument exception
    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleException(IllegalArgumentException ex) {
        this.handlerExceptionLog(ex);
                
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(new ErrorDetailResponse(ExceptionEnum.VALIDATION_EXCEPTION.getCode(), ExceptionEnum.VALIDATION_EXCEPTION.getValue(),
                Collections.singletonList(ex.getMessage())));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    // IllegalArgument exception
    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<ErrorResponse> handleException(IllegalStateException ex) {
        this.handlerExceptionLog(ex);

        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(new ErrorDetailResponse(ExceptionEnum.VALIDATION_EXCEPTION.getCode(), ExceptionEnum.VALIDATION_EXCEPTION.getValue(),
                Collections.singletonList(ex.getMessage())));
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
    }

    // Array Index Out Of Bound exception
    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ErrorResponse> handleException(ArrayIndexOutOfBoundsException ex) {
        this.handlerExceptionLog(ex);
                
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrors(new ErrorDetailResponse(ExceptionEnum.VALIDATION_EXCEPTION.getCode(), ExceptionEnum.VALIDATION_EXCEPTION.getValue(),
                Collections.singletonList(ex.getMessage())));
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
    }

    private void handlerExceptionLog(Exception ex) {
        log.error("Exception occurred: {}, Cause: {}", ex.getClass().getName(),
                ex.getCause() != null ? ex.getCause() : "No cause", ex);
    }
}
