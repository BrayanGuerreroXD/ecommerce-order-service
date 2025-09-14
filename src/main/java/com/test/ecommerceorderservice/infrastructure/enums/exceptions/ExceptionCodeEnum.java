package com.test.ecommerceorderservice.infrastructure.enums.exceptions;

import lombok.Getter;

@Getter
public enum ExceptionCodeEnum {

    // -------------- Generic Exceptions --------------
    N01GNR01("The Id was not found", ExceptionEnum.REQUEST_EXCEPTION.getValue()),
    N01GNR02("The Name was not found", ExceptionEnum.NOT_FOUND_EXCEPTION.getValue()),
    N01GNR03("The Code was not found(batchCode o sku code)", ExceptionEnum.NOT_FOUND_EXCEPTION.getValue()),
    N01GNR04("The Email was not found", ExceptionEnum.NOT_FOUND_EXCEPTION.getValue()),

    N01GNRC01("The object was not found", ExceptionEnum.NOT_FOUND_EXCEPTION.getValue()),
    I01GNRC02("An error occurred while communicating with the external service", ExceptionEnum.INTERNAL_EXCEPTION.getValue()),

    C01GNRC01("The object already exist", ExceptionEnum.REQUEST_EXCEPTION.getValue()),
    
    // -------------- Forbidden Exception Generic --------------
    S01FORB01("The user does not have permissions to access this resource", ExceptionEnum.SECURITY_EXCEPTION.getValue()),
    S01FORB02("The user does not contain any module permissions", ExceptionEnum.SECURITY_EXCEPTION.getValue()),
    S01FORB03("The user does not contain the module permissions", ExceptionEnum.SECURITY_EXCEPTION.getValue()),

    // -------------- Unauthorized Exception Generic --------------
    S01UNAU01("The user is invalid", ExceptionEnum.SECURITY_EXCEPTION.getValue()),
    S01UNAU02("No session active, log in again", ExceptionEnum.SECURITY_EXCEPTION.getValue()),
    S01UNAU03("The token is not the token of the active session", ExceptionEnum.SECURITY_EXCEPTION.getValue()),
    S01UNAU04("The token is null", ExceptionEnum.SECURITY_EXCEPTION.getValue()),
    S01UNAU05("The token is empty", ExceptionEnum.SECURITY_EXCEPTION.getValue()),
    S01UNAU06("The token is not valid", ExceptionEnum.SECURITY_EXCEPTION.getValue()),
    S01UNAU07("The token has expired", ExceptionEnum.SECURITY_EXCEPTION.getValue()),
    S01UNAU08("The user already has an active session", ExceptionEnum.SECURITY_EXCEPTION.getValue()),

    // -------------- Login Exceptions --------------
    S01LOGN01("Invalid password", ExceptionEnum.SECURITY_EXCEPTION.getValue()),

    // -------------- Users Exceptions --------------
    C01USR01("The user was not found", ExceptionEnum.NOT_FOUND_EXCEPTION.getValue()),

    // --------------Roles Exceptions --------------
    C01ROL01("The role was not found", ExceptionEnum.NOT_FOUND_EXCEPTION.getValue()),

    // -------------- Orders Exceptions --------------

    // -------------- Products Exceptions --------------
    C01PRD01("The product was not found", ExceptionEnum.NOT_FOUND_EXCEPTION.getValue()),
    C01PRD02("The product SKU already exists", ExceptionEnum.REQUEST_EXCEPTION.getValue()),

    // -------------- Inventory Exceptions --------------
    C01INV01("The inventory was not found", ExceptionEnum.NOT_FOUND_EXCEPTION.getValue()),
    C01INV02("The inventory for the product already exists", ExceptionEnum.REQUEST_EXCEPTION.getValue()),
    C01INV03("Too many concurrent updates to the inventory; optimistic locking failed after multiple retries.", ExceptionEnum.REQUEST_EXCEPTION.getValue()),

    ;



    private final String code;

    private final String message;
    private final String description;

    ExceptionCodeEnum(String message, String description) {
        this.code = this.name();
        this.message = message;
        this.description = description;
    }


}
