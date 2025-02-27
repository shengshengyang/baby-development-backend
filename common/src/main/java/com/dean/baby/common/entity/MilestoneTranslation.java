package com.dean.baby.common.entity;

import lombok.Data;
import jakarta.persistence.*;

@Entity
@Data
@Table(name = "milestone_translations")
public class MilestoneTranslation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "milestone_id")
    private Milestone milestone;

    @Column(name = "language_code")
    private String languageCode;

    private String description;
}
