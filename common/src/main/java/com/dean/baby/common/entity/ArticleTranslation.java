package com.dean.baby.common.entity;

import lombok.Data;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

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
    private String languageCode;

    private String title;

    private String content;
}
