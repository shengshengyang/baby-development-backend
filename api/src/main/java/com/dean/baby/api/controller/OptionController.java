package com.dean.baby.api.controller;

import com.dean.baby.common.dto.common.StringOptionItem;
import com.dean.baby.common.service.OptionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
@Tag(name = "選項管理", description = "系統選項與下拉選單數據 API")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @Operation(summary = "獲取年齡段選項", description = "獲取所有年齡段的選項列表，用於下拉選單")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "獲取成功")
    })
    @GetMapping(value = "/options/ages", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StringOptionItem>> getAgeOptions() {
        return ResponseEntity.ok(optionService.getAgeOptions());
    }

    @Operation(summary = "獲取分類選項", description = "獲取所有分類的選項列表，用於下拉選單")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "獲取成功")
    })
    @GetMapping(value = "/options/categories", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<StringOptionItem>> getCategoryOptions() {
        return ResponseEntity.ok(optionService.getCategoryOptions());
    }
}

