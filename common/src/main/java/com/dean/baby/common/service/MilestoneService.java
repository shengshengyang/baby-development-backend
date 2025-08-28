package com.dean.baby.common.service;

import com.dean.baby.common.dto.AgeDto;
import com.dean.baby.common.dto.CategoryDTO;
import com.dean.baby.common.dto.MilestoneDTO;
import com.dean.baby.common.entity.Age;
import com.dean.baby.common.entity.Category;
import com.dean.baby.common.entity.Milestone;
import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import com.dean.baby.common.repository.AgeRepository;
import com.dean.baby.common.repository.CategoryRepository;
import com.dean.baby.common.repository.MilestoneRepository;
import com.dean.baby.common.repository.UserRepository;
import com.dean.baby.common.util.LanguageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.dean.baby.common.dto.enums.Language;
import com.dean.baby.common.dto.common.LangFieldObject;

@Service
public class MilestoneService extends BaseService {

    private final MilestoneRepository milestoneRepository;
    private final CategoryRepository categoryRepository;
    private final AgeRepository ageRepository;

    public MilestoneService(UserRepository userRepository,
                           MilestoneRepository milestoneRepository,
                           CategoryRepository categoryRepository,
                           AgeRepository ageRepository) {
        super(userRepository);
        this.milestoneRepository = milestoneRepository;
        this.categoryRepository = categoryRepository;
        this.ageRepository = ageRepository;
    }

    @Transactional
    public MilestoneDTO createMilestone(MilestoneDTO milestoneDTO) {
        Category category = findCategoryById(milestoneDTO.getCategory().getId());
        Age age = findOrCreateAge(milestoneDTO.getAge().getMonth());

        Milestone milestone = new Milestone();
        milestone.setAge(age);
        milestone.setCategory(category);
        milestone.setDescription(buildDescriptionObject(milestoneDTO));
        milestone.setVideoUrl(milestoneDTO.getVideoUrl());
        milestone.setImageBase64(milestoneDTO.getImageBase64());

        Milestone saved = milestoneRepository.save(milestone);
        return MilestoneDTO.fromEntity(saved);
    }

    public Optional<MilestoneDTO> getMilestoneById(UUID id) {
        return milestoneRepository.findById(id).map(MilestoneDTO::fromEntity);
    }

    public List<MilestoneDTO> getAllMilestones() {
        return milestoneRepository.findAll().stream()
                .map(MilestoneDTO::fromEntity)
                .toList();
    }

    /**
     * 根據年齡月份查找里程碑（向後兼容）
     */
    public List<MilestoneDTO> getMilestonesByAgeAndLanguage(int ageMonth, String languageCode) {
        Language language = Language.fromCode(languageCode);
        if (language == null) {
            return List.of();
        }

        return milestoneRepository.findByAgeMonth(ageMonth).stream()
                .map(milestone -> buildMilestoneDTOWithLanguage(milestone, language))
                .toList();
    }

    /**
     * 根據年齡 UUID 查找里程碑，使用當前語言環境
     */
    public List<MilestoneDTO> getMilestonesByAgeId(UUID ageId) {
        Language currentLanguage = LanguageUtil.getLanguageFromLocale();

        return milestoneRepository.findByAgeId(ageId).stream()
                .map(milestone -> buildMilestoneDTOWithLanguage(milestone, currentLanguage))
                .toList();
    }

    @Transactional
    public MilestoneDTO updateMilestone(UUID id, MilestoneDTO milestoneDTO) {
        Milestone milestone = findMilestoneById(id);
        Category category = findCategoryById(milestoneDTO.getCategory().getId());
        Age age = findOrCreateAge(milestoneDTO.getAge().getMonth());

        milestone.setAge(age);
        milestone.setCategory(category);
        milestone.setDescription(updateDescriptionObject(milestone.getDescription(), milestoneDTO));
        milestone.setVideoUrl(milestoneDTO.getVideoUrl());
        milestone.setImageBase64(milestoneDTO.getImageBase64());

        Milestone updated = milestoneRepository.save(milestone);
        return MilestoneDTO.fromEntity(updated);
    }

    @Transactional
    public void deleteMilestone(UUID id) {
        Milestone milestone = findMilestoneById(id);
        milestoneRepository.delete(milestone);
    }

    // === 私有輔��方法 ===

    private Category findCategoryById(UUID categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException(SysCode.CATEGORY_NOT_FOUND));
    }

    private Milestone findMilestoneById(UUID milestoneId) {
        return milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ApiException(SysCode.MILESTONE_NOT_FOUND));
    }

    private Age findOrCreateAge(int month) {
        return ageRepository.findByMonth(month)
                .orElseGet(() -> createAgeIfNotExists(month));
    }

    /**
     * 構建里程碑描述對象
     */
    private LangFieldObject buildDescriptionObject(MilestoneDTO milestoneDTO) {
        LangFieldObject desc = milestoneDTO.getDescriptionObject();
        if (desc == null) {
            desc = new LangFieldObject();
            if (milestoneDTO.getDescription() != null) {
                desc.setLangByLocaleName(milestoneDTO.getDescription());
            }
            desc.setDefaultBeforeInsert("");
        }
        return desc;
    }

    /**
     * 更新里程碑描述對象
     */
    private LangFieldObject updateDescriptionObject(LangFieldObject existingDesc, MilestoneDTO milestoneDTO) {
        LangFieldObject desc = milestoneDTO.getDescriptionObject();
        if (desc == null) {
            desc = existingDesc != null ? existingDesc : new LangFieldObject();
            if (milestoneDTO.getDescription() != null) {
                desc.setLangByLocaleName(milestoneDTO.getDescription());
            }
            desc.setDefaultBeforeInsert("");
        }
        return desc;
    }

    /**
     * 構建帶有特定語言的里程碑 DTO
     */
    private MilestoneDTO buildMilestoneDTOWithLanguage(Milestone milestone, Language language) {
        String description = milestone.getDescription() != null
                ? milestone.getDescription().getLang(language.getCode())
                : "";

        return MilestoneDTO.builder()
                .id(milestone.getId())
                .age(AgeDto.fromEntity(milestone.getAge()))
                .description(description != null ? description : "")
                .category(CategoryDTO.fromEntity(milestone.getCategory()))
                .videoUrl(milestone.getVideoUrl())
                .imageBase64(milestone.getImageBase64())
                .build();
    }

    /**
     * 創建新的年齡記錄
     */
    private Age createAgeIfNotExists(int month) {
        Age age = new Age();
        age.setMonth(month);
        age.setDisplayName(createMultiLanguageDisplayName(month));
        return ageRepository.save(age);
    }

    /**
     * 創建多語言顯示名稱
     */
    private LangFieldObject createMultiLanguageDisplayName(int month) {
        LangFieldObject displayName = new LangFieldObject();
        displayName.setLang(Language.ENGLISH.getCode(), month + " Months");
        displayName.setLang(Language.TRADITIONAL_CHINESE.getCode(), month + "個月");
        displayName.setLang(Language.SIMPLIFIED_CHINESE.getCode(), month + "个月");
        displayName.setLang(Language.JAPANESE.getCode(), month + "ヶ月");
        displayName.setLang(Language.KOREAN.getCode(), month + "개월");
        displayName.setLang(Language.VIETNAMESE.getCode(), month + " tháng");
        return displayName;
    }
}
