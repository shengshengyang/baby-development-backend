package com.dean.baby.api.controller;

import com.dean.baby.common.dto.BabyCreateRequestVo;
import com.dean.baby.common.dto.BabyDto;
import com.dean.baby.common.service.BabyService;
import org.springframework.web.bind.annotation.*;

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
    public BabyDto createOrUpdateBaby(@RequestBody BabyCreateRequestVo vo) {
        return babyService.createOrUpdateBaby(vo);
    }
}
