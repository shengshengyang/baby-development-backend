package com.dean.baby.api.controller;

import com.dean.baby.api.dto.StringOptionItem;
import com.dean.baby.api.service.OptionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OptionController {

    private final OptionService optionService;

    public OptionController(OptionService optionService) {
        this.optionService = optionService;
    }

    @GetMapping("/options/ages")
    public ResponseEntity<List<StringOptionItem>> getAgeOptions() {
        return ResponseEntity.ok(optionService.getAgeOptions());
    }

    @GetMapping("/options/categories")
    public ResponseEntity<List<StringOptionItem>> getCategoryOptions() {
        return ResponseEntity.ok(optionService.getCategoryOptions());
    }
}

