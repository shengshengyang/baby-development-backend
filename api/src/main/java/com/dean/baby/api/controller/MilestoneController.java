package com.dean.baby.api.controller;

import com.dean.baby.common.dto.MilestoneDTO;
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

    // 根據條件查找 Milestone，支援 ageId 和 categoryId 的組合查詢，無條件時返回全部
    @GetMapping("/milestone")
    public ResponseEntity<List<MilestoneDTO>> searchMilestones(
            @RequestParam(required = false) UUID ageId,
            @RequestParam(required = false) UUID categoryId) {
        List<MilestoneDTO> milestones = milestoneService.getMilestonesByConditions(ageId, categoryId);
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
}
