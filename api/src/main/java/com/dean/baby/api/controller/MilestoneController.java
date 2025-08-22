package com.dean.baby.api.controller;

import com.dean.baby.common.dto.MilestoneDTO;
import com.dean.baby.common.dto.MilestoneTranslationDTO;
import com.dean.baby.common.service.MilestoneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class MilestoneController {

    private final MilestoneService milestoneService;

    @Autowired
    public MilestoneController(MilestoneService milestoneService) {
        this.milestoneService = milestoneService;
    }

    // 創建 Milestone
    @PostMapping("/milestone")
    public ResponseEntity<MilestoneDTO> createMilestone(@RequestBody MilestoneDTO milestoneDTO) {
        MilestoneDTO createdMilestone = milestoneService.createMilestone(milestoneDTO);
        return new ResponseEntity<>(createdMilestone, HttpStatus.CREATED);
    }

    // 讀取單個 Milestone
    @GetMapping("/milestone/{id}")
    public ResponseEntity<MilestoneDTO> getMilestoneById(@PathVariable UUID id) {
        Optional<MilestoneDTO> milestoneDTO = milestoneService.getMilestoneById(id);
        return milestoneDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 讀取所有 Milestones
    @GetMapping("/milestone")
    public ResponseEntity<List<MilestoneDTO>> getAllMilestones() {
        List<MilestoneDTO> milestones = milestoneService.getAllMilestones();
        return ResponseEntity.ok(milestones);
    }

    // 根據年齡和語言查詢 Milestones
    @GetMapping("/milestone/by-age")
    public ResponseEntity<List<MilestoneTranslationDTO>> getMilestonesByAgeAndLanguage(
            @RequestParam int age,
            @RequestParam String language) {
        List<MilestoneTranslationDTO> milestones = milestoneService.getMilestonesByAgeAndLanguage(age, language);
        return ResponseEntity.ok(milestones);
    }

    // 更新 Milestone
    @PutMapping("/milestone/{id}")
    public ResponseEntity<MilestoneDTO> updateMilestone(
            @PathVariable UUID id,
            @RequestBody MilestoneDTO milestoneDTO) {
        MilestoneDTO updatedMilestone = milestoneService.updateMilestone(id, milestoneDTO);
        return ResponseEntity.ok(updatedMilestone);
    }

    // 刪除 Milestone
    @DeleteMapping("/milestone/{id}")
    public ResponseEntity<Void> deleteMilestone(@PathVariable UUID id) {
        milestoneService.deleteMilestone(id);
        return ResponseEntity.noContent().build();
    }

    // 公開接口 - 不需要驗證的查詢接口
    @GetMapping("/open/milestone")
    public ResponseEntity<List<MilestoneDTO>> getAllMilestonesPublic() {
        List<MilestoneDTO> milestones = milestoneService.getAllMilestones();
        return ResponseEntity.ok(milestones);
    }

    @GetMapping("/open/milestone/{id}")
    public ResponseEntity<MilestoneDTO> getMilestoneByIdPublic(@PathVariable UUID id) {
        Optional<MilestoneDTO> milestoneDTO = milestoneService.getMilestoneById(id);
        return milestoneDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/open/milestone/by-age")
    public ResponseEntity<List<MilestoneTranslationDTO>> getMilestonesByAgeAndLanguagePublic(
            @RequestParam int age,
            @RequestParam String language) {
        List<MilestoneTranslationDTO> milestones = milestoneService.getMilestonesByAgeAndLanguage(age, language);
        return ResponseEntity.ok(milestones);
    }
}
