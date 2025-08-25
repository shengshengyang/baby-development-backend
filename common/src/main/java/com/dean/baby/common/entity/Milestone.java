package com.dean.baby.common.entity;

import lombok.Data;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Table(name = "milestones")
public class Milestone {
    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "age_id")
    private Age age;

    @ManyToOne(optional = false)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToMany(mappedBy = "milestone", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Flashcard> flashcards;

    @OneToMany(mappedBy = "milestone")
    private List<MilestoneTranslation> translations;

    // 为了向后兼容，添加便利方法
    public int getAgeInMonths() {
        return age != null ? age.getMonth() : 0;
    }
}
