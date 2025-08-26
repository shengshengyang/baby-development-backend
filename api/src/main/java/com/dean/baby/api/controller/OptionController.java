package com.dean.baby.api.controller;

import com.dean.baby.api.dto.OptionItem;
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

    // 受保護路徑（目前全開放），給前端取用的年齡選項
    @GetMapping("/options/ages")
    public ResponseEntity<List<OptionItem>> getAgeOptions() {
        return ResponseEntity.ok(optionService.getAgeOptions());
    }
}
