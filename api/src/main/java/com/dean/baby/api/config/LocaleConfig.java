package com.dean.baby.api.config;

import com.dean.baby.common.dto.enums.Language;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Arrays;
import java.util.Locale;

/**
 * 國際化配置類
 * 使用現有的Language enum來配置支持的語言
 */
@Configuration
public class LocaleConfig implements WebMvcConfigurer {

    /**
     * 配置語言解析器
     * 使用現有的Language enum來定義支持的語言
     */
    @Bean
    public LocaleResolver localeResolver() {
        AcceptHeaderLocaleResolver localeResolver = new AcceptHeaderLocaleResolver();

        // 使用現有的Language enum來設置支持的語言列表
        localeResolver.setSupportedLocales(Arrays.asList(
            Locale.forLanguageTag("zh-TW"),    // Language.TRADITIONAL_CHINESE
            Locale.forLanguageTag("zh-CN"),    // Language.SIMPLIFIED_CHINESE
            Locale.forLanguageTag("en"),       // Language.ENGLISH
            Locale.forLanguageTag("ja"),       // Language.JAPANESE
            Locale.forLanguageTag("ko"),       // Language.KOREAN
            Locale.forLanguageTag("vi")        // Language.VIETNAMESE
        ));

        // 設置默認語言為繁體中文，與Language enum保持一致
        localeResolver.setDefaultLocale(Locale.forLanguageTag("zh-TW"));

        return localeResolver;
    }

    /**
     * 配置語言切換攔截器
     * 允許通過URL參數切換語言，例如：?lang=tw
     */
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
        lci.setParamName("lang");
        return lci;
    }

    /**
     * 註冊攔截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }
}
