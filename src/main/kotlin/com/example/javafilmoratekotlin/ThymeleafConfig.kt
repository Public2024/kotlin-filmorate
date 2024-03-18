package com.example.javafilmoratekotlin

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.thymeleaf.templatemode.TemplateMode
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver

@Configuration
class ThymeleafConfig: WebMvcConfigurer {

    @Bean
    fun templateResolver(): ClassLoaderTemplateResolver{
        val templateResolver = ClassLoaderTemplateResolver()
        templateResolver.prefix = "templates/"
        templateResolver.suffix = ".html"
        templateResolver.templateMode = TemplateMode.HTML5
        templateResolver.characterEncoding = "UTF-8"
        templateResolver.order = 1
        templateResolver.isCacheable = false
        return templateResolver
    }
}