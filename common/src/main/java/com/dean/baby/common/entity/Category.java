package com.dean.baby.common.entity;

import com.dean.baby.common.dto.enums.Language;
import com.dean.baby.common.util.LanguageMapConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Map;

@Entity
@Data
@Table(name = "categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * JSON 欄位儲存多語言名稱，例如：
     * {
     * "en": "Gross Motor",
     * "zh_TW": "粗大動作"
     * }
     */
    @Column(columnDefinition = "json", nullable = false)
    @Convert(converter = LanguageMapConverter.class)
    private Map<Language, String> name;
}
