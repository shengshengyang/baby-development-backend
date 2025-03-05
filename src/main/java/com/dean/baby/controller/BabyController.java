package com.dean.baby.controller;

import com.dean.baby.dto.BabyCreateRequestVo;
import com.dean.baby.dto.BabyDto;
import com.dean.baby.service.BabyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/baby")
@RestController
public class BabyController {

    private final BabyService babyService;

    public BabyController(BabyService babyService) {
        this.babyService = babyService;
    }

    @GetMapping
    public List<BabyDto> listBabies() {
        return babyService.listBabies();
    }

    @PostMapping
    public BabyDto createOrUpdateBaby(BabyCreateRequestVo vo) {
        return babyService.createOrUpdateBaby(vo);
    }
}
