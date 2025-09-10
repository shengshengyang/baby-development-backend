package com.dean.baby.mvc.controller;

import com.dean.baby.mvc.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {
    @Autowired
    private DashboardService dashboardService;

    @GetMapping
    public String dashboard(Model model) {
        long milestoneCount = dashboardService.getMilestoneCount();
        long flashcardCount = dashboardService.getFlashcardCount();
        long articleCount = dashboardService.getArticleCount();
        model.addAttribute("milestoneCount", milestoneCount);
        model.addAttribute("flashcardCount", flashcardCount);
        model.addAttribute("articleCount", articleCount);
        return "dashboard";
    }
}
