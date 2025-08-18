package com.dean.baby.common.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDate;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Data
@Table(name = "measurements")
public class Measurement {
    @Id
    @UuidGenerator
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "baby_id")
    private Baby baby;

    private LocalDate date;

    private Double headCircumference;

    private Double height;

    private Double weight;
}
