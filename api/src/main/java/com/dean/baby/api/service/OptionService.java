package com.dean.baby.api.service;

import com.dean.baby.api.dto.OptionItem;
import com.dean.baby.common.entity.Age;
import com.dean.baby.common.repository.AgeRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class OptionService {

    private final AgeRepository ageRepository;

    public OptionService(AgeRepository ageRepository) {
        this.ageRepository = ageRepository;
    }

    /**
     * 列出前端用的年齡選項（以月排序）
     */
    public List<OptionItem> getAgeOptions() {
        return ageRepository.findAll().stream()
                .sorted(Comparator.comparingInt(Age::getMonth))
                .map(age -> new OptionItem(
                        age.getDisplayName() != null ? age.getDisplayName().getLangByLocaleName() : String.valueOf(age.getMonth()),
                        age.getId().toString()
                ))
                .toList();
    }
}
