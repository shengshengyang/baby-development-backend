package com.dean.baby.common.repository;

import com.dean.baby.common.entity.BabyVaccineSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BabyVaccineScheduleRepository extends JpaRepository<BabyVaccineSchedule, Long> {

    // 依寶寶 ID 查詢
    List<BabyVaccineSchedule> findByBabyId(Long babyId);

    // 依寶寶 ID + 疫苗 ID 查詢
    List<BabyVaccineSchedule> findByBabyIdAndVaccineId(Long babyId, Long vaccineId);
}
