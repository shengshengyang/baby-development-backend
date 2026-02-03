package com.dean.baby.entity;

import lombok.Data;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;
import com.dean.baby.common.dto.enums.Language;
import com.dean.baby.common.util.LanguageConverter;

@Entity
@Data
@Table(name = "article_translations")
public class ArticleTranslation {
    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(name = "language_code")
    @Convert(converter = LanguageConverter.class)
    private Language languageCode;

    private String title;

    private String content;
}
