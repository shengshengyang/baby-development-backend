package com.dean.baby.common.dto;

import com.dean.baby.common.dto.enums.Language;

public record ArticleTranslationRequestVo(
        Language languageCode,
        String title,
        String content
) {
}

