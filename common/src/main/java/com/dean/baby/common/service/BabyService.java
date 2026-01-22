package com.dean.baby.common.service;

import com.dean.baby.common.dto.BabyCreateRequestVo;
import com.dean.baby.common.dto.BabyDto;
import com.dean.baby.common.entity.Baby;
import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import com.dean.baby.common.repository.BabyRepository;
import com.dean.baby.common.repository.UserRepository;
import com.dean.baby.common.util.ChangeLogUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class BabyService extends BaseService {

    private final BabyRepository babyRepository;

    @Autowired
    protected BabyService(UserRepository userRepository, BabyRepository babyRepository) {
        super(userRepository);
        this.babyRepository = babyRepository;
    }

    @Transactional(readOnly = true)
    public List<BabyDto> listBabies() {
        return babyRepository.findByUserId(getCurrentUser().getId()).stream()
                .map(BabyDto::fromEntity)
                .toList();
    }

    @Transactional
    public BabyDto getBaby(UUID id) {
        return babyRepository.findById(id)
                .map(BabyDto::fromEntity)
                .orElseThrow(() -> new ApiException(SysCode.BABY_NOT_FOUND));
    }

    @Transactional(readOnly = true)
    public List<BabyDto> listBabiesFromUser() {
        return babyRepository.findByUserId(getCurrentUser().getId()).stream()
                .map(BabyDto::fromEntity)
                .toList();
    }



    @Transactional
    public BabyDto createOrUpdateBaby(BabyCreateRequestVo vo) {
        boolean isUpdate = vo.id() != null;
        Baby baby;

        if (isUpdate) {
            // 更新現有寶寶資料
            baby = babyRepository.findById(vo.id()).orElseThrow(
                () -> new ApiException(SysCode.BABY_NOT_FOUND)
            );

            // 記錄更新前的資料
            BabyDto oldData = BabyDto.fromEntity(baby);

            // 更新資料
            baby.setName(vo.name());
            baby.setBirthDate(LocalDate.parse(vo.birthDate()));

            // 儲存並記錄異動
            Baby savedBaby = babyRepository.save(baby);
            BabyDto newData = BabyDto.fromEntity(savedBaby);

            ChangeLogUtil.logUpdate("Baby", savedBaby.getId(), oldData, newData);
            log.info("Baby updated: id={}, name={}, birthDate={}",
                savedBaby.getId(), savedBaby.getName(), savedBaby.getBirthDate());

            return newData;

        } else {
            // 建立新寶寶
            baby = new Baby();
            baby.setName(vo.name());
            baby.setBirthDate(LocalDate.parse(vo.birthDate()));
            baby.setUser(getCurrentUser());

            // 儲存並記錄異動
            Baby savedBaby = babyRepository.save(baby);
            BabyDto result = BabyDto.fromEntity(savedBaby);

            ChangeLogUtil.logCreate("Baby", savedBaby.getId(), result);
            log.info("Baby created: id={}, name={}, birthDate={}",
                savedBaby.getId(), savedBaby.getName(), savedBaby.getBirthDate());

            return result;
        }
    }
}
