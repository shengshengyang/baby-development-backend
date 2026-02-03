package com.dean.baby.entity;

import lombok.*;
import com.dean.baby.common.dto.enums.Language;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;
import com.dean.baby.common.util.LanguageConverter;

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
    @Convert(converter = LanguageConverter.class)
    private Language languageCode;

    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
}
