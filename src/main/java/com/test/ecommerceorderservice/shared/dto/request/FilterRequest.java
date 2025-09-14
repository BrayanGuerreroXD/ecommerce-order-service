package com.test.ecommerceorderservice.shared.dto.request;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.test.ecommerceorderservice.shared.config.SpaceDeserializer;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FilterRequest {
    @JsonDeserialize(using = SpaceDeserializer.class)
    private String column;
    @JsonDeserialize(using = SpaceDeserializer.class)
    private String order;
    private int page;
    private int size;

    public FilterRequest(int page, int size, String column, String order) {
        this.page = page;
        this.size = size;
        this.order = order;
        this.column = column;
    }
}
