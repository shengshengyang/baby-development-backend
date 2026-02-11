package com.dean.baby.repository;

import com.dean.baby.entity.Baby;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface BabyRepository extends JpaRepository<Baby, UUID> {

    @EntityGraph(attributePaths = {"progresses"})
    List<Baby> findByUserId(UUID userId);
}
