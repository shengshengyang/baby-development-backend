package com.dean.baby.common.util;

import com.dean.baby.common.dto.enums.Language;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.HashMap;
import java.util.Map;

@Converter
public class LanguageMapConverter implements AttributeConverter<Map<Language, String>, String> {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(Map<Language, String> attribute) {
        if (attribute == null) return "{}";
        Map<String, String> rawMap = new HashMap<>();
        for (var entry : attribute.entrySet()) {
            rawMap.put(entry.getKey().getCode(), entry.getValue());
        }
        try {
            return mapper.writeValueAsString(rawMap);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to convert map to JSON", e);
        }
    }

    @Override
    public Map<Language, String> convertToEntityAttribute(String dbData) {
        try {
            Map<String, String> rawMap = mapper.readValue(dbData, new TypeReference<>() {});
            Map<Language, String> result = new HashMap<>();
            for (var entry : rawMap.entrySet()) {
                Language lang = Language.fromCode(entry.getKey());
                if (lang != null) {
                    result.put(lang, entry.getValue());
                }
            }
            return result;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to parse JSON to map", e);
        }
    }
}
