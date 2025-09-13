package com.test.ecommerceorderservice.infrastructure.web.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ErrorDetailIdResponse extends ErrorDetailResponse {
    private Long id;

    public ErrorDetailIdResponse(String code, String description, List<String> fields, Long id) {
        super(code, description, fields);
        this.id = id;
    }
}
