package com.dean.baby.common.repository;

import com.dean.baby.common.entity.BabyVaccineSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BabyVaccineScheduleRepository extends JpaRepository<BabyVaccineSchedule, UUID> {

    // 依寶寶 ID 查詢
    List<BabyVaccineSchedule> findByBabyId(UUID babyId);

    // 依寶寶 ID + 疫苗 ID 查詢
    List<BabyVaccineSchedule> findByBabyIdAndVaccineId(UUID babyId, UUID vaccineId);
}
