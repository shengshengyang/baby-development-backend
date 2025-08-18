package com.dean.baby.common.service;

import com.dean.baby.common.dto.BabyCreateRequestVo;
import com.dean.baby.common.dto.BabyDto;
import com.dean.baby.common.entity.Baby;
import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import com.dean.baby.common.repository.BabyRepository;
import com.dean.baby.common.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
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
        Baby baby = vo.id() == null ? new Baby() : babyRepository.findById(vo.id()).orElseThrow();
        baby.setName(vo.name());
        baby.setBirthDate(LocalDate.parse(vo.birthDate()));
        baby.setUser(getCurrentUser());
        return BabyDto.fromEntity(babyRepository.save(baby));
    }
}
