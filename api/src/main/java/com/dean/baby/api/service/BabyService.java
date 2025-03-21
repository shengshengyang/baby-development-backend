package com.dean.baby.api.service;

import com.dean.baby.common.dto.BabyCreateRequestVo;
import com.dean.baby.common.dto.BabyDto;
import com.dean.baby.common.entity.Baby;
import com.dean.baby.common.repository.BabyRepository;
import com.dean.baby.common.repository.UserRepository;
import com.dean.baby.common.service.BaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

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
    public BabyDto createOrUpdateBaby(BabyCreateRequestVo vo) {
        Baby baby = babyRepository.findById(vo.id()).orElse(Baby.builder()
                .name(vo.name())
                .birthDate(LocalDate.parse(vo.birthDate()))
                .user(getCurrentUser())
                .build());
        return BabyDto.fromEntity(babyRepository.save(baby));
    }
}
