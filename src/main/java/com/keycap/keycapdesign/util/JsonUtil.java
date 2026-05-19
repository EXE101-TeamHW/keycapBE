package com.keycap.keycapdesign.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.keycap.keycapdesign.exception.BadRequestException;

import java.util.Collections;
import java.util.List;

public class JsonUtil {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static String toJson(List<String> values) {
        try {
            return MAPPER.writeValueAsString(values == null ? Collections.emptyList() : values);
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Invalid JSON list");
        }
    }

    public static List<String> fromJson(String json) {
        try {
            if (json == null || json.isBlank()) {
                return Collections.emptyList();
            }
            return MAPPER.readValue(json, MAPPER.getTypeFactory().constructCollectionType(List.class, String.class));
        } catch (JsonProcessingException e) {
            throw new BadRequestException("Invalid JSON payload");
        }
    }
}

