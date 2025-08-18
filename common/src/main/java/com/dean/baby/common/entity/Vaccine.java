package com.dean.baby.common.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.UuidGenerator;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "vaccine")
@Data
public class Vaccine implements Serializable {

    @Id
    @UuidGenerator
    private UUID id;

    // 疫苗名稱，如"五合一"、"肺炎鏈球菌"等
    private String name;

    // 建議的初次接種月齡（例如2個月）
    @Column(name = "recommended_age_start")
    private Integer recommendedAgeStart;

    // 若此疫苗須多次接種，可用JSON或再用子表格記錄各劑次時程
    // 這裡舉例使用簡單欄位代表第 n 劑建議的接種月齡
    @Column(name = "dose_intervals_json")
    private String doseIntervalsJson;

    // 其他備註說明
    private String description;

}
