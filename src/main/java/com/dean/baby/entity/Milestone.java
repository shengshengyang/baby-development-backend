package com.dean.baby.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Data
@Table(name = "milestones")
public class Milestone {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "age_in_months")
    private int ageInMonths;

    private String category;

    @OneToMany(mappedBy = "milestone")
    private List<MilestoneTranslation> translations;
}
