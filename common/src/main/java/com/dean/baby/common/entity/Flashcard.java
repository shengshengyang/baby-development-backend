package com.dean.baby.common.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;
import com.dean.baby.common.dto.common.LangFieldObject;
import com.dean.baby.common.util.LangFieldObjectConverter;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "flashcards")
public class Flashcard {
    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "milestone_id", nullable = false)
    private Milestone milestone;

    @Column(name = "subject")
    @Convert(converter = LangFieldObjectConverter.class)
    private LangFieldObject subject;

    @Column(name = "image_url")
    private String imageUrl;

    @OneToMany(mappedBy = "flashcard")
    private List<FlashcardTranslation> translations;

    @OneToMany(mappedBy = "flashcard")
    private List<Video> videos;
}
