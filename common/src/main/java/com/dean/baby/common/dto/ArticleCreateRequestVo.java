package com.dean.baby.common.dto;

import jakarta.annotation.Nullable;

import java.util.List;
import java.util.UUID;

public record ArticleCreateRequestVo(
        @Nullable UUID id,
        UUID categoryId,
        List<ArticleTranslationRequestVo> translations
) {
}
