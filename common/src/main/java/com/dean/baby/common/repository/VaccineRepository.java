package com.dean.baby.common.repository;

import com.dean.baby.common.entity.Vaccine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VaccineRepository extends JpaRepository<Vaccine, Long> {
}
