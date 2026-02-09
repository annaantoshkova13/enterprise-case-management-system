package org.example.enterprisecasemanagementsystem.infrastructure.persistence.converters;

import org.example.enterprisecasemanagementsystem.domain.Role;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class RoleConverter implements Converter<String, Role> {
    @Override
    public Role convert(String source) {
        return Role.valueOf(source.toUpperCase());
    }
}