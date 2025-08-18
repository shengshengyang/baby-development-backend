package com.dean.baby.mvc.controller;

import com.dean.baby.common.entity.Category;
import com.dean.baby.common.entity.Flashcard;
import com.dean.baby.common.entity.FlashcardTranslation;
import com.dean.baby.common.entity.Milestone;
import com.dean.baby.common.repository.CategoryRepository;
import com.dean.baby.common.repository.FlashcardRepository;
import com.dean.baby.common.repository.MilestoneRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/flashcard")
public class FlashCardController {


    private final FlashcardRepository flashcardRepository;
    private final CategoryRepository categoryRepository;
    private final MilestoneRepository milestoneRepository;

    public FlashCardController(FlashcardRepository flashcardRepository, CategoryRepository categoryRepository, MilestoneRepository milestoneRepository) {
        this.flashcardRepository = flashcardRepository;
        this.categoryRepository = categoryRepository;
        this.milestoneRepository = milestoneRepository;
    }

    @GetMapping
    public String listFlashcards(Model model) {
        model.addAttribute("flashcards", flashcardRepository.findAll());
        return "flashcard/list";
    }

    // 新增頁面：顯示新增 Flashcard 表單
    @GetMapping("/add")
    public String addFlashcardForm(Model model) {
        Flashcard flashcard = new Flashcard();
        // 預設產生一筆空的 Translation
        flashcard.setTranslations(new ArrayList<>());
        flashcard.getTranslations().add(new FlashcardTranslation());

        model.addAttribute("flashcard", flashcard);
        // 下拉選單資料
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("milestones", milestoneRepository.findAll());
        return "flashcard/form";
    }

    // 處理新增 Flashcard
    @PostMapping("/add")
    public String addFlashcard(@ModelAttribute Flashcard flashcard,
                               @RequestParam("categoryId") UUID categoryId,
                               @RequestParam("milestoneId") UUID milestoneId) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        Optional<Milestone> milestoneOpt = milestoneRepository.findById(milestoneId);
        if (categoryOpt.isPresent() && milestoneOpt.isPresent()) {
            flashcard.setCategory(categoryOpt.get());
            flashcard.setMilestone(milestoneOpt.get());
        }
        // 將每筆 Translation 的 flashcard 屬性設為目前 flashcard
        if (flashcard.getTranslations() != null) {
            flashcard.getTranslations().forEach(t -> t.setFlashcard(flashcard));
        }
        flashcardRepository.save(flashcard);
        return "redirect:/flashcards";
    }

    // 編輯頁面：顯示編輯 Flashcard 表單
    @GetMapping("/edit/{id}")
    public String editFlashcardForm(@PathVariable("id") UUID id, Model model) {
        Optional<Flashcard> flashcardOpt = flashcardRepository.findById(id);
        if (flashcardOpt.isPresent()) {
            model.addAttribute("flashcard", flashcardOpt.get());
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("milestones", milestoneRepository.findAll());
            return "flashcard/form";
        }
        return "redirect:/flashcards";
    }

    // 處理更新 Flashcard
    @PostMapping("/edit")
    public String updateFlashcard(@ModelAttribute Flashcard flashcard,
                                  @RequestParam("categoryId") UUID categoryId,
                                  @RequestParam("milestoneId") UUID milestoneId) {
        Optional<Category> categoryOpt = categoryRepository.findById(categoryId);
        Optional<Milestone> milestoneOpt = milestoneRepository.findById(milestoneId);
        if (categoryOpt.isPresent() && milestoneOpt.isPresent()) {
            flashcard.setCategory(categoryOpt.get());
            flashcard.setMilestone(milestoneOpt.get());
        }
        if (flashcard.getTranslations() != null) {
            flashcard.getTranslations().forEach(t -> t.setFlashcard(flashcard));
        }
        flashcardRepository.save(flashcard);
        return "redirect:/flashcards";
    }

    // 刪除 Flashcard
    @PostMapping("/delete/{id}")
    public String deleteFlashcard(@PathVariable("id") UUID id) {
        flashcardRepository.deleteById(id);
        return "redirect:/flashcards";
    }
}
