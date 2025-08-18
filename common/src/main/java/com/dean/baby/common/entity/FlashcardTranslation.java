package com.dean.baby.common.entity;

import lombok.*;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "flashcard_translations")
public class FlashcardTranslation {
    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "flashcard_id")
    private Flashcard flashcard;

    @Column(name = "language_code")
    private String languageCode;

    @Column(name = "front_text")
    private String frontText;

    @Column(name = "back_text")
    private String backText;

    @Column(name = "image_url")
    private String imageUrl;
}
