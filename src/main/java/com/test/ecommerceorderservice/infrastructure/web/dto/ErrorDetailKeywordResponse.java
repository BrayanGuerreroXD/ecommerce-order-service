package com.test.ecommerceorderservice.infrastructure.web.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
public class ErrorDetailKeywordResponse extends ErrorDetailResponse {
    private String keyword;

    public ErrorDetailKeywordResponse(String code, String description, List<String> fields, String keyword) {
        super(code, description, fields);
        this.keyword = keyword;
    }
}
