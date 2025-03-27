package com.dean.baby.mvc.controller;

import com.dean.baby.common.dto.BabyCreateRequestVo;
import com.dean.baby.common.dto.BabyDto;
import com.dean.baby.common.service.BabyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/baby")
public class BabyController {

    BabyService babyService;

    public BabyController(BabyService babyService) {
        this.babyService = babyService;
    }

    @GetMapping
    public List<BabyDto> getBabies() {
        return babyService.listBabiesFromUser();
    }

    @PostMapping
    public BabyDto addBaby(@RequestBody BabyCreateRequestVo vo) {
        return babyService.createOrUpdateBaby(vo);
    }

}
