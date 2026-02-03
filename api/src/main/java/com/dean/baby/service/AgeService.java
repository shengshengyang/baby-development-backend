package com.dean.baby.common.service;

import com.dean.baby.common.dto.AgeDto;
import com.dean.baby.common.dto.common.LangFieldObject;
import com.dean.baby.common.entity.Age;
import com.dean.baby.common.repository.AgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class AgeService {

    private final AgeRepository ageRepository;

    @Autowired
    public AgeService(AgeRepository ageRepository) {
        this.ageRepository = ageRepository;
    }

    public List<Age> findAll() {
        return ageRepository.findAll();
    }

    public Optional<Age> findById(UUID id) {
        return ageRepository.findById(id);
    }

    public Age create(AgeDto ageDto, LangFieldObject displayName) {
        Optional<Age> existingAge = ageRepository.findByMonth(ageDto.getMonth());
        if (existingAge.isPresent()) {
            throw new IllegalArgumentException("Month " + ageDto.getMonth() + " already exists!");
        }

        Age age = new Age();
        age.setMonth(ageDto.getMonth());
        age.setDisplayName(displayName);
        return ageRepository.save(age);
    }

    public Age update(UUID id, AgeDto ageDto, LangFieldObject displayName) {
        Age age = ageRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Age not found!"));

        Optional<Age> existingAge = ageRepository.findByMonth(ageDto.getMonth());
        if (existingAge.isPresent() && !existingAge.get().getId().equals(id)) {
            throw new IllegalArgumentException("Month " + ageDto.getMonth() + " already used by another record!");
        }

        age.setMonth(ageDto.getMonth());
        if (displayName != null) {
            age.setDisplayName(displayName);
        }
        return ageRepository.save(age);
    }

    public void delete(UUID id) {
        if (!ageRepository.existsById(id)) {
            throw new IllegalArgumentException("Age not found!");
        }
        ageRepository.deleteById(id);
    }
}
