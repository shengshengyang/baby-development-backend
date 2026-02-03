package com.dean.baby.common.repository;

import com.dean.baby.common.entity.Age;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AgeRepository extends JpaRepository<Age, UUID> {
    Optional<Age> findByMonth(int month);
}
