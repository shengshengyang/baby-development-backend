package com.dean.baby.common.service;

import com.dean.baby.common.dto.MilestoneDTO;
import com.dean.baby.common.dto.MilestoneTranslationDTO;
import com.dean.baby.common.entity.Category;
import com.dean.baby.common.entity.Milestone;
import com.dean.baby.common.entity.MilestoneTranslation;
import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import com.dean.baby.common.repository.CategoryRepository;
import com.dean.baby.common.repository.MilestoneRepository;
import com.dean.baby.common.repository.MilestoneTranslationRepository;
import com.dean.baby.common.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.dean.baby.common.dto.enums.Language;

@Service
public class MilestoneService extends BaseService {

    private final MilestoneRepository milestoneRepository;
    private final MilestoneTranslationRepository milestoneTranslationRepository;
    private final CategoryRepository categoryRepository;

    public MilestoneService(UserRepository userRepository,
                           MilestoneRepository milestoneRepository,
                           MilestoneTranslationRepository milestoneTranslationRepository,
                           CategoryRepository categoryRepository) {
        super(userRepository);
        this.milestoneRepository = milestoneRepository;
        this.milestoneTranslationRepository = milestoneTranslationRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public MilestoneDTO createMilestone(MilestoneDTO milestoneDTO) {
        // 验证类别是否存在
        Category category = categoryRepository.findById(milestoneDTO.getCategory().getId())
                .orElseThrow(() -> new ApiException(SysCode.CATEGORY_NOT_FOUND));

        // 创建 Milestone Entity
        Milestone milestone = new Milestone();
        milestone.setAgeInMonths(milestoneDTO.getAgeInMonths());
        milestone.setCategory(category);

        // 保存 Milestone
        Milestone savedMilestone = milestoneRepository.save(milestone);

        // 创建翻译
        if (milestoneDTO.getTranslations() != null && !milestoneDTO.getTranslations().isEmpty()) {
            List<MilestoneTranslation> translations = milestoneDTO.getTranslations().stream()
                    .map(dto -> {
                        MilestoneTranslation translation = new MilestoneTranslation();
                        translation.setMilestone(savedMilestone);
                        translation.setLanguageCode(Language.fromCode(dto.getLanguageCode()));
                        translation.setDescription(dto.getDescription());
                        return translation;
                    })
                    .toList();

            milestoneTranslationRepository.saveAll(translations);
            savedMilestone.setTranslations(translations);
        }

        return MilestoneDTO.fromEntity(savedMilestone);
    }

    public Optional<MilestoneDTO> getMilestoneById(UUID id) {
        return milestoneRepository.findById(id)
                .map(MilestoneDTO::fromEntity);
    }

    public List<MilestoneDTO> getAllMilestones() {
        return milestoneRepository.findAll().stream()
                .map(MilestoneDTO::fromEntity)
                .toList();
    }

    public List<MilestoneTranslationDTO> getMilestonesByAgeAndLanguage(int age, String language) {
        Language lang = Language.fromCode(language);
        return milestoneTranslationRepository.findByAgeAndLanguage(age, lang).stream()
                .map(MilestoneTranslationDTO::fromEntity)
                .toList();
    }

    @Transactional
    public MilestoneDTO updateMilestone(UUID id, MilestoneDTO milestoneDTO) {
        // 查找现有的 Milestone
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new ApiException(SysCode.MILESTONE_NOT_FOUND));

        // 验证类别是否存在
        Category category = categoryRepository.findById(milestoneDTO.getCategory().getId())
                .orElseThrow(() -> new ApiException(SysCode.CATEGORY_NOT_FOUND));

        // 更新基本属性
        milestone.setAgeInMonths(milestoneDTO.getAgeInMonths());
        milestone.setCategory(category);

        // 删除旧的翻译
        if (milestone.getTranslations() != null) {
            milestoneTranslationRepository.deleteAll(milestone.getTranslations());
        }

        // 创建新的翻译
        if (milestoneDTO.getTranslations() != null && !milestoneDTO.getTranslations().isEmpty()) {
            List<MilestoneTranslation> newTranslations = milestoneDTO.getTranslations().stream()
                    .map(dto -> {
                        MilestoneTranslation translation = new MilestoneTranslation();
                        translation.setMilestone(milestone);
                        translation.setLanguageCode(Language.fromCode(dto.getLanguageCode()));
                        translation.setDescription(dto.getDescription());
                        return translation;
                    })
                    .toList();

            milestoneTranslationRepository.saveAll(newTranslations);
            milestone.setTranslations(newTranslations);
        }

        // 保存并返回 DTO
        Milestone updatedMilestone = milestoneRepository.save(milestone);
        return MilestoneDTO.fromEntity(updatedMilestone);
    }

    @Transactional
    public void deleteMilestone(UUID id) {
        // 查找 Milestone
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new ApiException(SysCode.MILESTONE_NOT_FOUND));

        // 删除相关翻译
        if (milestone.getTranslations() != null) {
            milestoneTranslationRepository.deleteAll(milestone.getTranslations());
        }

        // 删除 Milestone
        milestoneRepository.delete(milestone);
    }
}
