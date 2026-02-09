package org.example.enterprisecasemanagementsystem.infrastructure.web.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.enterprisecasemanagementsystem.domain.Role;
import org.example.enterprisecasemanagementsystem.infrastructure.web.dto.request.CreateUserRequestDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CreateUserRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldBeValid_WhenAllFieldsCorrect() {
        CreateUserRequestDTO dto = new CreateUserRequestDTO(
                "test@example.com",
                "password123",
                "STUDENT"
        );

        Set<ConstraintViolation<CreateUserRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldValidateEmailFormat() {
        CreateUserRequestDTO dto = new CreateUserRequestDTO(
                "invalid-email",
                "password123",
                "STUDENT"
        );

        Set<ConstraintViolation<CreateUserRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Invalid email format")));
    }

    @Test
    void shouldValidateEmailNotEmpty() {
        CreateUserRequestDTO dto = new CreateUserRequestDTO(
                "",
                "password123",
                "STUDENT"
        );

        Set<ConstraintViolation<CreateUserRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Email is required")));
    }

    @Test
    void shouldValidatePasswordLength() {
        CreateUserRequestDTO dto = new CreateUserRequestDTO(
                "test@example.com",
                "123", // Too short
                "STUDENT"
        );

        Set<ConstraintViolation<CreateUserRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Password must be at least 6 characters")));
    }

    @ParameterizedTest
    @ValueSource(strings = {"ADMIN", "STUDENT", "TEACHER"})
    void shouldAcceptValidRoles(String role) {
        CreateUserRequestDTO dto = new CreateUserRequestDTO(
                "test@example.com",
                "password123",
                role
        );

        Set<ConstraintViolation<CreateUserRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldRejectInvalidRole() {
        CreateUserRequestDTO dto = new CreateUserRequestDTO(
                "test@example.com",
                "password123",
                "INVALID_ROLE"
        );

        Set<ConstraintViolation<CreateUserRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("ADMIN, STUDENT or TEACHER")));
    }

    @Test
    void getRoleAsEnum_ShouldConvertToEnum() {
        CreateUserRequestDTO dto = new CreateUserRequestDTO(
                "test@example.com",
                "password123",
                "STUDENT"
        );

        assertEquals(Role.STUDENT, dto.getRoleAsEnum());
    }

    @Test
    void getRoleAsEnum_ShouldThrowException_WhenRoleIsNull() {
        CreateUserRequestDTO dto = new CreateUserRequestDTO();
        dto.setRole(null);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                dto::getRoleAsEnum);
        assertTrue(ex.getMessage().contains("Role is null or empty"));
    }

    @Test
    void shouldSetAndGetAllProperties() {
        CreateUserRequestDTO dto = new CreateUserRequestDTO();

        dto.setEmail("test@example.com");
        dto.setPassword("password123");
        dto.setRole("TEACHER");
        dto.setFirstName("John");
        dto.setLastName("Doe");

        assertEquals("test@example.com", dto.getEmail());
        assertEquals("password123", dto.getPassword());
        assertEquals("TEACHER", dto.getRole());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
    }
}
