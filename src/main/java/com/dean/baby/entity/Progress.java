package com.dean.baby.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
@Table(name = "progress")
public class Progress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "age_in_months")
    private int ageInMonths;

    private String category;

    private boolean achieved;

    @Column(name = "date_achieved")
    private LocalDate dateAchieved;

    @ManyToOne
    @JoinColumn(name = "baby_id")
    private Baby baby;

    @ManyToOne
    @JoinColumn(name = "flashcard_id", nullable = false)
    private Flashcard flashcard;
}
