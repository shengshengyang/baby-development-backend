package com.dean.baby.service;

import com.dean.baby.dto.FlashcardDTO;
import com.dean.baby.dto.FlashcardTranslationDTO;
import com.dean.baby.entity.Flashcard;
import com.dean.baby.entity.FlashcardTranslation;
import com.dean.baby.entity.Milestone;
import com.dean.baby.repository.FlashcardRepository;
import com.dean.baby.repository.FlashcardTranslationRepository;
import com.dean.baby.repository.MilestoneRepository;
import com.dean.baby.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class FlashCardService extends BaseService {

    private final FlashcardRepository flashcardRepository;
    private final FlashcardTranslationRepository translationRepository;
    private final MilestoneRepository milestoneRepository;

    protected FlashCardService(UserRepository userRepository, FlashcardRepository flashcardRepository, FlashcardTranslationRepository translationRepository, MilestoneRepository milestoneRepository) {
        super(userRepository);
        this.flashcardRepository = flashcardRepository;
        this.translationRepository = translationRepository;
        this.milestoneRepository = milestoneRepository;
    }

    @Transactional
    public FlashcardDTO createFlashcard(Long milestoneId, String category, List<FlashcardTranslationDTO> translationsDTO) {
        // 查找 Milestone
        Milestone milestone = milestoneRepository.findById(milestoneId)
                .orElseThrow(() -> new RuntimeException("Milestone not found"));

        // 創建 Flashcard Entity
        Flashcard flashcard = new Flashcard();
        flashcard.setCategory(category);
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
                .collect(Collectors.toList());

        flashcard.setTranslations(translations);

        // 保存並返回 DTO
        Flashcard savedFlashcard = flashcardRepository.save(flashcard);
        return convertToDTO(savedFlashcard);
    }

    public Optional<FlashcardDTO> getFlashcardById(Long id) {
        return flashcardRepository.findById(id)
                .map(this::convertToDTO);
    }

    public List<FlashcardDTO> getAllFlashcards(Integer ageInMonths) {
        if (ageInMonths != null) {
            return flashcardRepository.findByMilestoneAgeInMonths(ageInMonths).stream()
                    .map(this::convertToDTO)
                    .toList();
        }
        return flashcardRepository.findAll().stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Transactional
    public FlashcardDTO updateFlashcard(Long id, String category, List<FlashcardTranslationDTO> translationsDTO) {
        // 查找 Flashcard
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));

        // 更新屬性
        flashcard.setCategory(category);

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
    public void deleteFlashcard(Long id) {
        // 查找 Flashcard
        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Flashcard not found"));

        // 刪除相關翻譯
        translationRepository.deleteAll(flashcard.getTranslations());

        // 刪除 Flashcard
        flashcardRepository.delete(flashcard);
    }

    private FlashcardDTO convertToDTO(Flashcard flashcard) {
        List<FlashcardTranslationDTO> translationDTOs = flashcard.getTranslations().stream()
                .map(translation -> FlashcardTranslationDTO.builder()
                        .id(translation.getId())
                        .languageCode(translation.getLanguageCode())
                        .frontText(translation.getFrontText())
                        .backText(translation.getBackText())
                        .imageUrl(translation.getImageUrl())
                        .build())
                .toList();

        return new FlashcardDTO(
                flashcard.getId(),
                flashcard.getCategory(),
                flashcard.getMilestone().getId(),
                translationDTOs
        );
    }

}
