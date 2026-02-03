package com.dean.baby.entity;

import com.dean.baby.common.dto.common.LangFieldObject;
import com.dean.baby.common.util.LangFieldObjectConverter;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Data
@Table(name = "categories")
public class Category {

    @Id
    @UuidGenerator
    private UUID id;

    /**
     * 多語言名稱欄位，使用LangFieldObject儲存
     * 支援語言：en(英文), tw(繁體中文), cn(簡體中文), ja(日文), ko(韓文), vi(越南文)
     */
    @Column(name = "name", columnDefinition = "json", nullable = false)
    @Convert(converter = LangFieldObjectConverter.class)
    private LangFieldObject name;
}
