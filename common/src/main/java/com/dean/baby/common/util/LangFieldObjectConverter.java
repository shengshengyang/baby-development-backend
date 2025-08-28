package com.dean.baby.common.util;

import com.dean.baby.common.dto.common.LangFieldObject;
import com.dean.baby.common.dto.enums.Language;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.HashMap;
import java.util.Map;

@Converter
public class LangFieldObjectConverter implements AttributeConverter<LangFieldObject, String> {
    private static final ObjectMapper mapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(LangFieldObject attribute) {
        try {
            if (attribute == null || attribute.getLanguageMap().isEmpty()) {
                // 返回包含所有支持语言的空对象
                Map<String, String> emptyMap = new HashMap<>();
                for (Language lang : Language.values()) {
                    emptyMap.put(lang.getCode(), "");
                }
                return mapper.writeValueAsString(emptyMap);
            }
            return mapper.writeValueAsString(attribute.getLanguageMap());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to serialize LangFieldObject", e);
        }
    }

    @Override
    public LangFieldObject convertToEntityAttribute(String dbData) {
        try {
            if (dbData == null || dbData.isEmpty()) {
                return new LangFieldObject();
            }

            Map<String, String> rawMap = mapper.readValue(dbData, new TypeReference<>() {});
            LangFieldObject result = new LangFieldObject();

            // 只设置有效的语言代码
            for (Map.Entry<String, String> entry : rawMap.entrySet()) {
                Language language = Language.fromCode(entry.getKey());
                if (language != null) {
                    result.setLang(entry.getKey(), entry.getValue());
                }
            }

            return result;
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to deserialize LangFieldObject", e);
        }
    }
}
