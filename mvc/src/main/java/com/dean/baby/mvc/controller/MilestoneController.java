package com.dean.baby.mvc.controller;

import com.dean.baby.common.dto.MilestoneDTO;
import com.dean.baby.common.dto.common.LangFieldObject;
import com.dean.baby.common.repository.CategoryRepository;
import com.dean.baby.common.repository.AgeRepository;
import com.dean.baby.common.service.MilestoneService;
import com.dean.baby.common.service.OptionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/milestones")
public class MilestoneController {

    private final MilestoneService milestoneService;
    private final CategoryRepository categoryRepository;
    private final AgeRepository ageRepository;
    private final OptionService optionService;

    public MilestoneController(MilestoneService milestoneService,
                              CategoryRepository categoryRepository,
                              AgeRepository ageRepository,
                              OptionService optionService) {
        this.milestoneService = milestoneService;
        this.categoryRepository = categoryRepository;
        this.ageRepository = ageRepository;
        this.optionService = optionService;
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
        MilestoneDTO milestone = new MilestoneDTO();
        // 初始化多語描述物件，讓 Spring DataBinder 可以綁定 nested 欄位
        milestone.setDescriptionObject(new LangFieldObject());
        model.addAttribute("milestone", milestone);
        model.addAttribute("categoryOptions", optionService.getCategoryOptions());
        model.addAttribute("ageOptions", optionService.getAgeOptions());
        return "milestone/form";
    }

    // 處理新增 Milestone
    @PostMapping
    public String createMilestone(@ModelAttribute MilestoneDTO milestoneDTO,
                                 @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                 RedirectAttributes redirectAttributes) {
        try {
            if (milestoneDTO.getAge() == null || milestoneDTO.getAge().getId() == null) {
                throw new IllegalArgumentException("Age id is required");
            }
            if (milestoneDTO.getCategory() == null || milestoneDTO.getCategory().getId() == null) {
                throw new IllegalArgumentException("Category id is required");
            }
            // 處理圖片上傳
            if (imageFile != null && !imageFile.isEmpty()) {
                String base64Image = convertToBase64(imageFile);
                milestoneDTO.setImageBase64(base64Image);
            }
            // 若未綁定（理論上不會），避免 NPE
            if (milestoneDTO.getDescriptionObject() == null) {
                milestoneDTO.setDescriptionObject(new LangFieldObject());
            }
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
    public String editMilestone(@PathVariable UUID id, Model model) {
        Optional<MilestoneDTO> milestoneOpt = milestoneService.getMilestoneById(id);
        if (milestoneOpt.isPresent()) {
            MilestoneDTO milestone = milestoneOpt.get();
            // 確保 descriptionObject 不為 null
            if (milestone.getDescriptionObject() == null) {
                milestone.setDescriptionObject(new LangFieldObject());
            }
            model.addAttribute("milestone", milestone);
            model.addAttribute("categoryOptions", optionService.getCategoryOptions());
            model.addAttribute("ageOptions", optionService.getAgeOptions());
            return "milestone/form";
        } else {
            model.addAttribute("error", "Milestone not found!");
            return "redirect:/milestones";
        }
    }

    // 處理更新 Milestone
    @PostMapping("/edit/{id}")
    public String updateMilestone(@PathVariable UUID id,
                                 @ModelAttribute MilestoneDTO milestoneDTO,
                                 @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                 RedirectAttributes redirectAttributes) {
        try {
            if (milestoneDTO.getAge() == null || milestoneDTO.getAge().getId() == null) {
                throw new IllegalArgumentException("Age id is required");
            }
            if (milestoneDTO.getCategory() == null || milestoneDTO.getCategory().getId() == null) {
                throw new IllegalArgumentException("Category id is required");
            }
            if (imageFile != null && !imageFile.isEmpty()) {
                String base64Image = convertToBase64(imageFile);
                milestoneDTO.setImageBase64(base64Image);
            }
            if (milestoneDTO.getDescriptionObject() == null) {
                milestoneDTO.setDescriptionObject(new LangFieldObject());
            }
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

    /**
     * 查看 Milestone 詳情（包含編輯功能）
     */
    @GetMapping("/view/{id}")
    public String viewMilestone(@PathVariable UUID id,
                               @RequestParam(value = "edit", defaultValue = "false") boolean editMode,
                               Model model, RedirectAttributes redirectAttributes) {
        Optional<MilestoneDTO> milestoneOpt = milestoneService.getMilestoneById(id);
        if (milestoneOpt.isPresent()) {
            MilestoneDTO milestone = milestoneOpt.get();
            if (milestone.getDescriptionObject() == null) {
                milestone.setDescriptionObject(new LangFieldObject());
            }
            model.addAttribute("milestone", milestone);

            if (editMode) {
                // 改用精簡選項
                model.addAttribute("ageOptions", optionService.getAgeOptions());
                model.addAttribute("categoryOptions", optionService.getCategoryOptions());
            }

            model.addAttribute("editMode", editMode);
            return "milestone/view";
        } else {
            redirectAttributes.addFlashAttribute("error", "Milestone not found!");
            return "redirect:/milestones";
        }
    }

    /**
     * 處理更新 Milestone（從查看頁面提交）
     */
    @PostMapping("/view/{id}")
    public String updateMilestoneFromView(@PathVariable UUID id,
                                         @ModelAttribute MilestoneDTO milestoneDTO,
                                         @RequestParam(value = "imageFile", required = false) MultipartFile imageFile,
                                         RedirectAttributes redirectAttributes) {
        try {
            if (milestoneDTO.getAge() == null || milestoneDTO.getAge().getId() == null) {
                throw new IllegalArgumentException("Age id is required");
            }
            if (milestoneDTO.getCategory() == null || milestoneDTO.getCategory().getId() == null) {
                throw new IllegalArgumentException("Category id is required");
            }
            if (imageFile != null && !imageFile.isEmpty()) {
                String base64Image = convertToBase64(imageFile);
                milestoneDTO.setImageBase64(base64Image);
            }
            if (milestoneDTO.getDescriptionObject() == null) {
                milestoneDTO.setDescriptionObject(new LangFieldObject());
            }
            milestoneService.updateMilestone(id, milestoneDTO);
            redirectAttributes.addFlashAttribute("success", "Milestone updated successfully!");
            return "redirect:/milestones/view/" + id;
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Failed to update milestone: " + e.getMessage());
            return "redirect:/milestones/view/" + id + "?edit=true";
        }
    }

    // 根據年齡查詢 Milestones
    @GetMapping("/search")
    public String searchMilestones(@RequestParam(required = false) Integer ageMonth,
                                  @RequestParam(required = false) UUID ageId,
                                  Model model) {
        List<MilestoneDTO> milestones = List.of();

        if (ageId != null) {
            milestones = milestoneService.getMilestonesByAgeId(ageId);
        } else if (ageMonth != null) {
            // 使用預設語言進行搜尋
            milestones = milestoneService.getMilestonesByAgeAndLanguage(ageMonth, "zh-TW");
        }

        model.addAttribute("milestones", milestones);
        model.addAttribute("searchAgeMonth", ageMonth);
        model.addAttribute("searchAgeId", ageId);
        model.addAttribute("ages", ageRepository.findAll());
        return "milestone/search";
    }

    /**
     * 將 MultipartFile 轉換為 Base64 字串
     */
    private String convertToBase64(MultipartFile file) throws Exception {
        byte[] bytes = file.getBytes();
        String mimeType = file.getContentType();
        String base64 = Base64.getEncoder().encodeToString(bytes);
        return "data:" + mimeType + ";base64," + base64;
    }
}
