package com.dean.baby.common.dto;

import com.dean.baby.common.dto.common.LangFieldObject;
import lombok.Data;

/**
 * Video表單的DTO類 - 支援完整的多語言
 */
@Data
public class VideoFormDto {
    // 支援所有語言的描述
    private String descriptionEn;      // 英文
    private String descriptionTw;      // 繁體中文
    private String descriptionCn;      // 簡體中文
    private String descriptionJa;      // 日文
    private String descriptionKo;      // 韓文
    private String descriptionVi;      // 越南文

    private String videoUrl;
    private Integer durationSeconds;
    private String thumbnailUrl;

    /**
     * 轉換為LangFieldObject
     */
    public LangFieldObject toDescription() {
        LangFieldObject description = new LangFieldObject();
        if (descriptionEn != null && !descriptionEn.trim().isEmpty()) {
            description.setEn(descriptionEn);
        }
        if (descriptionTw != null && !descriptionTw.trim().isEmpty()) {
            description.setTw(descriptionTw);
        }
        if (descriptionCn != null && !descriptionCn.trim().isEmpty()) {
            description.setCn(descriptionCn);
        }
        if (descriptionJa != null && !descriptionJa.trim().isEmpty()) {
            description.setJa(descriptionJa);
        }
        if (descriptionKo != null && !descriptionKo.trim().isEmpty()) {
            description.setKo(descriptionKo);
        }
        if (descriptionVi != null && !descriptionVi.trim().isEmpty()) {
            description.setVi(descriptionVi);
        }
        return description;
    }

    /**
     * 從LangFieldObject載入資料
     */
    public void fromDescription(LangFieldObject description) {
        if (description != null) {
            this.descriptionEn = description.getEn();
            this.descriptionTw = description.getTw();
            this.descriptionCn = description.getCn();
            this.descriptionJa = description.getJa();
            this.descriptionKo = description.getKo();
            this.descriptionVi = description.getVi();
        }
    }

    /**
     * 從VideoDto載入資料
     */
    public static VideoFormDto fromVideoDto(VideoDto videoDto) {
        VideoFormDto form = new VideoFormDto();
        form.setVideoUrl(videoDto.getVideoUrl());
        form.setDurationSeconds(videoDto.getDurationSeconds());
        form.setThumbnailUrl(videoDto.getThumbnailUrl());

        if (videoDto.getDescription() != null) {
            form.fromDescription(videoDto.getDescription());
        }

        return form;
    }
}
