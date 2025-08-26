package com.dean.baby.common.util;

import com.dean.baby.common.dto.enums.Language;
import com.dean.baby.common.dto.common.LangFieldObject;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * 語言處理工具類
 * 整合現有的Language enum和LangFieldObject的語言處理邏輯
 */
@Component
public class LanguageUtil {

    /**
     * 從當前的 LocaleContextHolder 獲取語言
     * 直接使用 LangFieldObject 中已有的語言映射邏輯
     * @return 對應的Language枚舉
     */
    public static Language getLanguageFromLocale() {
        // 創建一個臨時的 LangFieldObject 來使用其語言映射邏輯
        LangFieldObject tempLangObject = new LangFieldObject();
        String currentLocale = LocaleContextHolder.getLocale().toLanguageTag().toLowerCase();

        // 使用 LangFieldObject 中的 mapLocaleToLanguageCode 邏輯
        String languageCode = mapLocaleToLanguageCode(currentLocale);

        // 通過語言代碼獲取 Language 枚舉
        Language language = Language.fromCode(languageCode);
        return language != null ? language : Language.TRADITIONAL_CHINESE;
    }

    /**
     * 從語言代碼字符串獲取Language枚舉
     * @param languageCode 語言代碼
     * @return 對應的Language枚舉
     */
    public static Language getLanguageFromCode(String languageCode) {
        Language language = Language.fromCode(languageCode);
        return language != null ? language : Language.TRADITIONAL_CHINESE;
    }

    /**
     * 將Language枚舉轉換為語言代碼字符串
     * @param language Language枚舉
     * @return 語言代碼字符串
     */
    public static String getLanguageCode(Language language) {
        return language != null ? language.getCode() : Language.TRADITIONAL_CHINESE.getCode();
    }

    /**
     * 將系統locale映射到語言代碼
     * 直接使用 LangFieldObject 中的相同邏輯，保持一致性
     * @param locale 系統locale
     * @return 對應的語言代碼
     */
    private static String mapLocaleToLanguageCode(String locale) {
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
}
