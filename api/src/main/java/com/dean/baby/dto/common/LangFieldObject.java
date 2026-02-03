package com.dean.baby.common.dto.common;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.dean.baby.common.dto.enums.Language;
import com.dean.baby.common.exception.ApiException;
import com.dean.baby.common.exception.SysCode;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.context.i18n.LocaleContextHolder;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "多語言欄位物件")
public class LangFieldObject implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    private Map<String, String> languageMap = new HashMap<>();

    /**
     * 建構函數 - 透過語言代碼設定值
     * @param languageCode 語言代碼
     * @param value 值
     */
    public LangFieldObject(String languageCode, String value) {
        setLang(languageCode, value);
    }

    /**
     * 透過語言代碼取得值
     * @param languageCode 語言代碼
     * @return 對應的值，若找不到則返回null
     */
    @JsonIgnore
    public String getLang(String languageCode) {
        if (languageMap == null) {
            languageMap = new HashMap<>();
        }
        Language language = Language.fromCode(languageCode);
        if (language != null) {
            return languageMap.get(language.getCode());
        }
        return null;
    }

    /**
     * 透過語言代碼設定值
     * @param languageCode 語言代碼
     * @param value 要設定的值
     */
    @JsonIgnore
    public void setLang(String languageCode, String value) {
        if (languageMap == null) {
            languageMap = new HashMap<>();
        }
        Language language = Language.fromCode(languageCode);
        if (language != null) {
            languageMap.put(language.getCode(), value);
        } else {
            throw new ApiException(SysCode.BAD_CREDENTIALS);
        }
    }

    /**
     * 取得目前Locale對應的語言值
     * @return 目前語言環境對應的值
     */
    @JsonIgnore
    public String getLangByLocaleName() {
        if (languageMap == null) {
            languageMap = new HashMap<>();
        }
        String currentLocale = LocaleContextHolder.getLocale().toLanguageTag().toLowerCase();

        // 將實際的locale映射到我們的語言代碼
        String languageCode = mapLocaleToLanguageCode(currentLocale);

        String value = languageMap.get(languageCode);
        if (value != null && !value.isEmpty()) {
            return value;
        }

        // 如果找不到對應語言，返回繁體中文作為預設值
        return languageMap.getOrDefault(Language.TRADITIONAL_CHINESE.getCode(), "");
    }

    /**
     * 設定目前Locale對應的語言值
     * @param value 要設定的值
     */
    @JsonIgnore
    public void setLangByLocaleName(String value) {
        if (languageMap == null) {
            languageMap = new HashMap<>();
        }
        String currentLocale = LocaleContextHolder.getLocale().toLanguageTag().toLowerCase();
        String languageCode = mapLocaleToLanguageCode(currentLocale);
        languageMap.put(languageCode, value);
    }

    /**
     * 將系統locale映射到我們定義的語言代碼
     * @param locale 系統locale
     * @return 對應的語言代碼
     */
    @JsonIgnore
    private String mapLocaleToLanguageCode(String locale) {
        String normalizedLocale = locale.toLowerCase().replace('-', '_');

        // 處理中文的特殊情況
        if (normalizedLocale.startsWith("zh")) {
            if (normalizedLocale.contains("cn") || normalizedLocale.contains("hans") || normalizedLocale.equals("zh")) {
                return Language.SIMPLIFIED_CHINESE.getCode(); // "cn"
            } else if (normalizedLocale.contains("tw") || normalizedLocale.contains("hant") || normalizedLocale.contains("hk")) {
                return Language.TRADITIONAL_CHINESE.getCode(); // "tw"
            }
        }

        // 處理其他語言
        if (normalizedLocale.startsWith("en")) {
            return Language.ENGLISH.getCode(); // "en"
        } else if (normalizedLocale.startsWith("ja")) {
            return Language.JAPANESE.getCode(); // "ja"
        } else if (normalizedLocale.startsWith("ko")) {
            return Language.KOREAN.getCode(); // "ko"
        } else if (normalizedLocale.startsWith("vi")) {
            return Language.VIETNAMESE.getCode(); // "vi"
        }

        // 預設返回繁體中文代碼
        return Language.TRADITIONAL_CHINESE.getCode(); // "tw"
    }

    /**
     * 為所有空欄位設定預設值
     * @param defaultValue 預設值
     */
    @JsonIgnore
    public void setDefaultBeforeInsert(String defaultValue) {
        if (languageMap == null) {
            languageMap = new HashMap<>();
        }
        for (Language lang : Language.values()) {
            if (languageMap.get(lang.getCode()) == null || languageMap.get(lang.getCode()).isEmpty()) {
                languageMap.put(lang.getCode(), defaultValue);
            }
        }
    }

    /**
     * 使用目前語言環境的值作為預設值，為所有空欄位設定預設值
     */
    @JsonIgnore
    public void setDefaultBeforeInsert() {
        setDefaultBeforeInsert(getLangByLocaleName());
    }

    /**
     * 為JSON序列化提供動態屬性
     * @return 語言映射表
     */
    @JsonAnyGetter
    public Map<String, String> getLanguageMap() {
        if (languageMap == null) {
            languageMap = new HashMap<>();
        }
        return languageMap;
    }

    /**
     * 為JSON反序列化設定動態屬性
     * @param key 語言代碼
     * @param value 值
     */
    @JsonAnySetter
    public void setLanguageProperty(String key, String value) {
        if (languageMap == null) {
            languageMap = new HashMap<>();
        }
        Language language = Language.fromCode(key);
        if (language != null) {
            languageMap.put(key, value);
        }
    }

    // 便利方法 - 基於枚舉的getter/setter
    /** 取得英文值 */
    public String getEn() {
        String value = getLang(Language.ENGLISH.getCode());
        return value != null ? value : "";
    }
    /** 設定英文值 */
    public void setEn(String value) { setLang(Language.ENGLISH.getCode(), value); }

    /** 取得日文值 */
    public String getJa() {
        String value = getLang(Language.JAPANESE.getCode());
        return value != null ? value : "";
    }
    /** 設定日文值 */
    public void setJa(String value) { setLang(Language.JAPANESE.getCode(), value); }

    /** 取得簡體中文值 */
    public String getCn() {
        String value = getLang(Language.SIMPLIFIED_CHINESE.getCode());
        return value != null ? value : "";
    }
    /** 設定簡體中文值 */
    public void setCn(String value) { setLang(Language.SIMPLIFIED_CHINESE.getCode(), value); }

    /** 取得繁體中文值 */
    public String getTw() {
        String value = getLang(Language.TRADITIONAL_CHINESE.getCode());
        return value != null ? value : "";
    }
    /** 設定繁體中文值 */
    public void setTw(String value) { setLang(Language.TRADITIONAL_CHINESE.getCode(), value); }

    /** 取得韓文值 */
    public String getKo() {
        String value = getLang(Language.KOREAN.getCode());
        return value != null ? value : "";
    }
    /** 設定韓文值 */
    public void setKo(String value) { setLang(Language.KOREAN.getCode(), value); }

    /** 取得越南文值 */
    public String getVi() {
        String value = getLang(Language.VIETNAMESE.getCode());
        return value != null ? value : "";
    }
    /** 設定越南文值 */
    public void setVi(String value) { setLang(Language.VIETNAMESE.getCode(), value); }
}
