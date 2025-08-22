package com.dean.baby.common.entity;

import lombok.Data;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;
import com.dean.baby.common.dto.enums.Language;
import com.dean.baby.common.util.LanguageConverter;

@Entity
@Data
@Table(name = "milestone_translations")
public class MilestoneTranslation {
    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "milestone_id")
    private Milestone milestone;

    @Column(name = "language_code")
    @Convert(converter = LanguageConverter.class)
    private Language languageCode;

    private String description;
}
