package com.dean.baby.api.controller;

import com.dean.baby.common.dto.BabyCreateRequestVo;
import com.dean.baby.common.dto.BabyDto;
import com.dean.baby.common.dto.CompleteScheduleRequest;
import com.dean.baby.common.entity.BabyVaccineSchedule;
import com.dean.baby.common.service.BabyService;
import com.dean.baby.common.service.BabyVaccineScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/baby")
@RestController
public class BabyController {

    private final BabyService babyService;
    private final BabyVaccineScheduleService scheduleService;

    public BabyController(BabyService babyService, BabyVaccineScheduleService scheduleService) {
        this.babyService = babyService;
        this.scheduleService = scheduleService;
    }

    @GetMapping
    public List<BabyDto> listBabies() {
        return babyService.listBabies();
    }

    @PostMapping
    public BabyDto createOrUpdateBaby(@RequestBody BabyCreateRequestVo vo) {
        return babyService.createOrUpdateBaby(vo);
    }

    @GetMapping("/{babyId}/schedules")
    public List<BabyVaccineSchedule> getSchedules(@PathVariable UUID babyId) {
        return scheduleService.findSchedulesByBaby(babyId);
    }

    @PostMapping("/{babyId}/schedules/generate")
    public List<BabyVaccineSchedule> generateSchedules(@PathVariable UUID babyId) {
        return scheduleService.generateDefaultScheduleForBaby(babyId, true);
    }

    @PostMapping("/complete-schedule")
    public BabyVaccineSchedule updateSchedule(@RequestBody CompleteScheduleRequest request) {
        return scheduleService.completeSchedule(request.scheduleId(),request.actualDate());
    }
}
