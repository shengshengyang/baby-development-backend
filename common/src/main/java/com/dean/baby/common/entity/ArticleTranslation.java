package com.dean.baby.common.entity;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
@Table(name = "article_translations")
public class ArticleTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @Column(name = "language_code")
    private String languageCode;

    private String title;

    private String content;
}
