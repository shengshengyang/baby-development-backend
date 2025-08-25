package com.dean.baby.common.service;

import com.dean.baby.common.dto.MilestoneDTO;
import com.dean.baby.common.dto.MilestoneTranslationDTO;
import com.dean.baby.common.entity.Age;
import com.dean.baby.common.entity.Category;
import com.dean.baby.common.entity.Milestone;
import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import com.dean.baby.common.repository.AgeRepository;
import com.dean.baby.common.repository.CategoryRepository;
import com.dean.baby.common.repository.MilestoneRepository;
import com.dean.baby.common.repository.UserRepository;
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
        // 驗證類別
        Category category = categoryRepository.findById(milestoneDTO.getCategory().getId())
                .orElseThrow(() -> new ApiException(SysCode.CATEGORY_NOT_FOUND));

        // 查找或建立 Age
        Age age = ageRepository.findByMonth(milestoneDTO.getAgeInMonths())
                .orElseGet(() -> createAgeIfNotExists(milestoneDTO.getAgeInMonths()));

        // 建立 Milestone
        Milestone milestone = new Milestone();
        milestone.setAge(age);
        milestone.setCategory(category);

        // 設定描述（LangFieldObject）
        LangFieldObject desc = milestoneDTO.getDescriptionObject();
        if (desc == null) {
            desc = new LangFieldObject();
            if (milestoneDTO.getDescription() != null) {
                // 使用目前語系
                desc.setLangByLocaleName(milestoneDTO.getDescription());
            }
            // 補齊其他語言為空字串
            desc.setDefaultBeforeInsert("");
        }
        milestone.setDescription(desc);

        // 設定影片連結
        milestone.setVideoUrl(milestoneDTO.getVideoUrl());

        Milestone saved = milestoneRepository.save(milestone);
        return MilestoneDTO.fromEntity(saved);
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
        if (lang == null) {
            return List.of();
        }
        return milestoneRepository.findAll().stream()
                .filter(m -> m.getAge() != null && m.getAge().getMonth() == age)
                .map(m -> {
                    String desc = m.getDescription() != null ? m.getDescription().getLang(lang.getCode()) : null;
                    return MilestoneTranslationDTO.builder()
                            .id(m.getId()) // 使用 Milestone ID 以便前端導向詳細頁
                            .languageCode(lang.getCode())
                            .description(desc != null ? desc : "")
                            .build();
                })
                .toList();
    }

    @Transactional
    public MilestoneDTO updateMilestone(UUID id, MilestoneDTO milestoneDTO) {
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new ApiException(SysCode.MILESTONE_NOT_FOUND));

        // 驗證類別
        Category category = categoryRepository.findById(milestoneDTO.getCategory().getId())
                .orElseThrow(() -> new ApiException(SysCode.CATEGORY_NOT_FOUND));

        // Age 更新
        Age age = ageRepository.findByMonth(milestoneDTO.getAgeInMonths())
                .orElseGet(() -> createAgeIfNotExists(milestoneDTO.getAgeInMonths()));

        milestone.setAge(age);
        milestone.setCategory(category);

        // 更新描述
        LangFieldObject desc = milestoneDTO.getDescriptionObject();
        if (desc == null) {
            desc = milestone.getDescription() != null ? milestone.getDescription() : new LangFieldObject();
            if (milestoneDTO.getDescription() != null) {
                desc.setLangByLocaleName(milestoneDTO.getDescription());
            }
            desc.setDefaultBeforeInsert("");
        }
        milestone.setDescription(desc);

        // 更新影片連結
        milestone.setVideoUrl(milestoneDTO.getVideoUrl());

        Milestone updated = milestoneRepository.save(milestone);
        return MilestoneDTO.fromEntity(updated);
    }

    @Transactional
    public void deleteMilestone(UUID id) {
        Milestone milestone = milestoneRepository.findById(id)
                .orElseThrow(() -> new ApiException(SysCode.MILESTONE_NOT_FOUND));
        milestoneRepository.delete(milestone);
    }

    private Age createAgeIfNotExists(int month) {
        Age age = new Age();
        age.setMonth(month);

        // 預設多語名稱
        com.dean.baby.common.dto.common.LangFieldObject displayName = new com.dean.baby.common.dto.common.LangFieldObject();
        displayName.setLang(Language.ENGLISH.getCode(), month + " Months");
        displayName.setLang(Language.TRADITIONAL_CHINESE.getCode(), month + "個月");
        displayName.setLang(Language.SIMPLIFIED_CHINESE.getCode(), month + "个月");
        displayName.setLang(Language.JAPANESE.getCode(), month + "ヶ月");
        displayName.setLang(Language.KOREAN.getCode(), month + "개월");
        displayName.setLang(Language.VIETNAMESE.getCode(), month + " tháng");

        age.setDisplayName(displayName);
        return ageRepository.save(age);
    }
}
