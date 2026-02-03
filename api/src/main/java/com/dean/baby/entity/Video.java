package com.dean.baby.entity;

import com.dean.baby.common.dto.common.LangFieldObject;
import com.dean.baby.common.util.LangFieldObjectConverter;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "videos")
public class Video {
    @Id
    @UuidGenerator
    private UUID id;

    @Column(name = "description", columnDefinition = "json", nullable = false)
    @Convert(converter = LangFieldObjectConverter.class)
    private LangFieldObject description;

    @Column(name = "video_url", nullable = false)
    private String videoUrl;

    @Column(name = "duration_seconds")
    private Integer durationSeconds;

    @Column(name = "thumbnail_url")
    private String thumbnailUrl;

    @ManyToOne
    @JoinColumn(name = "milestone_id")
    private Milestone milestone;

    @ManyToOne
    @JoinColumn(name = "article_id")
    private Article article;

    @ManyToOne
    @JoinColumn(name = "flashcard_id")
    private Flashcard flashcard;

    @OneToMany(mappedBy = "video", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Progress> progresses;
}
