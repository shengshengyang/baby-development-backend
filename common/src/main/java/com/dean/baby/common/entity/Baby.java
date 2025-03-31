package com.dean.baby.common.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Data
@Table(name = "babies")
@ToString
public class Baby implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "baby", fetch = FetchType.LAZY)
    private List<Progress> progresses;

    @OneToMany(mappedBy = "baby", fetch = FetchType.LAZY)
    private List<BabyVaccineSchedule> babyVaccineSchedules;
}
