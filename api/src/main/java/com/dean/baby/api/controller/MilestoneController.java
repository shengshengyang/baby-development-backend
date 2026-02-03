package com.dean.baby.api.controller;

import com.dean.baby.common.dto.MilestoneDTO;
import com.dean.baby.common.service.MilestoneService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api")
@Tag(name = "里程碑管理", description = "寶寶發展里程碑管理 API")
public class MilestoneController {

    private final MilestoneService milestoneService;

    @Autowired
    public MilestoneController(MilestoneService milestoneService) {
        this.milestoneService = milestoneService;
    }

    @Operation(summary = "創建里程碑", description = "創建新的寶寶發展里程碑")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "創建成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "請求參數錯誤"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "未授權")
    })
    @PostMapping(value = "/milestone", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MilestoneDTO> createMilestone(@RequestBody MilestoneDTO milestoneDTO) {
        MilestoneDTO createdMilestone = milestoneService.createMilestone(milestoneDTO);
        return new ResponseEntity<>(createdMilestone, HttpStatus.CREATED);
    }

    @Operation(summary = "獲取里程碑詳情", description = "根據 ID 獲取單個里程碑的詳細信息")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "獲取成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "里程碑不存在")
    })
    @GetMapping(value = "/milestone/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MilestoneDTO> getMilestoneById(
            @Parameter(description = "里程碑 ID") @PathVariable UUID id) {
        Optional<MilestoneDTO> milestoneDTO = milestoneService.getMilestoneById(id);
        return milestoneDTO.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @Operation(summary = "查詢里程碑列表", description = "根據年齡段和/或分類查詢里程碑，無條件時返回全部")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "查詢成功")
    })
    @GetMapping(value = "/milestone", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MilestoneDTO>> searchMilestones(
            @Parameter(description = "年齡段 ID") @RequestParam(required = false) UUID ageId,
            @Parameter(description = "分類 ID") @RequestParam(required = false) UUID categoryId) {
        List<MilestoneDTO> milestones = milestoneService.getMilestonesByConditions(ageId, categoryId);
        return ResponseEntity.ok(milestones);
    }

    @Operation(summary = "更新里程碑", description = "更新指定 ID 的里程碑信息")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "更新成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "請求參數錯誤"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "里程碑不存在")
    })
    @PutMapping(value = "/milestone/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MilestoneDTO> updateMilestone(
            @Parameter(description = "里程碑 ID") @PathVariable UUID id,
            @RequestBody MilestoneDTO milestoneDTO) {
        MilestoneDTO updatedMilestone = milestoneService.updateMilestone(id, milestoneDTO);
        return ResponseEntity.ok(updatedMilestone);
    }

    @Operation(summary = "刪除里程碑", description = "刪除指定 ID 的里程碑")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "刪除成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "401", description = "未授權")
    })
    @DeleteMapping(value = "/milestone/{id}")
    public ResponseEntity<Void> deleteMilestone(
            @Parameter(description = "里程碑 ID") @PathVariable UUID id) {
        milestoneService.deleteMilestone(id);
        return ResponseEntity.noContent().build();
    }
}
