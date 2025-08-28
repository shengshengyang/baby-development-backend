package com.dean.baby.mvc.controller;

import com.dean.baby.common.dto.VideoDto;
import com.dean.baby.common.dto.VideoFormDto;
import com.dean.baby.common.entity.Milestone;
import com.dean.baby.common.entity.Flashcard;
import com.dean.baby.common.repository.MilestoneRepository;
import com.dean.baby.common.repository.FlashcardRepository;
import com.dean.baby.common.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/video")
public class VideoController {

    private final VideoService videoService;
    private final MilestoneRepository milestoneRepository;
    private final FlashcardRepository flashcardRepository;

    @Autowired
    public VideoController(VideoService videoService,
                          MilestoneRepository milestoneRepository,
                          FlashcardRepository flashcardRepository) {
        this.videoService = videoService;
        this.milestoneRepository = milestoneRepository;
        this.flashcardRepository = flashcardRepository;
    }

    /**
     * 顯示所有 Video 列表
     */
    @GetMapping
    public String listVideos(Model model,
                            @RequestParam(value = "success", required = false) String success,
                            @RequestParam(value = "error", required = false) String error) {
        try {
            List<VideoDto> videos = videoService.getAllVideos();
            model.addAttribute("videos", videos);

            if (success != null) {
                model.addAttribute("success", "操作成功完成");
            }
            if (error != null) {
                model.addAttribute("error", "操作失敗，請稍後重試");
            }

            return "video/list";
        } catch (Exception e) {
            model.addAttribute("error", "載入視頻列表失敗：" + e.getMessage());
            return "video/list";
        }
    }

    /**
     * 顯示新增 Video 表單
     */
    @GetMapping("/add")
    public String addVideoForm(Model model) {
        try {
            model.addAttribute("videoForm", new VideoFormDto());

            // 加載可選的關聯對象
            List<Milestone> milestones = milestoneRepository.findAll();
            List<Flashcard> flashcards = flashcardRepository.findAll();
            model.addAttribute("milestones", milestones);
            model.addAttribute("flashcards", flashcards);

            return "video/form";
        } catch (Exception e) {
            return "redirect:/video?error=true";
        }
    }

    /**
     * 處理新增 Video
     */
    @PostMapping("/add")
    public String addVideo(@ModelAttribute VideoFormDto videoForm,
                          @RequestParam(value = "milestoneId", required = false) UUID milestoneId,
                          @RequestParam(value = "flashcardId", required = false) UUID flashcardId,
                          RedirectAttributes redirectAttributes) {
        try {
            VideoDto createdVideo;

            if (milestoneId != null) {
                createdVideo = videoService.createVideoForMilestone(
                    milestoneId,
                    videoForm.toDescription(),
                    videoForm.getVideoUrl(),
                    videoForm.getDurationSeconds(),
                    videoForm.getThumbnailUrl()
                );
            } else if (flashcardId != null) {
                createdVideo = videoService.createVideoForFlashcard(
                    flashcardId,
                    videoForm.toDescription(),
                    videoForm.getVideoUrl(),
                    videoForm.getDurationSeconds(),
                    videoForm.getThumbnailUrl()
                );
            } else {
                redirectAttributes.addFlashAttribute("error", "請選擇要關聯的 Milestone 或 FlashCard");
                return "redirect:/video/add";
            }

            redirectAttributes.addFlashAttribute("success", "Video 新增成功");
            return "redirect:/video";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "新增失敗：" + e.getMessage());
            return "redirect:/video/add";
        }
    }

    /**
     * 顯示編輯 Video 表單
     */
    @GetMapping("/edit/{id}")
    public String editVideoForm(@PathVariable UUID id, Model model) {
        try {
            Optional<VideoDto> videoOpt = videoService.getVideoById(id);
            if (videoOpt.isEmpty()) {
                return "redirect:/video?error=true";
            }

            VideoDto video = videoOpt.get();
            VideoFormDto videoForm = VideoFormDto.fromVideoDto(video);

            model.addAttribute("video", video);
            model.addAttribute("videoForm", videoForm);

            // 加載可選的關聯對象
            List<Milestone> milestones = milestoneRepository.findAll();
            List<Flashcard> flashcards = flashcardRepository.findAll();
            model.addAttribute("milestones", milestones);
            model.addAttribute("flashcards", flashcards);

            return "video/form";
        } catch (Exception e) {
            return "redirect:/video?error=true";
        }
    }

    /**
     * 處理更新 Video
     */
    @PostMapping("/edit/{id}")
    public String updateVideo(@PathVariable UUID id,
                             @ModelAttribute VideoFormDto videoForm,
                             RedirectAttributes redirectAttributes) {
        try {
            videoService.updateVideoWithForm(id, videoForm);
            redirectAttributes.addFlashAttribute("success", "Video 更新成功");
            return "redirect:/video";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "更新失敗：" + e.getMessage());
            return "redirect:/video/edit/" + id;
        }
    }

    /**
     * 刪除 Video
     */
    @PostMapping("/delete/{id}")
    public String deleteVideo(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            videoService.deleteVideo(id);
            redirectAttributes.addFlashAttribute("success", "Video 刪除成功");
            return "redirect:/video";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "刪除失敗：" + e.getMessage());
            return "redirect:/video";
        }
    }

    /**
     * 查看 Video 詳情（包含編輯功能）
     */
    @GetMapping("/view/{id}")
    public String viewVideo(@PathVariable UUID id,
                           @RequestParam(value = "edit", defaultValue = "false") boolean editMode,
                           Model model) {
        try {
            Optional<VideoDto> videoOpt = videoService.getVideoById(id);
            if (videoOpt.isEmpty()) {
                return "redirect:/video?error=true";
            }

            VideoDto video = videoOpt.get();
            model.addAttribute("video", video);

            if (editMode) {
                // 編輯模式 - 載入表單數據
                VideoFormDto videoForm = VideoFormDto.fromVideoDto(video);
                model.addAttribute("videoForm", videoForm);

                // 加載可選的關聯對象
                List<Milestone> milestones = milestoneRepository.findAll();
                List<Flashcard> flashcards = flashcardRepository.findAll();
                model.addAttribute("milestones", milestones);
                model.addAttribute("flashcards", flashcards);
            }

            model.addAttribute("editMode", editMode);
            return "video/view";
        } catch (Exception e) {
            return "redirect:/video?error=true";
        }
    }

    /**
     * 處理更新 Video（從查看頁面提交）
     */
    @PostMapping("/view/{id}")
    public String updateVideoFromView(@PathVariable UUID id,
                                     @ModelAttribute VideoFormDto videoForm,
                                     RedirectAttributes redirectAttributes) {
        try {
            videoService.updateVideoWithForm(id, videoForm);
            redirectAttributes.addFlashAttribute("success", "Video 更新成功");
            return "redirect:/video/view/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "更新失敗：" + e.getMessage());
            return "redirect:/video/view/" + id + "?edit=true";
        }
    }
}
