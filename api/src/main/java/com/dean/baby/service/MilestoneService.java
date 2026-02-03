package com.dean.baby.common.service;

import com.dean.baby.common.dto.AgeDto;
import com.dean.baby.common.dto.CategoryDTO;
import com.dean.baby.common.dto.FlashcardSummaryDTO;
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
import com.dean.baby.common.util.ChangeLogUtil;
import com.dean.baby.common.util.LanguageUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import com.dean.baby.common.dto.enums.Language;
import com.dean.baby.common.dto.common.LangFieldObject;

@Service
@Slf4j
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
        Age age = findAgeById(milestoneDTO.getAge().getId());
        Milestone milestone = new Milestone();
        milestone.setAge(age);
        milestone.setCategory(category);
        milestone.setDescription(buildDescriptionObject(milestoneDTO));
        milestone.setVideoUrl(milestoneDTO.getVideoUrl());
        milestone.setImageBase64(milestoneDTO.getImageBase64());

        Milestone saved = milestoneRepository.save(milestone);
        MilestoneDTO result = MilestoneDTO.fromEntity(saved);

        // 記錄異動日誌
        ChangeLogUtil.logCreate("Milestone", saved.getId(), result);
        log.info("Milestone created: id={}, category={}, ageMonths={}",
                saved.getId(), category.getId(), age.getMonth());

        return result;
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
     * 獲取所有 Milestones（支援分頁）
     */
    public Page<MilestoneDTO> getAllMilestones(Pageable pageable) {
        Language currentLanguage = LanguageUtil.getLanguageFromLocale();
        return milestoneRepository.findAll(pageable)
                .map(milestone -> buildMilestoneDTOWithLanguage(milestone, currentLanguage));
    }

    /**
     * 根據年齡 UUID 查找里程碑（支援分頁）
     */
    public Page<MilestoneDTO> getMilestonesByAgeId(UUID ageId, Pageable pageable) {
        Language currentLanguage = LanguageUtil.getLanguageFromLocale();
        return milestoneRepository.findByAgeId(ageId, pageable)
                .map(milestone -> buildMilestoneDTOWithLanguage(milestone, currentLanguage));
    }

    /**
     * 根據分類 UUID 查找里程碑（支援分頁）
     */
    public Page<MilestoneDTO> getMilestonesByCategoryId(UUID categoryId, Pageable pageable) {
        Language currentLanguage = LanguageUtil.getLanguageFromLocale();
        return milestoneRepository.findByCategoryId(categoryId, pageable)
                .map(milestone -> buildMilestoneDTOWithLanguage(milestone, currentLanguage));
    }

    /**
     * 根據年齡 UUID 和分類 UUID 查找里程碑（支援分頁）
     */
    public Page<MilestoneDTO> getMilestonesByAgeIdAndCategoryId(UUID ageId, UUID categoryId, Pageable pageable) {
        Language currentLanguage = LanguageUtil.getLanguageFromLocale();
        return milestoneRepository.findByAgeIdAndCategoryId(ageId, categoryId, pageable)
                .map(milestone -> buildMilestoneDTOWithLanguage(milestone, currentLanguage));
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

    /**
     * 根據條件查找里程碑，支援 ageId 和 categoryId 的組合查詢
     * 如果兩個參數都為 null 則返回所有里程碑
     */
    public List<MilestoneDTO> getMilestonesByConditions(UUID ageId, UUID categoryId) {
        Language currentLanguage = LanguageUtil.getLanguageFromLocale();
        List<Milestone> milestones;

        if (ageId != null && categoryId != null) {
            // 兩個條件都有，查詢組合條件
            milestones = milestoneRepository.findByAgeIdAndCategoryId(ageId, categoryId);
        } else if (ageId != null) {
            // 只有 ageId
            milestones = milestoneRepository.findByAgeId(ageId);
        } else if (categoryId != null) {
            // 只有 categoryId
            milestones = milestoneRepository.findByCategoryId(categoryId);
        } else {
            // 都沒有，返回所有
            milestones = milestoneRepository.findAll();
        }

        return milestones.stream()
                .map(milestone -> buildMilestoneDTOWithLanguage(milestone, currentLanguage))
                .toList();
    }

    @Transactional
    public MilestoneDTO updateMilestone(UUID id, MilestoneDTO milestoneDTO) {
        Milestone milestone = findMilestoneById(id);

        // 記錄更新前的資料
        MilestoneDTO oldData = MilestoneDTO.fromEntity(milestone);

        Category category = findCategoryById(milestoneDTO.getCategory().getId());
        Age age = findAgeById(milestoneDTO.getAge().getId());
        milestone.setAge(age);
        milestone.setCategory(category);
        milestone.setDescription(updateDescriptionObject(milestone.getDescription(), milestoneDTO));
        milestone.setVideoUrl(milestoneDTO.getVideoUrl());

        // 只有在有新圖片時才更新圖片，避免覆蓋掉原有圖片
        if (milestoneDTO.getImageBase64() != null && !milestoneDTO.getImageBase64().isEmpty()) {
            milestone.setImageBase64(milestoneDTO.getImageBase64());
        }

        Milestone updated = milestoneRepository.save(milestone);
        MilestoneDTO newData = MilestoneDTO.fromEntity(updated);

        // 記錄異動日誌
        ChangeLogUtil.logUpdate("Milestone", id, oldData, newData);
        log.info("Milestone updated: id={}, category={}, ageMonths={}",
                id, category.getId(), age.getMonth());

        return newData;
    }

    @Transactional
    public void deleteMilestone(UUID id) {
        Milestone milestone = findMilestoneById(id);

        // 記錄被刪除的資料
        MilestoneDTO deletedData = MilestoneDTO.fromEntity(milestone);

        milestoneRepository.delete(milestone);

        // 記錄異動日誌
        ChangeLogUtil.logDelete("Milestone", id, deletedData);
        log.warn("Milestone deleted: id={}, category={}, ageMonths={}",
                id, milestone.getCategory().getId(), milestone.getAge().getMonth());
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

    private Age findAgeById(UUID ageId) {
        return ageRepository.findById(ageId)
                .orElseThrow(() -> new ApiException(SysCode.DATA_NOT_FOUND));
    }

    /**
     * 構建里程碑描述對象
     */
    private LangFieldObject buildDescriptionObject(MilestoneDTO milestoneDTO) {
        LangFieldObject desc = milestoneDTO.getDescriptionObject();
        if (desc == null) {
            // 建立空的語言物件，不自動將單一語系內容複製到所有語系
            desc = new LangFieldObject();
            if (milestoneDTO.getDescription() != null && !milestoneDTO.getDescription().isBlank()) {
                // 只設定目前 locale 對應語言，其餘保持空白
                desc.setLangByLocaleName(milestoneDTO.getDescription());
            }
        }
        return desc;
    }

    /**
     * 更新里程碑描述對象：僅覆寫有提供值的語言，其他語言保留原值
     */
    private LangFieldObject updateDescriptionObject(LangFieldObject existingDesc, MilestoneDTO milestoneDTO) {
        LangFieldObject incoming = milestoneDTO.getDescriptionObject();
        if (existingDesc == null) {
            // 沒有既存資料，直接建立（沿用 create 邏輯）
            return buildDescriptionObject(milestoneDTO);
        }
        if (incoming == null) {
            // 沒有新資料，保留原本
            return existingDesc;
        }
        // 合併：僅當新值 != null 且非空字串時覆寫
        for (Language lang : Language.values()) {
            String code = lang.getCode();
            String newVal = incoming.getLang(code);
            if (newVal != null) { // 允許清空：若需要保留舊值則再判斷 isBlank
                if (!newVal.isBlank()) {
                    existingDesc.setLang(code, newVal);
                }
                // 若想支援清空語言，可以在此處加上 else 將其設為空字串
            }
        }
        return existingDesc;
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
                .flashcards(milestone.getFlashcards() != null ?
                    milestone.getFlashcards().stream()
                        .map(FlashcardSummaryDTO::fromEntity)
                        .toList() : null)
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
