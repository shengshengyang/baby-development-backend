package com.dean.baby.common.util;

import com.dean.baby.common.dto.enums.Language;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = false)
public class LanguageConverter implements AttributeConverter<Language, String> {
    @Override
    public String convertToDatabaseColumn(Language attribute) {
        return attribute != null ? attribute.getCode() : null;
    }

    @Override
    public Language convertToEntityAttribute(String dbData) {
        if (dbData == null) return null;
        return Language.fromCode(dbData);
    }
}

