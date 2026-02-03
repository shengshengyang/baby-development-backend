package com.dean.baby.common.service;

import com.dean.baby.common.dto.common.LangFieldObject;
import com.dean.baby.common.entity.Category;
import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
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
    public Category create(LangFieldObject nameObject) {
        Category category = new Category();
        if (nameObject == null) {
            nameObject = new LangFieldObject();
        }
        // 以目前語系的值填滿其餘為空的欄位，避免空字串
        nameObject.setDefaultBeforeInsert();
        category.setName(nameObject);
        return categoryRepository.save(category);
    }

    @Transactional
    public Category update(UUID id, LangFieldObject nameObject) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new ApiException(SysCode.CATEGORY_NOT_FOUND, "Category not found with id: " + id));

        if (nameObject == null) {
            nameObject = new LangFieldObject();
        }
        existing.setName(nameObject);
        return categoryRepository.save(existing);
    }

    @Transactional
    public void delete(UUID id) {
        categoryRepository.deleteById(id);
    }
}
