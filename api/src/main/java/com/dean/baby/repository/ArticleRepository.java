package com.dean.baby.repository;

import com.dean.baby.entity.Article;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArticleRepository extends JpaRepository<Article, UUID> {

    @EntityGraph(attributePaths = {"translations", "category"})
    List<Article> findAll();

    @EntityGraph(attributePaths = {"translations", "category"})
    List<Article> findByCategoryId(UUID categoryId);

    @EntityGraph(attributePaths = {"translations", "category"})
    @Override
    List<Article> findAllById(Iterable<UUID> ids);
}
