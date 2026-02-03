package com.dean.baby.common.repository;

import com.dean.baby.common.entity.Baby;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BabyRepository extends JpaRepository<Baby, UUID> {
    List<Baby> findByUserId(UUID userId);
}
