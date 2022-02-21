package com.epam.javacourse.hotelapp.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.AcceptHeaderLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;


@Configuration
public class LocaleConfig implements WebMvcConfigurer {

    @Bean("messageSource")
    public MessageSource messageSource() {
        ResourceBundleMessageSource messageSource =
                new ResourceBundleMessageSource();
        messageSource.setBasenames("language/locale_resources");
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    @Bean
    @Primary
    public LocaleResolver customerLocaleResolver() {
        SessionLocaleResolver slr = new SessionLocaleResolver();
        slr.setDefaultLocale(Locale.US);
        slr.setLocaleAttributeName("session.current.locale");
        slr.setTimeZoneAttributeName("session.current.timezone");
        return slr;
    }

    //  to detect any change in the user’s locale and then switch to the new locale
    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor
                = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }



}
//
//    @Value("${app.baseName}")
//    private String baseName;
//
//    @Value("${app.defaultLocale")
//    private String defaultLocale;
//
//    @Bean(name = "locale_resources")
//    public ResourceBundleMessageSource messageSource() {
//        ResourceBundleMessageSource rs = new ResourceBundleMessageSource();
//        rs.setBasename(baseName);
//        rs.setDefaultEncoding("UTF-8");
//        rs.setUseCodeAsDefaultMessage(true);
//        return rs;
//    }
//
//    @Bean
//    public LocaleResolver localeResolver() {
//        AcceptHeaderLocaleResolver acceptHeaderLocaleResolver = new AcceptHeaderLocaleResolver();
//        acceptHeaderLocaleResolver.setDefaultLocale(new Locale(defaultLocale));
//        return acceptHeaderLocaleResolver;
//    }
//
//
//
//    //  The language value must be provided via session
//    //  (uses a locale attribute in the user's session in case of a custom setting,
//    //  with a fallback to the specified default locale).
////    @Bean
////    public LocaleResolver localeResolver() {
////        SessionLocaleResolver sessionLocaleResolver = new SessionLocaleResolver();
////        sessionLocaleResolver.setDefaultLocale(Locale.US);
////        return sessionLocaleResolver;
////    }
//
//
//    //  switch to a new locale based on the value of the lang parameter appended to a request
////    @Bean
////    public LocaleChangeInterceptor localeChangeInterceptor() {
////        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
////        localeChangeInterceptor.setParamName("lang");
////        return localeChangeInterceptor;
////    }
////
////    @Bean(name = "messageSource")
////    public MessageSource getMessageResource() {
////        ReloadableResourceBundleMessageSource messageResource = new ReloadableResourceBundleMessageSource();
////
////        messageResource.setBasename("classpath:locale_resources");
////        messageResource.setDefaultEncoding("UTF-8");
////        return messageResource;
////    }
////
////    // add localeChangeInterceptor bean to the application's interceptor registry
////    @Override
////    public void addInterceptors(InterceptorRegistry registry) {
////        registry.addInterceptor(localeChangeInterceptor());
////    }
//}
