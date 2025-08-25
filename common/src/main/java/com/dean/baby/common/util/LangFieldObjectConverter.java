package com.dean.baby.common.util;

import com.dean.baby.common.dto.common.LangFieldObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class LangFieldObjectConverter implements AttributeConverter<LangFieldObject, String> {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(LangFieldObject attribute) {
        try {
            if (attribute == null) return mapper.writeValueAsString(new LangFieldObject());
            return mapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to serialize LangFieldObject", e);
        }
    }

    @Override
    public LangFieldObject convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) return new LangFieldObject();
            return mapper.readValue(dbData, LangFieldObject.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to deserialize LangFieldObject", e);
        }
    }
}

