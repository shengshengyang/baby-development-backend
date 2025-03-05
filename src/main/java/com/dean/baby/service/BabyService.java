package com.dean.baby.service;

import com.dean.baby.dto.BabyCreateRequestVo;
import com.dean.baby.dto.BabyDto;
import com.dean.baby.entity.Baby;
import com.dean.baby.repository.BabyRepository;
import com.dean.baby.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class BabyService extends BaseService {

    private final BabyRepository babyRepository;

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
