package com.dean.baby.common.service;

import com.dean.baby.common.dto.*;
import com.dean.baby.common.entity.*;
import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import com.dean.baby.common.repository.*;
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
    private final AgeRepository ageRepository;

    protected FlashCardService(UserRepository userRepository, FlashcardRepository flashcardRepository, FlashcardTranslationRepository translationRepository, MilestoneRepository milestoneRepository, ProgressRepository progressRepository, BabyService babyService, AgeRepository ageRepository) {
        super(userRepository);
        this.flashcardRepository = flashcardRepository;
        this.translationRepository = translationRepository;
        this.milestoneRepository = milestoneRepository;
        this.progressRepository = progressRepository;
        this.babyService = babyService;
        this.ageRepository = ageRepository;
    }

    @Transactional
    public FlashcardDTO createFlashcard(UUID milestoneId, UUID category, List<FlashcardTranslationDTO> translationsDTO) {
        // 查找 Milestone
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new RuntimeException("Milestone not found"));

        // 創建 Flashcard Entity
        Flashcard flashcard = new Flashcard();
        flashcard.setCategory(new Category());
        flashcard.setMilestone(milestone);

        // 將 DTO 轉換為 Entity
        List<FlashcardTranslation> translations = translationsDTO.stream()
                .map(dto -> {
                    FlashcardTranslation translation = new FlashcardTranslation();
                    translation.setLanguageCode(dto.getLanguageCode());
                    translation.setFrontText(dto.getFrontText());
                    translation.setBackText(dto.getBackText());
                    translation.setImageUrl(dto.getImageUrl());
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

    public List<FlashcardLanguageDTO> getAllFlashcards(String language) {
        return flashcardRepository.findAll().stream()
                .map(flashcard -> convertToLanguageDTO(flashcard, language))
                .filter(Objects::nonNull)
                .toList();
    }

    @Transactional
    public FlashcardDTO updateFlashcard(UUID id, UUID category, List<FlashcardTranslationDTO> translationsDTO) {
        // 查找 Flashcard
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));

        // 更新屬性
        flashcard.setCategory(new Category());

        // 刪除舊的翻譯
        translationRepository.deleteAll(flashcard.getTranslations());

        // 創建新的翻譯
        List<FlashcardTranslation> newTranslations = translationsDTO.stream()
                .map(dto -> {
                    FlashcardTranslation translation = new FlashcardTranslation();
                    translation.setLanguageCode(dto.getLanguageCode());
                    translation.setFrontText(dto.getFrontText());
                    translation.setBackText(dto.getBackText());
                    translation.setImageUrl(dto.getImageUrl());
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
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));

        // 刪除相關翻譯
        translationRepository.deleteAll(flashcard.getTranslations());

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
        List<FlashcardTranslationDTO> translationDTOs = flashcard.getTranslations().stream()
                .map(FlashcardTranslationDTO::fromEntity)
                .toList();

        return FlashcardDTO.builder()
                .id(flashcard.getId())
                .category(CategoryDTO.fromEntity(flashcard.getCategory()))
                .milestone(MilestoneDTO.fromEntity(flashcard.getMilestone()))
                .ageInMonths(flashcard.getMilestone().getAgeInMonths())
                .translations(translationDTOs)
                .build();
    }

    private FlashcardLanguageDTO convertToLanguageDTO(Flashcard flashcard, String language) {
        Language lang = Language.fromCode(language);
        return flashcard.getTranslations()
                .stream()
                .filter(translation -> translation.getLanguageCode() == lang)
                .findFirst()
                .map(translation -> FlashcardLanguageDTO.fromEntityWithLanguage(flashcard, translation))
                .orElse(null);
    }
}
