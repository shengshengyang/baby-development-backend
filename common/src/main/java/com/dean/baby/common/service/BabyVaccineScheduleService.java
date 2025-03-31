package com.dean.baby.common.service;

import com.dean.baby.common.dto.enums.VaccineStatus;
import com.dean.baby.common.entity.Baby;
import com.dean.baby.common.entity.BabyVaccineSchedule;
import com.dean.baby.common.entity.Vaccine;
import com.dean.baby.common.repository.BabyRepository;
import com.dean.baby.common.repository.BabyVaccineScheduleRepository;
import com.dean.baby.common.repository.UserRepository;
import com.dean.baby.common.repository.VaccineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 服務：負責管理排程紀錄(BabyVaccineSchedule)的各種邏輯：
 *   - 產生預排接種計劃
 *   - 查詢寶寶的排程
 *   - 更新實際施打/延期
 *   - ...
 */
@Service
@Transactional
public class BabyVaccineScheduleService extends BaseService{

    private final BabyVaccineScheduleRepository scheduleRepository;
    private final BabyRepository babyRepository;
    private final VaccineRepository vaccineRepository;

    public BabyVaccineScheduleService(BabyVaccineScheduleRepository scheduleRepository,
                                      BabyRepository babyRepository,
                                      VaccineRepository vaccineRepository,
                                      UserRepository userRepository) {
        super(userRepository);
        this.scheduleRepository = scheduleRepository;
        this.babyRepository = babyRepository;
        this.vaccineRepository = vaccineRepository;
    }

    /**
     * 依 babyId 查詢該寶寶的接種排程
     */
    @Transactional(readOnly = true)
    public List<BabyVaccineSchedule> findSchedulesByBaby(Long babyId) {
        isCurrentUserBabyOwner(babyId);
        return scheduleRepository.findByBabyId(babyId);
    }

    /**
     * 依 babyId + vaccineId 查詢
     */
    @Transactional(readOnly = true)
    public List<BabyVaccineSchedule> findSchedulesByBabyAndVaccine(Long babyId, Long vaccineId) {
        return scheduleRepository.findByBabyIdAndVaccineId(babyId, vaccineId);
    }

    /**
     * 儲存(新增或更新) BabyVaccineSchedule
     */
    public BabyVaccineSchedule saveSchedule(BabyVaccineSchedule schedule) {
        return scheduleRepository.save(schedule);
    }

    /**
     * 產生預排接種計畫
     * （示範：對所有 Vaccine 或特定 Vaccine 自動生成）
     *
     * @param babyId 指定寶寶 ID
     * @param allVaccines 是否對所有疫苗產生 (true: 對 vaccine 全部生成)
     */
    public List<BabyVaccineSchedule> generateDefaultScheduleForBaby(Long babyId, boolean allVaccines) {
        isCurrentUserBabyOwner(babyId);
        // 1) 找到寶寶資料
        Baby baby = babyRepository.findById(babyId)
                .orElseThrow(() -> new RuntimeException("Baby not found"));

        // 2) 如果 allVaccines=true, 則把全部 Vaccine 撈出來；否則可依需求篩選
        List<Vaccine> vaccines = vaccineRepository.findAll();

        List<BabyVaccineSchedule> newSchedules = new ArrayList<>();

        for (Vaccine vaccine : vaccines) {
            // 解析 dose_intervals_json，如 [2,4,6] ...
            List<Integer> intervals = parseDoseIntervalsJson(vaccine.getDoseIntervalsJson());
            if (intervals == null || intervals.isEmpty()) {
                continue;
            }

            // 依照每個間隔月數產生對應 doseNumber
            // 例如 [2,4,6] => 第1劑(2個月), 第2劑(4個月), 第3劑(6個月)
            for (int i = 0; i < intervals.size(); i++) {
                Integer monthOffset = intervals.get(i);
                int doseNumber = i + 1;  // 第幾劑

                LocalDate scheduledDate = baby.getBirthDate().plusMonths(monthOffset);
                LocalDate reminderDate = scheduledDate.minusDays(14); // 範例：提前 14 天提醒

                BabyVaccineSchedule schedule = new BabyVaccineSchedule();
                schedule.setBaby(baby);
                schedule.setVaccine(vaccine);
                schedule.setDoseNumber(doseNumber);
                schedule.setScheduledDate(scheduledDate);
                schedule.setReminderDate(reminderDate);
                schedule.setStatus(VaccineStatus.SCHEDULED);

                newSchedules.add(schedule);
            }
        }
        // 3) 存到 DB
        return scheduleRepository.saveAll(newSchedules);
    }

    /**
     * 更新某一筆排程為「已施打」, 並填入實際施打時間
     */
    public BabyVaccineSchedule completeSchedule(Long scheduleId, LocalDate actualDate) {
        BabyVaccineSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        schedule.setActualDate(actualDate);
        schedule.setStatus(VaccineStatus.COMPLETED);
        return scheduleRepository.save(schedule);
    }

    /**
     * 更新某一筆排程為「延遲(DELAYED)」並重新安排日期
     */
    public BabyVaccineSchedule delayAndReschedule(Long scheduleId, LocalDate newDate) {
        BabyVaccineSchedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        schedule.setStatus(VaccineStatus.DELAYED);
        schedule.setRescheduledDate(newDate);
        // 如果你也想同步更新 scheduledDate，就加上:
        schedule.setScheduledDate(newDate);
        return scheduleRepository.save(schedule);
    }

    /**
     * @param doseIntervalsJson 格式如 "[2,4,6]"
     * @return List<Integer> [2,4,6]
     */
    private List<Integer> parseDoseIntervalsJson(String doseIntervalsJson) {
        if (doseIntervalsJson == null || doseIntervalsJson.isBlank()) {
            return Collections.emptyList();
        }
        // 假設格式大概是: [2,4,6]，把逗號與中括號過濾
        // ※實務中可用 JSON 解析，如 Jackson / Gson
        String clean = doseIntervalsJson
                .replace("[", "")
                .replace("]", "");
        return Arrays.stream(clean.split(","))
                .map(String::trim)
                .map(Integer::parseInt)
                .collect(Collectors.toList());
    }
}
