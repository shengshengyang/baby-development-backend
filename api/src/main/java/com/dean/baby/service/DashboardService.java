package com.dean.baby.service;

import com.dean.baby.repository.MilestoneRepository;
import com.dean.baby.repository.FlashcardRepository;
import com.dean.baby.repository.ArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardService {
    @Autowired
    private MilestoneRepository milestoneRepository;
    @Autowired
    private FlashcardRepository flashcardRepository;
    @Autowired
    private ArticleRepository articleRepository;

    public long getMilestoneCount() {
        return milestoneRepository.count();
    }

    public long getFlashcardCount() {
        return flashcardRepository.count();
    }

    public long getArticleCount() {
        return articleRepository.count();
    }
}
