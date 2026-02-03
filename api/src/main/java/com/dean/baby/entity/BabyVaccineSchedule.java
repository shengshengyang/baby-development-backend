package com.dean.baby.entity;

import com.dean.baby.common.dto.enums.VaccineStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "baby_vaccine_schedule")
@Data
public class BabyVaccineSchedule implements Serializable {
    @Id
    @UuidGenerator
    private UUID id;

    // 關聯到寶寶
    @ManyToOne
    @JoinColumn(name = "baby_id")
    @JsonIgnore
    private Baby baby;

    // 關聯到疫苗種類
    @ManyToOne
    @JoinColumn(name = "vaccine_id")
    private Vaccine vaccine;

    // 第幾劑 (若此疫苗有多劑次)
    private Integer doseNumber;

    // 預定施打日期(由寶寶生日與建議施打月齡推算)
    @Column(name = "scheduled_date")
    private LocalDate scheduledDate;

    // 實際施打日期
    @Column(name = "actual_date")
    private LocalDate actualDate;

    // 狀態: SCHEDULED(已排程), COMPLETED(已施打), DELAYED(延遲)...
    @Enumerated(EnumType.STRING)
    private VaccineStatus status;

    // 記錄若有延遲或改期後的調整日期
    @Column(name = "rescheduled_date")
    private LocalDate rescheduledDate;

    // 給家長或診所的提醒日期
    @Column(name = "reminder_date")
    private LocalDate reminderDate;

    // 其他備註
    private String note;

}
