package com.dean.baby.mvc.controller;

import com.dean.baby.common.dto.CategoryDTO;
import com.dean.baby.common.dto.MilestoneDTO;
import com.dean.baby.common.dto.MilestoneTranslationDTO;
import com.dean.baby.common.entity.Category;
import com.dean.baby.common.repository.CategoryRepository;
import com.dean.baby.common.service.MilestoneService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/milestones")
public class MilestoneController {

    private final MilestoneService milestoneService;
    private final CategoryRepository categoryRepository;

    public MilestoneController(MilestoneService milestoneService, CategoryRepository categoryRepository) {
        this.milestoneService = milestoneService;
        this.categoryRepository = categoryRepository;
    }

    // 顯示所有 Milestones 列表
    @GetMapping
    public String list(Model model) {
        List<MilestoneDTO> milestones = milestoneService.getAllMilestones();
        model.addAttribute("milestones", milestones);
        return "milestone/list";
    }

    // 顯示新增 Milestone 表單
    @GetMapping("/new")
    public String newMilestone(Model model) {
        model.addAttribute("milestone", new MilestoneDTO());
        model.addAttribute("categories", categoryRepository.findAll());
        return "milestone/form";
    }

    // 處理新增 Milestone
    @PostMapping
    public String createMilestone(@ModelAttribute MilestoneDTO milestoneDTO,
                                 RedirectAttributes redirectAttributes) {
        try {
            milestoneService.createMilestone(milestoneDTO);
            redirectAttributes.addFlashAttribute("success", "Milestone created successfully!");
            return "redirect:/milestones";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to create milestone: " + e.getMessage());
            return "redirect:/milestones/new";
        }
    }

    // 顯示編輯 Milestone 表單
    @GetMapping("/edit/{id}")
    public String editMilestone(@PathVariable UUID id, Model model, RedirectAttributes redirectAttributes) {
        Optional<MilestoneDTO> milestoneOpt = milestoneService.getMilestoneById(id);
        if (milestoneOpt.isPresent()) {
            model.addAttribute("milestone", milestoneOpt.get());
            model.addAttribute("categories", categoryRepository.findAll());
            return "milestone/form";
        } else {
            redirectAttributes.addFlashAttribute("error", "Milestone not found!");
            return "redirect:/milestones";
        }
    }

    // 處理更新 Milestone
    @PostMapping("/edit/{id}")
    public String updateMilestone(@PathVariable UUID id,
                                 @ModelAttribute MilestoneDTO milestoneDTO,
                                 RedirectAttributes redirectAttributes) {
        try {
            milestoneService.updateMilestone(id, milestoneDTO);
            redirectAttributes.addFlashAttribute("success", "Milestone updated successfully!");
            return "redirect:/milestones";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update milestone: " + e.getMessage());
            return "redirect:/milestones/edit/" + id;
        }
    }

    // 處理刪除 Milestone
    @PostMapping("/delete/{id}")
    public String deleteMilestone(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            milestoneService.deleteMilestone(id);
            redirectAttributes.addFlashAttribute("success", "Milestone deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to delete milestone: " + e.getMessage());
        }
        return "redirect:/milestones";
    }

    // 顯示 Milestone 詳細資訊
    @GetMapping("/view/{id}")
    public String viewMilestone(@PathVariable UUID id, Model model, RedirectAttributes redirectAttributes) {
        Optional<MilestoneDTO> milestoneOpt = milestoneService.getMilestoneById(id);
        if (milestoneOpt.isPresent()) {
            model.addAttribute("milestone", milestoneOpt.get());
            return "milestone/view";
        } else {
            redirectAttributes.addFlashAttribute("error", "Milestone not found!");
            return "redirect:/milestones";
        }
    }

    // 根據年齡和語言查詢 Milestones
    @GetMapping("/search")
    public String searchMilestones(@RequestParam(required = false) Integer age,
                                  @RequestParam(required = false) String language,
                                  Model model) {
        if (age != null && language != null && !language.isEmpty()) {
            List<MilestoneTranslationDTO> milestones = milestoneService.getMilestonesByAgeAndLanguage(age, language);
            model.addAttribute("milestoneTranslations", milestones);
        }
        model.addAttribute("searchAge", age);
        model.addAttribute("searchLanguage", language);
        return "milestone/search";
    }
}
