package com.test.ecommerceorderservice.shared.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.springframework.util.StringUtils;

import java.io.IOException;

public class SpaceDeserializer extends JsonDeserializer<String> {
    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        String value = jsonParser.getValueAsString();
        if (StringUtils.hasText(value)) {
            return value.trim().replaceAll("\\s+", " ");
        }
        return value;
    }
}