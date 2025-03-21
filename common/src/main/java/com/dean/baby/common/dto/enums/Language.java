package com.dean.baby.common.dto.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum Language {
    ENGLISH("en"),
    TRADITIONAL_CHINESE("zh_TW");

    private final String code;

    Language(String code) {
        this.code = code;
    }

    private static final Map<String, Language> CODE_MAP =
            Arrays.stream(values()).collect(Collectors.toMap(Language::getCode, l -> l));

    public static Language fromCode(String code) {
        return CODE_MAP.get(code);
    }
}
