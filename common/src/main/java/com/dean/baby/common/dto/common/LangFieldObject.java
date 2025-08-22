package com.dean.baby.common.dto.common;


import java.io.Serializable;
import java.lang.reflect.Field;

import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.context.i18n.LocaleContextHolder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(using = LangFieldObjectSerializer.class)
public class LangFieldObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "英文", example = "Field")
    public String en;

    @Schema(description = "日文", example = "分野")
    public String ja;

    @Schema(description = "中文(簡體)", example = "栏位")
    @JsonProperty("zh-CN")
    public String zh_cn;

    @Schema(description = "中文(繁體)", example = "欄位")
    @JsonProperty("zh-TW")
    public String zh_tw;

    @Schema(description = "韓文", example = "필드")
    @JsonProperty("ko")
    public String ko;

    @Schema(description = "越南文", example = "Trường")
    @JsonProperty("vi")
    public String vi;

    public LangFieldObject(String localeName, String value) {
        try {
            String localeNameReplaced = localeName.replace('-', '_').toLowerCase();
            for (Field f : this.getClass().getFields()) {
                if (f.getName().equals(localeNameReplaced)) {
                    f.set(this, value);
                }
            }
        } catch (Exception e) {
            throw new ApiException(SysCode.BAD_CREDENTIALS);
        }
    }

    @JsonIgnore
    public String getLangByLocaleName(String localeName) {
        try {
            String localeNameReplaced = localeName.replace('-', '_').toLowerCase();
            for (Field f : this.getClass().getFields()) {
                if (f.getName().equals(localeNameReplaced))
                    return f.get(this).toString();
            }
        } catch (Exception e) {
            return this.zh_tw;
        }
        return this.zh_tw;
    }

    @JsonIgnore
    public String getLangByLocaleName() {
        return getLangByLocaleName(LocaleContextHolder.getLocale().toLanguageTag());
    }

    @JsonIgnore
    public void setLangByLocaleName(String localeName, String value) {
        try {
            String localeNameReplaced = localeName.replace('-', '_').toLowerCase();
            for (Field f : this.getClass().getFields()) {
                if (f.getName().equals(localeNameReplaced))
                    f.set(this, value);
            }
        } catch (Exception e) {
            throw new ApiException(SysCode.BAD_CREDENTIALS);
        }
    }

    @JsonIgnore
    public void setLangByLocaleName(String lang) {
        setLangByLocaleName(LocaleContextHolder.getLocale().toLanguageTag(), lang);
    }

    @JsonIgnore
    public void setDefaultBeforeInsert(String lang) {
        try {
            for (Field f : this.getClass().getFields()) {
                if (null == f.get(this) || f.get(this).equals("")) {
                    f.set(this, lang);
                }
            }
        } catch (Exception e) {
            throw new ApiException(SysCode.BAD_CREDENTIALS);
        }
    }

    @JsonIgnore
    public void setDefaultBeforeInsert() {
        setDefaultBeforeInsert(getLangByLocaleName());
    }

}
