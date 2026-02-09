package org.example.enterprisecasemanagementsystem.infrastructure.web.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.enterprisecasemanagementsystem.infrastructure.web.dto.request.CreateTeacherRequestDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CreateTeacherRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldBeValid_WhenAllFieldsCorrect() {
        CreateTeacherRequestDTO dto = new CreateTeacherRequestDTO(
                "John",
                "Doe",
                "Computer Science",
                1L
        );

        Set<ConstraintViolation<CreateTeacherRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldValidateFirstNameNotEmpty() {
        CreateTeacherRequestDTO dto = new CreateTeacherRequestDTO(
                "", // Empty
                "Doe",
                "Computer Science",
                1L
        );

        Set<ConstraintViolation<CreateTeacherRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("First name is required")));
    }

    @Test
    void shouldValidateFirstNameLength() {
        CreateTeacherRequestDTO dto = new CreateTeacherRequestDTO(
                "J", // Too short
                "Doe",
                "Computer Science",
                1L
        );

        Set<ConstraintViolation<CreateTeacherRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("between 2 and 50 characters")));
    }

    @Test
    void shouldValidateUserIdNotNull() {
        CreateTeacherRequestDTO dto = new CreateTeacherRequestDTO(
                "John",
                "Doe",
                "Computer Science",
                null // Null user ID
        );

        Set<ConstraintViolation<CreateTeacherRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("User ID is required")));
    }

    @Test
    void shouldSetAndGetProperties() {
        CreateTeacherRequestDTO dto = new CreateTeacherRequestDTO();

        dto.setFirstName("Jane");
        dto.setLastName("Smith");
        dto.setDepartment("Mathematics");
        dto.setUserId(2L);

        assertEquals("Jane", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
        assertEquals("Mathematics", dto.getDepartment());
        assertEquals(2L, dto.getUserId());
    }

    @Test
    void shouldHaveConstructor() {
        CreateTeacherRequestDTO dto = new CreateTeacherRequestDTO(
                "Alice",
                "Johnson",
                "Physics",
                3L
        );

        assertEquals("Alice", dto.getFirstName());
        assertEquals("Johnson", dto.getLastName());
        assertEquals("Physics", dto.getDepartment());
        assertEquals(3L, dto.getUserId());
    }
}
