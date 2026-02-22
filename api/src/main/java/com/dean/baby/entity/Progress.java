package com.dean.baby.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.proxy.HibernateProxy;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Builder
@Table(name = "progress")
@AllArgsConstructor
public class Progress implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @UuidGenerator
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "baby_id")
    private Baby baby;

    @ManyToOne
    @JoinColumn(name = "flashcard_id")
    private Flashcard flashcard;

    @ManyToOne
    @JoinColumn(name = "milestone_id")
    private Milestone milestone;

    @ManyToOne
    @JoinColumn(name = "video_id")
    private Video video;

    @Column(name = "date_achieved")
    private LocalDate dateAchieved;

    @Column(name = "progress_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProgressStatus progressStatus = ProgressStatus.NOT_STARTED;

    @Column(name = "date_started")
    private LocalDate dateStarted;

    @Column(name = "progress_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProgressType progressType;

    // 便利方法獲取年齡
    public int getAgeInMonths() {
        if (flashcard != null && flashcard.getMilestone() != null && flashcard.getMilestone().getAge() != null) {
            return flashcard.getMilestone().getAge().getMonth();
        }
        if (milestone != null && milestone.getAge() != null) {
            return milestone.getAge().getMonth();
        }
        if (video != null && video.getMilestone() != null && video.getMilestone().getAge() != null) {
            return video.getMilestone().getAge().getMonth();
        }
        return 0;
    }

    // 獲取Category
    public Category getCategory() {
        if (flashcard != null) {
            return flashcard.getCategory();
        }
        if (milestone != null) {
            return milestone.getCategory();
        }
        if (video != null && video.getMilestone() != null) {
            return video.getMilestone().getCategory();
        }
        return null;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        Progress progress = (Progress) o;
        return getId() != null && Objects.equals(getId(), progress.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
