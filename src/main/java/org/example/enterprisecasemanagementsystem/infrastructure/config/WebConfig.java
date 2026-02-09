package org.example.enterprisecasemanagementsystem.infrastructure.config;

import org.example.enterprisecasemanagementsystem.infrastructure.persistence.converters.EmailValueConverter;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.converters.RoleConverter;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new EmailValueConverter());
        registry.addConverter(new RoleConverter());
    }
}
