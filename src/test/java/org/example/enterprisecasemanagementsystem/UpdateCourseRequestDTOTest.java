package org.example.enterprisecasemanagementsystem;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UpdateCourseRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldBeValid_WhenBothTitleAndDescriptionProvided() {
        UpdateCourseRequestDTO dto = new UpdateCourseRequestDTO(
                "Updated Title",
                "Updated Description"
        );

        Set<ConstraintViolation<UpdateCourseRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeValid_WhenOnlyTitleProvided() {
        UpdateCourseRequestDTO dto = new UpdateCourseRequestDTO(
                "Updated Title",
                null
        );

        Set<ConstraintViolation<UpdateCourseRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeValid_WhenOnlyDescriptionProvided() {
        UpdateCourseRequestDTO dto = new UpdateCourseRequestDTO(
                null,
                "Updated Description"
        );

        Set<ConstraintViolation<UpdateCourseRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldBeValid_WhenBothNull() {
        UpdateCourseRequestDTO dto = new UpdateCourseRequestDTO(null, null);

        Set<ConstraintViolation<UpdateCourseRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldValidateTitleLength_WhenProvided() {
        UpdateCourseRequestDTO dto = new UpdateCourseRequestDTO(
                "AB",
                "Description"
        );

        Set<ConstraintViolation<UpdateCourseRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("between 3 and 100 characters")));
    }

    @Test
    void shouldValidateDescriptionLength_WhenProvided() {
        UpdateCourseRequestDTO dto = new UpdateCourseRequestDTO(
                "Title",
                "D"
        );

        Set<ConstraintViolation<UpdateCourseRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("between 3 and 500 characters")));
    }

    @Test
    void shouldSetAndGetProperties() {
        UpdateCourseRequestDTO dto = new UpdateCourseRequestDTO();

        dto.setTitle("New Title");
        dto.setDescription("New Description");

        assertEquals("New Title", dto.getTitle());
        assertEquals("New Description", dto.getDescription());

        dto.setTitle(null);
        dto.setDescription(null);

        assertNull(dto.getTitle());
        assertNull(dto.getDescription());
    }

    @Test
    void shouldHaveConstructorWithBothParameters() {
        UpdateCourseRequestDTO dto = new UpdateCourseRequestDTO(
                "Advanced Mathematics",
                "Advanced math course for seniors"
        );

        assertEquals("Advanced Mathematics", dto.getTitle());
        assertEquals("Advanced math course for seniors", dto.getDescription());
    }

    @Test
    void shouldHandleEmptyStrings() {
        UpdateCourseRequestDTO dto = new UpdateCourseRequestDTO("", "");

        Set<ConstraintViolation<UpdateCourseRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
    }

    @Test
    void shouldAcceptWhitespaceOnlyAsEmpty() {
        UpdateCourseRequestDTO dto = new UpdateCourseRequestDTO("   ", "   ");

        Set<ConstraintViolation<UpdateCourseRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());

    }
}
