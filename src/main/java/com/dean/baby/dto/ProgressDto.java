package com.dean.baby.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProgressDto {
    private int babyId;
    private int flashcardId;
    private int ageInMonths;
    private String category;
    private boolean achieved;
    private String dateAchieved;

}
