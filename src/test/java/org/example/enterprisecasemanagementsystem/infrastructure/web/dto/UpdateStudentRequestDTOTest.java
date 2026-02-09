package org.example.enterprisecasemanagementsystem.infrastructure.web.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.enterprisecasemanagementsystem.infrastructure.web.dto.request.UpdateStudentRequestDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateStudentRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldBeValid_WhenGroupNameProvided() {
        UpdateStudentRequestDTO dto = new UpdateStudentRequestDTO("CS-102");

        Set<ConstraintViolation<UpdateStudentRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldNotBeValid_WhenEmptyGroupName() {
        UpdateStudentRequestDTO dto = new UpdateStudentRequestDTO("");

        Set<ConstraintViolation<UpdateStudentRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("between 2 and 20 characters")));
    }

    @Test
    void shouldBeValid_WhenNullGroupName() {
        UpdateStudentRequestDTO dto = new UpdateStudentRequestDTO(null);

        Set<ConstraintViolation<UpdateStudentRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldValidateGroupNameLength_WhenProvided() {
        UpdateStudentRequestDTO dto = new UpdateStudentRequestDTO("A"); // Too short

        Set<ConstraintViolation<UpdateStudentRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("between 2 and 20 characters")));
    }

    @Test
    void shouldAcceptValidGroupNames() {
        UpdateStudentRequestDTO dto1 = new UpdateStudentRequestDTO("CS-101");
        UpdateStudentRequestDTO dto2 = new UpdateStudentRequestDTO("AI_202");
        UpdateStudentRequestDTO dto3 = new UpdateStudentRequestDTO("MATH-303");

        assertTrue(validator.validate(dto1).isEmpty());
        assertTrue(validator.validate(dto2).isEmpty());
        assertTrue(validator.validate(dto3).isEmpty());
    }

    @Test
    void shouldSetAndGetGroupName() {
        UpdateStudentRequestDTO dto = new UpdateStudentRequestDTO();

        dto.setGroupName("NEW-GROUP-101");
        assertEquals("NEW-GROUP-101", dto.getGroupName());

        dto.setGroupName(null);
        assertNull(dto.getGroupName());

        dto.setGroupName("");
        assertEquals("", dto.getGroupName());
    }

    @Test
    void shouldHaveConstructorWithGroupName() {
        UpdateStudentRequestDTO dto = new UpdateStudentRequestDTO("CS-102");
        assertEquals("CS-102", dto.getGroupName());
    }

    @Test
    void shouldHaveNoArgsConstructor() {
        UpdateStudentRequestDTO dto = new UpdateStudentRequestDTO();
        assertNotNull(dto);
        assertNull(dto.getGroupName());
    }

    @Test
    void shouldBeValidOnlyForNull_InPartialUpdates() {
        UpdateStudentRequestDTO dto1 = new UpdateStudentRequestDTO(null);

        UpdateStudentRequestDTO dto2 = new UpdateStudentRequestDTO("");
        UpdateStudentRequestDTO dto3 = new UpdateStudentRequestDTO("  ");

        assertTrue(validator.validate(dto1).isEmpty());
        assertFalse(validator.validate(dto2).isEmpty());
        assertFalse(validator.validate(dto3).isEmpty());
    }
}
