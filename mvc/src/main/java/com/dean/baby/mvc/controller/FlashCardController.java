package com.dean.baby.mvc.controller;

import com.dean.baby.common.entity.*;
import com.dean.baby.common.repository.*;
import com.dean.baby.common.dto.VideoDto;
import com.dean.baby.common.dto.VideoFormDto;
import com.dean.baby.common.service.VideoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/flashcard")
public class FlashCardController {

    private final FlashcardRepository flashcardRepository;
    private final CategoryRepository categoryRepository;
    private final MilestoneRepository milestoneRepository;
    private final VideoService videoService;

    public FlashCardController(FlashcardRepository flashcardRepository,
                              CategoryRepository categoryRepository,
                              MilestoneRepository milestoneRepository,
                              VideoService videoService) {
        this.flashcardRepository = flashcardRepository;
        this.categoryRepository = categoryRepository;
        this.milestoneRepository = milestoneRepository;
        this.videoService = videoService;
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
        return "redirect:/flashcard";
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
        return "redirect:/flashcard";
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
        return "redirect:/flashcard";
    }

    // 刪除 Flashcard
    @PostMapping("/delete/{id}")
    public String deleteFlashcard(@PathVariable("id") UUID id) {
        flashcardRepository.deleteById(id);
        return "redirect:/flashcard";
    }

    // ===== Video Management for FlashCard =====

    /**
     * 顯示FlashCard的Video列表
     */
    @GetMapping("/{flashcardId}/videos")
    public String listFlashcardVideos(@PathVariable UUID flashcardId, Model model) {
        Optional<Flashcard> flashcardOpt = flashcardRepository.findById(flashcardId);
        if (flashcardOpt.isPresent()) {
            List<VideoDto> videos = videoService.getVideosByFlashcard(flashcardId);
            model.addAttribute("flashcard", flashcardOpt.get());
            model.addAttribute("videos", videos);
            return "flashcard/video-list";
        }
        return "redirect:/flashcard";
    }

    /**
     * 顯示新增Video表單
     */
    @GetMapping("/{flashcardId}/videos/add")
    public String addVideoForm(@PathVariable UUID flashcardId, Model model) {
        Optional<Flashcard> flashcardOpt = flashcardRepository.findById(flashcardId);
        if (flashcardOpt.isPresent()) {
            model.addAttribute("flashcard", flashcardOpt.get());
            model.addAttribute("videoForm", new VideoFormDto());
            return "flashcard/video-form";
        }
        return "redirect:/flashcard";
    }

    /**
     * 處理新增Video
     */
    @PostMapping("/{flashcardId}/videos/add")
    public String addVideo(@PathVariable UUID flashcardId, @ModelAttribute VideoFormDto videoForm) {
        try {
            videoService.createVideoForFlashcardWithForm(flashcardId, videoForm);
            return "redirect:/flashcard/" + flashcardId + "/videos";
        } catch (Exception e) {
            return "redirect:/flashcard/" + flashcardId + "/videos?error=true";
        }
    }

    /**
     * 顯示編輯Video表單
     */
    @GetMapping("/{flashcardId}/videos/edit/{videoId}")
    public String editVideoForm(@PathVariable UUID flashcardId, @PathVariable UUID videoId, Model model) {
        Optional<Flashcard> flashcardOpt = flashcardRepository.findById(flashcardId);

        if (flashcardOpt.isPresent()) {
            VideoFormDto videoForm = videoService.prepareVideoFormForEdit(videoId);
            Optional<VideoDto> videoOpt = videoService.getVideoById(videoId);

            model.addAttribute("flashcard", flashcardOpt.get());
            model.addAttribute("video", videoOpt.orElse(null));
            model.addAttribute("videoForm", videoForm);
            return "flashcard/video-form";
        }
        return "redirect:/flashcard/" + flashcardId + "/videos";
    }

    /**
     * 處理更新Video
     */
    @PostMapping("/{flashcardId}/videos/edit/{videoId}")
    public String updateVideo(@PathVariable UUID flashcardId, @PathVariable UUID videoId, @ModelAttribute VideoFormDto videoForm) {
        try {
            videoService.updateVideoWithForm(videoId, videoForm);
            return "redirect:/flashcard/" + flashcardId + "/videos";
        } catch (Exception e) {
            return "redirect:/flashcard/" + flashcardId + "/videos?error=true";
        }
    }

    /**
     * 刪除Video
     */
    @PostMapping("/{flashcardId}/videos/delete/{videoId}")
    public String deleteVideo(@PathVariable UUID flashcardId, @PathVariable UUID videoId) {
        try {
            videoService.deleteVideo(videoId);
            return "redirect:/flashcard/" + flashcardId + "/videos";
        } catch (Exception e) {
            return "redirect:/flashcard/" + flashcardId + "/videos?error=true";
        }
    }
}
