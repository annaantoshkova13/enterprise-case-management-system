package org.example.enterprisecasemanagementsystem.infrastructure.web.dto;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.example.enterprisecasemanagementsystem.infrastructure.web.dto.request.CreateCourseRequestDTO;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CreateCourseRequestDTOTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void shouldBeValid_WhenAllFieldsCorrect() {
        CreateCourseRequestDTO dto = new CreateCourseRequestDTO(
                "Mathematics",
                "Math course description with at least 10 characters",
                1L,
                30
        );

        Set<ConstraintViolation<CreateCourseRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldValidateTitleNotEmpty() {
        CreateCourseRequestDTO dto = new CreateCourseRequestDTO(
                "",
                "Description with at least 10 characters",
                1L,
                30
        );

        Set<ConstraintViolation<CreateCourseRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Title is required")));
    }

    @Test
    void shouldValidateTitleLength() {
        CreateCourseRequestDTO dto = new CreateCourseRequestDTO(
                "M",
                "Description with at least 10 characters",
                1L,
                30
        );

        Set<ConstraintViolation<CreateCourseRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("between 3 and 100 characters")));
    }

    @Test
    void shouldValidateDescriptionNotEmpty() {
        CreateCourseRequestDTO dto = new CreateCourseRequestDTO(
                "Mathematics",
                "",
                1L,
                30
        );

        Set<ConstraintViolation<CreateCourseRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Description is required")));
    }

    @Test
    void shouldValidateDescriptionLength() {
        CreateCourseRequestDTO dto = new CreateCourseRequestDTO(
                "Mathematics",
                "Too short",
                1L,
                30
        );

        Set<ConstraintViolation<CreateCourseRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("between 10 and 1000 characters")));
    }

    @Test
    void shouldValidateTeacherIdNotNull() {
        CreateCourseRequestDTO dto = new CreateCourseRequestDTO(
                "Mathematics",
                "Description with at least 10 characters",
                null,
                30
        );

        Set<ConstraintViolation<CreateCourseRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().equals("Teacher ID is required")));
    }

    @Test
    void shouldValidateTeacherIdPositive() {
        CreateCourseRequestDTO dto = new CreateCourseRequestDTO(
                "Mathematics",
                "Description with at least 10 characters",
                0L,
                30
        );

        Set<ConstraintViolation<CreateCourseRequestDTO>> violations = validator.validate(dto);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream()
                .anyMatch(v -> v.getMessage().contains("Teacher ID must be positive")));
    }

    @Test
    void shouldValidateMaxStudentsRange() {
        CreateCourseRequestDTO dto1 = new CreateCourseRequestDTO(
                "Mathematics",
                "Description with at least 10 characters",
                1L,
                0
        );

        CreateCourseRequestDTO dto2 = new CreateCourseRequestDTO(
                "Mathematics",
                "Description with at least 10 characters",
                1L,
                101
        );

        Set<ConstraintViolation<CreateCourseRequestDTO>> violations1 = validator.validate(dto1);
        Set<ConstraintViolation<CreateCourseRequestDTO>> violations2 = validator.validate(dto2);

        assertFalse(violations1.isEmpty());
        assertFalse(violations2.isEmpty());
        assertTrue(violations1.stream()
                .anyMatch(v -> v.getMessage().contains("must be at least 1")));
        assertTrue(violations2.stream()
                .anyMatch(v -> v.getMessage().contains("cannot exceed 100")));
    }

    @Test
    void shouldAcceptNullMaxStudents_ForDefaultValue() {
        CreateCourseRequestDTO dto = new CreateCourseRequestDTO(
                "Mathematics",
                "Description with at least 10 characters",
                1L,
                null
        );

        Set<ConstraintViolation<CreateCourseRequestDTO>> violations = validator.validate(dto);
        assertTrue(violations.isEmpty());
    }

    @Test
    void shouldSetAndGetProperties() {
        CreateCourseRequestDTO dto = new CreateCourseRequestDTO();

        dto.setTitle("Physics");
        dto.setDescription("Physics course with at least 10 characters");
        dto.setTeacherId(2L);
        dto.setMaxStudents(25);

        assertEquals("Physics", dto.getTitle());
        assertEquals("Physics course with at least 10 characters", dto.getDescription());
        assertEquals(2L, dto.getTeacherId());
        assertEquals(25, dto.getMaxStudents());
    }

    @Test
    void shouldHaveAllArgsConstructor() {
        CreateCourseRequestDTO dto = new CreateCourseRequestDTO(
                "Chemistry",
                "Chemistry course with at least 10 characters",
                3L,
                20
        );

        assertEquals("Chemistry", dto.getTitle());
        assertEquals("Chemistry course with at least 10 characters", dto.getDescription());
        assertEquals(3L, dto.getTeacherId());
        assertEquals(20, dto.getMaxStudents());
    }

    @Test
    void shouldUseDefaultMaxStudents() {
        CreateCourseRequestDTO dto = new CreateCourseRequestDTO();

        assertEquals(30, dto.getMaxStudents());

        dto.setMaxStudents(null);
        assertEquals(30, dto.getMaxStudents());
    }
}
