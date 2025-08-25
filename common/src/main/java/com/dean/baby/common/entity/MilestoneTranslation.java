package com.dean.baby.common.entity;

import lombok.Data;

import java.util.UUID;
import com.dean.baby.common.dto.enums.Language;

/**
 * Deprecated: 里程碑翻譯表已移除，改用 Milestone.description (LangFieldObject)。
 * 本類保留作為兼容性的��料結構，不再標記為 JPA 實體。
 */
@Deprecated
@Data
public class MilestoneTranslation {
    private UUID id;
    private Milestone milestone;
    private Language languageCode;
    private String description;
}
