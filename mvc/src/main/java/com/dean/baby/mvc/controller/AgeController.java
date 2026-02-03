package com.dean.baby.mvc.controller;

import com.dean.baby.common.dto.AgeDto;
import com.dean.baby.common.entity.Age;
import com.dean.baby.common.repository.AgeRepository;
import com.dean.baby.common.dto.common.LangFieldObject;
import com.dean.baby.common.dto.enums.Language;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequestMapping("/ages")
public class AgeController {

    private final AgeRepository ageRepository;

    public AgeController(AgeRepository ageRepository) {
        this.ageRepository = ageRepository;
    }

    // 顯示所有 Ages 列表
    @GetMapping
    public String list(Model model) {
        List<Age> ages = ageRepository.findAll();
        List<AgeDto> ageDtos = ages.stream()
                .map(AgeDto::fromEntity)
                .toList();
        model.addAttribute("ages", ageDtos);
        return "age/list";
    }

    // 顯示新增 Age 表單
    @GetMapping("/new")
    public String newAge(Model model) {
        AgeDto ageDto = new AgeDto();
        model.addAttribute("age", ageDto);
        return "age/form";
    }

    // 處理新增 Age
    @PostMapping
    public String createAge(@ModelAttribute AgeDto ageDto,
                           @RequestParam(required = false) String displayNameTw,
                           @RequestParam(required = false) String displayNameEn,
                           @RequestParam(required = false) String displayNameCn,
                           @RequestParam(required = false) String displayNameJa,
                           @RequestParam(required = false) String displayNameKo,
                           @RequestParam(required = false) String displayNameVi,
                           RedirectAttributes redirectAttributes) {
        try {
            // 檢查月份是否已存在
            Optional<Age> existingAge = ageRepository.findByMonth(ageDto.getMonth());
            if (existingAge.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "月份 " + ageDto.getMonth() + " 已存在！");
                return "redirect:/ages/new";
            }

            Age age = new Age();
            age.setMonth(ageDto.getMonth());

            // 建立多語言顯示名稱
            LangFieldObject displayName = new LangFieldObject();
            displayName.setLang(Language.TRADITIONAL_CHINESE.getCode(), displayNameTw != null ? displayNameTw : ageDto.getMonth() + "個月");
            displayName.setLang(Language.ENGLISH.getCode(), displayNameEn != null ? displayNameEn : ageDto.getMonth() + " Months");
            displayName.setLang(Language.SIMPLIFIED_CHINESE.getCode(), displayNameCn != null ? displayNameCn : ageDto.getMonth() + "个月");
            displayName.setLang(Language.JAPANESE.getCode(), displayNameJa != null ? displayNameJa : ageDto.getMonth() + "ヶ月");
            displayName.setLang(Language.KOREAN.getCode(), displayNameKo != null ? displayNameKo : ageDto.getMonth() + "개월");
            displayName.setLang(Language.VIETNAMESE.getCode(), displayNameVi != null ? displayNameVi : ageDto.getMonth() + " tháng");

            age.setDisplayName(displayName);
            ageRepository.save(age);

            redirectAttributes.addFlashAttribute("success", "Age 創建成功！");
            return "redirect:/ages";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "創建失敗：" + e.getMessage());
            return "redirect:/ages/new";
        }
    }

    // 顯示編輯 Age 表單
    @GetMapping("/edit/{id}")
    public String editAge(@PathVariable UUID id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Age> ageOpt = ageRepository.findById(id);
        if (ageOpt.isPresent()) {
            AgeDto ageDto = AgeDto.fromEntity(ageOpt.get());
            model.addAttribute("age", ageDto);
            model.addAttribute("ageEntity", ageOpt.get());
            return "age/form";
        } else {
            redirectAttributes.addFlashAttribute("error", "Age 不存在！");
            return "redirect:/ages";
        }
    }

    // 處理更新 Age
    @PostMapping("/edit/{id}")
    public String updateAge(@PathVariable UUID id,
                           @ModelAttribute AgeDto ageDto,
                           @RequestParam(required = false) String displayNameTw,
                           @RequestParam(required = false) String displayNameEn,
                           @RequestParam(required = false) String displayNameCn,
                           @RequestParam(required = false) String displayNameJa,
                           @RequestParam(required = false) String displayNameKo,
                           @RequestParam(required = false) String displayNameVi,
                           RedirectAttributes redirectAttributes) {
        try {
            Optional<Age> ageOpt = ageRepository.findById(id);
            if (ageOpt.isEmpty()) {
                redirectAttributes.addFlashAttribute("error", "Age 不存在！");
                return "redirect:/ages";
            }

            // 檢查月份是否與其他記錄衝突
            Optional<Age> existingAge = ageRepository.findByMonth(ageDto.getMonth());
            if (existingAge.isPresent() && !existingAge.get().getId().equals(id)) {
                redirectAttributes.addFlashAttribute("error", "月份 " + ageDto.getMonth() + " 已被其他記錄使用！");
                return "redirect:/ages/edit/" + id;
            }

            Age age = ageOpt.get();
            age.setMonth(ageDto.getMonth());

            // 更新多語言顯示名稱
            LangFieldObject displayName = age.getDisplayName();
            if (displayName == null) {
                displayName = new LangFieldObject();
            }

            displayName.setLang(Language.TRADITIONAL_CHINESE.getCode(), displayNameTw != null ? displayNameTw : ageDto.getMonth() + "個月");
            displayName.setLang(Language.ENGLISH.getCode(), displayNameEn != null ? displayNameEn : ageDto.getMonth() + " Months");
            displayName.setLang(Language.SIMPLIFIED_CHINESE.getCode(), displayNameCn != null ? displayNameCn : ageDto.getMonth() + "个月");
            displayName.setLang(Language.JAPANESE.getCode(), displayNameJa != null ? displayNameJa : ageDto.getMonth() + "ヶ月");
            displayName.setLang(Language.KOREAN.getCode(), displayNameKo != null ? displayNameKo : ageDto.getMonth() + "개월");
            displayName.setLang(Language.VIETNAMESE.getCode(), displayNameVi != null ? displayNameVi : ageDto.getMonth() + " tháng");

            age.setDisplayName(displayName);
            ageRepository.save(age);

            redirectAttributes.addFlashAttribute("success", "Age 更新成功！");
            return "redirect:/ages";
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "更新失敗：" + e.getMessage());
            return "redirect:/ages/edit/" + id;
        }
    }

    // 處理刪除 Age
    @PostMapping("/delete/{id}")
    public String deleteAge(@PathVariable UUID id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Age> ageOpt = ageRepository.findById(id);
            if (!ageOpt.isPresent()) {
                redirectAttributes.addFlashAttribute("error", "Age 不存在！");
                return "redirect:/ages";
            }

            ageRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Age 刪除成功！");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "刪除失敗：" + e.getMessage());
        }
        return "redirect:/ages";
    }

    // 顯示 Age 詳細資訊
    @GetMapping("/view/{id}")
    public String viewAge(@PathVariable UUID id, Model model, RedirectAttributes redirectAttributes) {
        Optional<Age> ageOpt = ageRepository.findById(id);
        if (ageOpt.isPresent()) {
            AgeDto ageDto = AgeDto.fromEntity(ageOpt.get());
            model.addAttribute("age", ageDto);
            model.addAttribute("ageEntity", ageOpt.get());
            return "age/view";
        } else {
            redirectAttributes.addFlashAttribute("error", "Age 不存在！");
            return "redirect:/ages";
        }
    }
}
