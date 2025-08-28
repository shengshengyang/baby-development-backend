package com.dean.baby.common.service;

import com.dean.baby.common.dto.common.StringOptionItem;
import com.dean.baby.common.entity.Age;
import com.dean.baby.common.repository.AgeRepository;
import com.dean.baby.common.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class OptionService {

    private final AgeRepository ageRepository;
    private final CategoryRepository categoryRepository;

    public OptionService(AgeRepository ageRepository, CategoryRepository categoryRepository) {
        this.ageRepository = ageRepository;
        this.categoryRepository = categoryRepository;
    }

    /**
     * 列出前端用的年齡選項（以月排序）
     */
    public List<StringOptionItem> getAgeOptions() {
        return ageRepository.findAll().stream()
                .sorted(Comparator.comparingInt(Age::getMonth))
                .map(age -> new StringOptionItem(
                        age.getDisplayName() != null ? age.getDisplayName().getLangByLocaleName() : String.valueOf(age.getMonth()),
                        age.getId().toString()
                ))
                .toList();
    }

    /**
     * 列出前端用的分類選項
     */
    public List<StringOptionItem> getCategoryOptions() {
        return categoryRepository.findAll().stream()
                .map(category -> new StringOptionItem(
                        category.getName() != null ? category.getName().getLangByLocaleName() : "未命名分類",
                        category.getId().toString()
                ))
                .toList();
    }
}
