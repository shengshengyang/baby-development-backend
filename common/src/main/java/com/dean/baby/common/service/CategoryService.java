package com.dean.baby.common.service;

import com.dean.baby.common.dto.common.LangFieldObject;
import com.dean.baby.common.entity.Category;
import com.dean.baby.common.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> findById(UUID id) {
        return categoryRepository.findById(id);
    }

    @Transactional
    public Category create(String tw, String en) {
        Category category = new Category();
        LangFieldObject name = new LangFieldObject();
        if (tw != null) name.setTw(tw);
        if (en != null) name.setEn(en);
        // 以目前語系的值填滿其餘為空的欄位，避免空字串
        name.setDefaultBeforeInsert();
        category.setName(name);
        return categoryRepository.save(category);
    }

    @Transactional
    public Optional<Category> update(UUID id, String tw, String en) {
        return categoryRepository.findById(id).map(existing -> {
            LangFieldObject name = existing.getName();
            if (name == null) {
                name = new LangFieldObject();
            }
            if (tw != null) name.setTw(tw);
            if (en != null) name.setEn(en);
            existing.setName(name);
            return categoryRepository.save(existing);
        });
    }

    @Transactional
    public void delete(UUID id) {
        categoryRepository.deleteById(id);
    }
}

