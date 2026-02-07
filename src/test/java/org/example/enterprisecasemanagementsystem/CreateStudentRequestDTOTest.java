package org.example.enterprisecasemanagementsystem;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CreateStudentRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldBeValid_WhenAllFieldsCorrect() {
        CreateStudentRequestDTO dto = new CreateStudentRequestDTO(
                "Alice",
                "Smith",
                "CS-101",
                1L
        );

        Set<ConstraintViolation<CreateStudentRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldValidateFirstNameNotEmpty() {
        CreateStudentRequestDTO dto = new CreateStudentRequestDTO(
                "",
                "Smith",
                "CS-101",
                1L
        );

        Set<ConstraintViolation<CreateStudentRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("First name is required")));
    }

    @Test
    void shouldValidateFirstNameLength() {
        CreateStudentRequestDTO dto = new CreateStudentRequestDTO(
                "A",
                "Smith",
                "CS-101",
                1L
        );

        Set<ConstraintViolation<CreateStudentRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("between 2 and 50 characters")));
    }

    @Test
    void shouldValidateLastNameNotEmpty() {
        CreateStudentRequestDTO dto = new CreateStudentRequestDTO(
                "Alice",
                "",
                "CS-101",
                1L
        );

        Set<ConstraintViolation<CreateStudentRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Last name is required")));
    }

    @Test
    void shouldValidateGroupNameNotEmpty() {
        CreateStudentRequestDTO dto = new CreateStudentRequestDTO(
                "Alice",
                "Smith",
                "",
                1L
        );

        Set<ConstraintViolation<CreateStudentRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Group name is required")));
    }

    @Test
    void shouldValidateUserIdNotNull() {
        CreateStudentRequestDTO dto = new CreateStudentRequestDTO(
                "Alice",
                "Smith",
                "CS-101",
                null
        );

        Set<ConstraintViolation<CreateStudentRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("User ID is required")));
    }

    @Test
    void shouldSetAndGetProperties() {
        CreateStudentRequestDTO dto = new CreateStudentRequestDTO();

        dto.setFirstName("Bob");
        dto.setLastName("Johnson");
        dto.setGroupName("AI-201");
        dto.setUserId(2L);

        assertEquals("Bob", dto.getFirstName());
        assertEquals("Johnson", dto.getLastName());
        assertEquals("AI-201", dto.getGroupName());
        assertEquals(2L, dto.getUserId());
    }

    @Test
    void shouldAcceptValidGroupNames() {
        CreateStudentRequestDTO dto1 = new CreateStudentRequestDTO("Alice", "Smith", "CS-101", 1L);
        CreateStudentRequestDTO dto2 = new CreateStudentRequestDTO("Bob", "Johnson", "AI_202", 2L);
        CreateStudentRequestDTO dto3 = new CreateStudentRequestDTO("Charlie", "Brown", "MATH-303", 3L);

        assertTrue(validator.validate(dto1).isEmpty());
        assertTrue(validator.validate(dto2).isEmpty());
        assertTrue(validator.validate(dto3).isEmpty());
    }

    @Test
    void shouldHaveAllArgsConstructor() {
        CreateStudentRequestDTO dto = new CreateStudentRequestDTO(
                "Alice",
                "Smith",
                "CS-101",
                1L
        );

        assertEquals("Alice", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
        assertEquals("CS-101", dto.getGroupName());
        assertEquals(1L, dto.getUserId());
    }
}
