package com.dean.baby.common.service;

import com.dean.baby.common.dto.*;
import com.dean.baby.common.entity.*;
import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import com.dean.baby.common.repository.*;
import com.dean.baby.common.util.LanguageUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import com.dean.baby.common.dto.enums.Language;

@Service
public class FlashCardService extends BaseService {

    private final FlashcardRepository flashcardRepository;
    private final FlashcardTranslationRepository translationRepository;
    private final MilestoneRepository milestoneRepository;
    private final ProgressRepository progressRepository;
    private final BabyService babyService;
    private final CategoryRepository categoryRepository;

    protected FlashCardService(UserRepository userRepository, FlashcardRepository flashcardRepository,
            FlashcardTranslationRepository translationRepository, MilestoneRepository milestoneRepository,
            ProgressRepository progressRepository, BabyService babyService, AgeRepository ageRepository,
            CategoryRepository categoryRepository) {
        super(userRepository);
        this.flashcardRepository = flashcardRepository;
        this.translationRepository = translationRepository;
        this.milestoneRepository = milestoneRepository;
        this.progressRepository = progressRepository;
        this.babyService = babyService;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public FlashcardDTO createFlashcard(FlashcardDTO dto) {
        // 查找 Milestone
        UUID milestoneId = dto.getMilestone() != null ? dto.getMilestone().getId() : null;
        if (milestoneId == null) {
            throw new ApiException(SysCode.MISSING_PARAMETER, "Milestone id is required");
        }
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new ApiException(SysCode.MILESTONE_NOT_FOUND));

        // 查找 Category
        UUID categoryId = dto.getCategoryId();
        if (categoryId == null) {
            throw new ApiException(SysCode.MISSING_PARAMETER, "Category id is required");
        }
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ApiException(SysCode.CATEGORY_NOT_FOUND));

        // 創建 Flashcard Entity
        Flashcard flashcard = new Flashcard();
        flashcard.setCategory(category);
        flashcard.setMilestone(milestone);
        flashcard.setSubject(dto.getSubject());
        flashcard.setImageUrl(dto.getImageUrl());

        // 將 DTO 轉換為 Entity (僅 description & 語言)
        List<FlashcardTranslationDTO> translationsDTO = Optional.ofNullable(dto.getTranslations()).orElse(List.of());
        List<FlashcardTranslation> translations = translationsDTO.stream()
                .map(tdto -> {
                    FlashcardTranslation translation = new FlashcardTranslation();
                    translation.setLanguageCode(tdto.getLanguageCode());
                    translation.setDescription(tdto.getDescription());
                    translation.setFlashcard(flashcard);
                    return translation;
                })
                .toList();

        flashcard.setTranslations(translations);

        // 保存並返回 DTO
        Flashcard savedFlashcard = flashcardRepository.save(flashcard);
        return convertToDTO(savedFlashcard);
    }

    public Optional<FlashcardDTO> getFlashcardById(UUID id) {
        return flashcardRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<FlashcardLanguageDTO> getAllFlashcards() {
        Language language = LanguageUtil.getLanguageFromLocale();
        return flashcardRepository.findAll().stream()
                .map(flashcard -> convertToLanguageDTO(flashcard, language))
                .filter(Objects::nonNull)
                .toList();
    }

    @Transactional
    public FlashcardDTO updateFlashcard(UUID id, FlashcardDTO dto) {
        // 查找 Flashcard
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new ApiException(SysCode.FLASHCARD_NOT_FOUND));

        // 更新屬性：Category, Subject, ImageUrl, 以及 Milestone (若提供)
        if (dto.getCategoryId() != null) {
            Category category = categoryRepository.findById(dto.getCategoryId())
                    .orElseThrow(() -> new ApiException(SysCode.CATEGORY_NOT_FOUND));
            flashcard.setCategory(category);
        }
        if (dto.getMilestone() != null && dto.getMilestone().getId() != null) {
            Milestone milestone = milestoneRepository.findById(dto.getMilestone().getId())
                    .orElseThrow(() -> new ApiException(SysCode.MILESTONE_NOT_FOUND));
            flashcard.setMilestone(milestone);
        }
        flashcard.setSubject(dto.getSubject());
        flashcard.setImageUrl(dto.getImageUrl());

        // 刪除舊的翻譯
        if (flashcard.getTranslations() != null && !flashcard.getTranslations().isEmpty()) {
            translationRepository.deleteAll(flashcard.getTranslations());
        }

        // 創建新的翻譯 (僅 description & 語言)
        List<FlashcardTranslationDTO> translationsDTO = Optional.ofNullable(dto.getTranslations()).orElse(List.of());
        List<FlashcardTranslation> newTranslations = translationsDTO.stream()
                .map(tdto -> {
                    FlashcardTranslation translation = new FlashcardTranslation();
                    translation.setLanguageCode(tdto.getLanguageCode());
                    translation.setDescription(tdto.getDescription());
                    translation.setFlashcard(flashcard);
                    return translation;
                })
                .toList();

        flashcard.setTranslations(newTranslations);

        // 保存並返回 DTO
        Flashcard updatedFlashcard = flashcardRepository.save(flashcard);
        return convertToDTO(updatedFlashcard);
    }

    @Transactional
    public void deleteFlashcard(UUID id) {
        // 查找 Flashcard
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new ApiException(SysCode.FLASHCARD_NOT_FOUND));

        // 刪除相關翻譯
        if (flashcard.getTranslations() != null && !flashcard.getTranslations().isEmpty()) {
            translationRepository.deleteAll(flashcard.getTranslations());
        }

        // 刪除 Flashcard
        flashcardRepository.delete(flashcard);
    }

    @Transactional
    public BabyDto checkProgress(CheckProgressRequestVo vo) {
        User user = getCurrentUser();
        if (!user.isBaby(vo.babyId())) {
            throw new ApiException(SysCode.NOT_YOUR_BABY);
        }
        Progress progress = progressRepository.findByBabyIdAndFlashcardId(vo.babyId(), vo.flashcardId())
                .orElseGet(() -> {
                    Progress newProgress = new Progress();
                    newProgress.setBaby(new Baby());
                    newProgress.setFlashcard(new Flashcard());
                    return newProgress;
                });

        progressRepository.save(progress);

        return babyService.getBaby(vo.babyId());
    }

    private FlashcardDTO convertToDTO(Flashcard flashcard) {
        List<FlashcardTranslationDTO> translationDTOs = flashcard.getTranslations() == null ? List.of()
                : flashcard.getTranslations().stream()
                        .map(FlashcardTranslationDTO::fromEntity)
                        .toList();

        FlashcardDTO dto = FlashcardDTO.fromEntity(flashcard);
        dto.setTranslations(translationDTOs);
        return dto;
    }

    private FlashcardLanguageDTO convertToLanguageDTO(Flashcard flashcard, Language lang) {
        return flashcard.getTranslations()
                .stream()
                .filter(translation -> translation.getLanguageCode() == lang)
                .findFirst()
                .map(translation -> FlashcardLanguageDTO.fromEntityWithLanguage(flashcard, translation))
                .orElse(null);
    }

    /**
     * 根據年齡 ID 查找對應的 FlashCard
     */
    public List<FlashcardDTO> getFlashcardsByAgeId(UUID ageId) {
        return flashcardRepository.findByAgeId(ageId).stream()
                .map(this::convertToDTO)
                .toList();
    }

    /**
     * 根據條件查找 FlashCard，支援 ageId 和 categoryId 的組合查詢
     * 如果兩個參數都為 null 則返回所有 FlashCard
     */
    public List<FlashcardDTO> getFlashcardsByConditions(UUID ageId, UUID categoryId) {
        List<Flashcard> flashcards;

        if (ageId != null && categoryId != null) {
            // 兩個條件都有，查詢組合條件
            flashcards = flashcardRepository.findByAgeIdAndCategoryId(ageId, categoryId);
        } else if (ageId != null) {
            // 只有 ageId
            flashcards = flashcardRepository.findByAgeId(ageId);
        } else if (categoryId != null) {
            // 只有 categoryId
            flashcards = flashcardRepository.findByCategoryId(categoryId);
        } else {
            // 都沒有，返回所有
            flashcards = flashcardRepository.findAll();
        }

        return flashcards.stream()
                .map(this::convertToDTO)
                .toList();
    }
}
