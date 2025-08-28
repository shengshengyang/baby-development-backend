package com.dean.baby.common.dto.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public enum Language {
    ENGLISH("en"),
    JAPANESE("ja"),
    SIMPLIFIED_CHINESE("cn"),
    TRADITIONAL_CHINESE("tw"),
    KOREAN("ko"),
    VIETNAMESE("vi");

    private final String code;

    Language(String code) {
        this.code = code;
    }

    private static final Map<String, Language> CODE_MAP =
            Arrays.stream(values()).collect(Collectors.toMap(Language::getCode, l -> l));

    /**
     * 根據語言代碼取得對應的語言枚舉
     * @param code 語言代碼
     * @return 對應的語言枚舉，若找不到則返回null
     */
    public static Language fromCode(String code) {
        return CODE_MAP.get(code);
    }
}
