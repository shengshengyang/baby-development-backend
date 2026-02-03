package com.dean.baby.api.controller;

import com.dean.baby.common.dto.BabyCreateRequestVo;
import com.dean.baby.common.dto.BabyDto;
import com.dean.baby.common.dto.CompleteScheduleRequest;
import com.dean.baby.common.entity.BabyVaccineSchedule;
import com.dean.baby.common.service.BabyService;
import com.dean.baby.common.service.BabyVaccineScheduleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RequestMapping("/api/baby")
@RestController
@Tag(name = "寶寶管理", description = "寶寶資訊與疫苗接種管理 API")
public class BabyController {

    private final BabyService babyService;
    private final BabyVaccineScheduleService scheduleService;

    public BabyController(BabyService babyService, BabyVaccineScheduleService scheduleService) {
        this.babyService = babyService;
        this.scheduleService = scheduleService;
    }

    @Operation(summary = "獲取寶寶列表", description = "獲取當前用戶的所有寶寶資訊")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "獲取成功")
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BabyDto>> listBabies() {
        return ResponseEntity.ok(babyService.listBabies());
    }

    @Operation(summary = "創建或更新寶寶", description = "創建新的寶寶資訊或更新已有寶寶資訊")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "操作成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "請求參數錯誤")
    })
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BabyDto> createOrUpdateBaby(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "寶寶資訊") @RequestBody BabyCreateRequestVo vo) {
        return ResponseEntity.ok(babyService.createOrUpdateBaby(vo));
    }

    @Operation(summary = "獲取疫苗接種計劃", description = "獲取指定寶寶的疫苗接種計劃列表")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "獲取成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "寶寶不存在")
    })
    @GetMapping(value = "/{babyId}/schedules", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BabyVaccineSchedule>> getSchedules(
            @Parameter(description = "寶寶 ID") @PathVariable UUID babyId) {
        return ResponseEntity.ok(scheduleService.findSchedulesByBaby(babyId));
    }

    @Operation(summary = "生成疫苗接種計劃", description = "為指定寶寶生成預設的疫苗接種計劃")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "生成成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "寶寶不存在")
    })
    @PostMapping(value = "/{babyId}/schedules/generate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<BabyVaccineSchedule>> generateSchedules(
            @Parameter(description = "寶寶 ID") @PathVariable UUID babyId) {
        return ResponseEntity.ok(scheduleService.generateDefaultScheduleForBaby(babyId, true));
    }

    @Operation(summary = "完成疫苗接種", description = "標記指定的疫苗接種計劃為已完成")
    @ApiResponses({
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "更新成功"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "請求參數錯誤"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "接種計劃不存在")
    })
    @PostMapping(value = "/complete-schedule", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BabyVaccineSchedule> updateSchedule(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "完成接種請求") @RequestBody CompleteScheduleRequest request) {
        return ResponseEntity.ok(scheduleService.completeSchedule(request.scheduleId(), request.actualDate()));
    }
}
