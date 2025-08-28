package com.dean.baby.common.entity;

import lombok.Data;
import jakarta.persistence.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.List;
import java.util.UUID;
import com.dean.baby.common.dto.common.LangFieldObject;
import com.dean.baby.common.util.LangFieldObjectConverter;

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

    // 新增：里程碑描述（多語）
    @Column(name = "description", columnDefinition = "json", nullable = false)
    @Convert(converter = LangFieldObjectConverter.class)
    private LangFieldObject description;

    // 新增：影片連結
    @Column(name = "video_url")
    private String videoUrl;

    // 新增：base64 圖片儲存
    @Lob
    @Column(name = "image_base64", columnDefinition = "LONGTEXT")
    private String imageBase64;

    // 為了向后兼容，添加便利方法
    public int getAgeInMonths() {
        return age != null ? age.getMonth() : 0;
    }
}
