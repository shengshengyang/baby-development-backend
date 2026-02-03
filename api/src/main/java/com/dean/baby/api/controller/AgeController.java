package com.dean.baby.api.controller;

import com.dean.baby.common.dto.AgeDto;
import com.dean.baby.common.dto.common.LangFieldObject;
import com.dean.baby.common.entity.Age;
import com.dean.baby.common.service.AgeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/ages")
@Tag(name = "年齡段管理", description = "寶寶年齡段分類管理 API")
public class AgeController {

    private final AgeService ageService;

    @Autowired
    public AgeController(AgeService ageService) {
        this.ageService = ageService;
    }

    @Operation(summary = "獲取所有年齡段", description = "獲取系統中所有的年齡段分類")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "獲取成功")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Age>> getAllAges() {
        return ResponseEntity.ok(ageService.findAll());
    }

    @Operation(summary = "獲取年齡段詳情", description = "根據 ID 獲取單個年齡段的詳細信息")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "獲取成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "年齡段不存在")
    })
    @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Age> getAgeById(
            @Parameter(description = "年齡段 ID") @PathVariable UUID id) {
        return ageService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "創建年齡段", description = "創建新的寶寶年齡段分類")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "創建成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "請求參數錯誤")
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createAge(@RequestBody AgeCreateRequest request) {
        try {
            Age created = ageService.create(request.getAgeDto(), request.getDisplayName());
            return ResponseEntity.ok(created);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "更新年齡段", description = "更新指定 ID 的年齡段信息")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "更新成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "請求參數錯誤"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "年齡段不存在")
    })
    @PutMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateAge(
            @Parameter(description = "年齡段 ID") @PathVariable UUID id,
            @RequestBody AgeCreateRequest request) {
        try {
            Age updated = ageService.update(id, request.getAgeDto(), request.getDisplayName());
            return ResponseEntity.ok(updated);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Operation(summary = "刪除年齡段", description = "刪除指定 ID 的年齡段")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "刪除成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "年齡段不存在")
    })
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<?> deleteAge(
            @Parameter(description = "年齡段 ID") @PathVariable UUID id) {
        try {
            ageService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    public static class AgeCreateRequest {
        private AgeDto ageDto;
        private LangFieldObject displayName;

        public AgeDto getAgeDto() {
            return ageDto;
        }

        public void setAgeDto(AgeDto ageDto) {
            this.ageDto = ageDto;
        }

        public LangFieldObject getDisplayName() {
            return displayName;
        }

        public void setDisplayName(LangFieldObject displayName) {
            this.displayName = displayName;
        }
    }
}
