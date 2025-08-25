package com.dean.baby.common.service;

import com.dean.baby.common.dto.MilestoneDTO;
import com.dean.baby.common.dto.MilestoneTranslationDTO;
import com.dean.baby.common.entity.Age;
import com.dean.baby.common.entity.Category;
import com.dean.baby.common.entity.Milestone;
import com.dean.baby.common.entity.MilestoneTranslation;
import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import com.dean.baby.common.repository.AgeRepository;
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
    private final AgeRepository ageRepository;

    public MilestoneService(UserRepository userRepository,
                           MilestoneRepository milestoneRepository,
                           MilestoneTranslationRepository milestoneTranslationRepository,
                           CategoryRepository categoryRepository,
                           AgeRepository ageRepository) {
        super(userRepository);
        this.milestoneRepository = milestoneRepository;
        this.milestoneTranslationRepository = milestoneTranslationRepository;
        this.categoryRepository = categoryRepository;
        this.ageRepository = ageRepository;
    }

    @Transactional
    public MilestoneDTO createMilestone(MilestoneDTO milestoneDTO) {
        // 验证类别是否存在
        Category category = categoryRepository.findById(milestoneDTO.getCategory().getId())
                .orElseThrow(() -> new ApiException(SysCode.CATEGORY_NOT_FOUND));

        // 查找或创建Age实体
        Age age = ageRepository.findByMonth(milestoneDTO.getAgeInMonths())
                .orElseGet(() -> createAgeIfNotExists(milestoneDTO.getAgeInMonths()));

        // 创建 Milestone Entity
        Milestone milestone = new Milestone();
        milestone.setAge(age);
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

        // 查找或创建Age实体
        Age age = ageRepository.findByMonth(milestoneDTO.getAgeInMonths())
                .orElseGet(() -> createAgeIfNotExists(milestoneDTO.getAgeInMonths()));

        // 更新基本属性
        milestone.setAge(age);
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

    private Age createAgeIfNotExists(int month) {
        Age age = new Age();
        age.setMonth(month);

        // 创建默认的多语言显示名称
        com.dean.baby.common.dto.common.LangFieldObject displayName = new com.dean.baby.common.dto.common.LangFieldObject();
        displayName.setEn(month + " Months");
        displayName.setTw(month + "個月");
        displayName.setCn(month + "个月");
        displayName.setJa(month + "ヶ月");
        displayName.setKo(month + "개월");
        displayName.setVi(month + " tháng");

        age.setDisplayName(displayName);
        return ageRepository.save(age);
    }
}
