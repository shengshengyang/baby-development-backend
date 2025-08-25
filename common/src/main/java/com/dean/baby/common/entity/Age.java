package com.dean.baby.common.entity;

import com.dean.baby.common.dto.common.LangFieldObject;
import com.dean.baby.common.util.LangFieldObjectConverter;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Data
@Table(name = "ages", uniqueConstraints = {
        @UniqueConstraint(name = "uk_ages_month", columnNames = {"month"})
})
public class Age {
    @Id
    @UuidGenerator
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;

    @Column(name = "month", nullable = false)
    private int month;

    @Column(name = "display_name", columnDefinition = "json", nullable = false)
    @Convert(converter = LangFieldObjectConverter.class)
    private LangFieldObject displayName;
}

