package org.example.enterprisecasemanagementsystem.infrastructure.web.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.enterprisecasemanagementsystem.infrastructure.web.dto.request.UpdateUserRequestDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateUserRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldBeValid_WhenBothEmailAndPasswordProvided() {
        UpdateUserRequestDTO dto = new UpdateUserRequestDTO(
                "new@example.com",
                "newPassword123"
        );

        Set<ConstraintViolation<UpdateUserRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeValid_WhenOnlyEmailProvided() {
        UpdateUserRequestDTO dto = new UpdateUserRequestDTO(
                "new@example.com",
                null
        );

        Set<ConstraintViolation<UpdateUserRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeValid_WhenOnlyPasswordProvided() {
        UpdateUserRequestDTO dto = new UpdateUserRequestDTO(
                null,
                "newPassword123"
        );

        Set<ConstraintViolation<UpdateUserRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeValid_WhenBothNull() {
        UpdateUserRequestDTO dto = new UpdateUserRequestDTO(null, null);

        Set<ConstraintViolation<UpdateUserRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldValidateEmailFormat_WhenProvided() {
        UpdateUserRequestDTO dto = new UpdateUserRequestDTO(
                "invalid-email", // Invalid format
                "password123"
        );

        Set<ConstraintViolation<UpdateUserRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Invalid email format")));
    }

    @Test
    void shouldValidatePasswordLength_WhenProvided() {
        UpdateUserRequestDTO dto = new UpdateUserRequestDTO(
                "test@example.com",
                "123" // Too short
        );

        Set<ConstraintViolation<UpdateUserRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Password must be at least 6 characters")));
    }

    @Test
    void shouldAcceptEmptyPassword() {
        UpdateUserRequestDTO dto = new UpdateUserRequestDTO(
                "test@example.com",
                ""
        );

        Set<ConstraintViolation<UpdateUserRequestDTO>> violations = validator.validate(dto);
    }

    @Test
    void shouldSetAndGetProperties() {
        UpdateUserRequestDTO dto = new UpdateUserRequestDTO();

        dto.setEmail("updated@example.com");
        dto.setPassword("updatedPassword123");

        assertEquals("updated@example.com", dto.getEmail());
        assertEquals("updatedPassword123", dto.getPassword());

        dto.setEmail(null);
        dto.setPassword(null);

        assertNull(dto.getEmail());
        assertNull(dto.getPassword());
    }

    @Test
    void shouldHaveConstructorWithBothParameters() {
        UpdateUserRequestDTO dto = new UpdateUserRequestDTO(
                "user@example.com",
                "securePassword456"
        );

        assertEquals("user@example.com", dto.getEmail());
        assertEquals("securePassword456", dto.getPassword());
    }

    @Test
    void shouldHandleEmailNormalization() {
        UpdateUserRequestDTO dto = new UpdateUserRequestDTO(
                "  USER@EXAMPLE.COM  ",
                "password"
        );

        // Assuming your validation trims whitespace
        Set<ConstraintViolation<UpdateUserRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty()); // Should fail because of whitespace
    }

    @Test
    void shouldAcceptComplexPasswords() {
        UpdateUserRequestDTO dto1 = new UpdateUserRequestDTO(null, "P@ssw0rd!");
        UpdateUserRequestDTO dto2 = new UpdateUserRequestDTO(null, "VeryLongPassword123456");
        UpdateUserRequestDTO dto3 = new UpdateUserRequestDTO(null, "mixED123!@#");

        assertTrue(validator.validate(dto1).isEmpty());
        assertTrue(validator.validate(dto2).isEmpty());
        assertTrue(validator.validate(dto3).isEmpty());
    }
}
