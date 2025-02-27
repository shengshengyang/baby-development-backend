package com.dean.baby.common.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "flashcards")
public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String category;

    @ManyToOne
    @JoinColumn(name = "milestone_id", nullable = false)
    private Milestone milestone;

    @OneToMany(mappedBy = "flashcard")
    private List<FlashcardTranslation> translations;
}
