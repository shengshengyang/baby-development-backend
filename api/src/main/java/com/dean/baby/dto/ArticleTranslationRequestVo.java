package com.dean.baby.dto;

import com.dean.baby.dto.enums.Language;

public record ArticleTranslationRequestVo(
        Language languageCode,
        String title,
        String content
) {
}

