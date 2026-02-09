package org.example.enterprisecasemanagementsystem.application.course;

import org.example.enterprisecasemanagementsystem.application.course.UpdateCourseUseCase;
import org.example.enterprisecasemanagementsystem.domain.Course;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateCourseUseCaseTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private UpdateCourseUseCase updateCourseUseCase;

    private Course mockCourse;

    @BeforeEach
    void setUp() {
        mockCourse = new Course("Old Title", "Old Description", null, 30);
        mockCourse.setId(1L);
    }

    @Test
    void shouldUpdateCourse_WhenCourseExists() {

        when(courseRepository.findById(1L)).thenReturn(Optional.of(mockCourse));
        when(courseRepository.save(any(Course.class))).thenReturn(mockCourse);

        Course result = updateCourseUseCase.execute(
                1L,
                "New Title",
                "New Description"
        );

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        assertEquals("New Description", result.getDescription());
        verify(courseRepository).findById(1L);
        verify(courseRepository).save(mockCourse);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenCourseNotFound() {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> updateCourseUseCase.execute(
                        999L,
                        "New Title",
                        "New Description"
                )
        );

        assertEquals("Course not found with id: '999'", ex.getMessage());
        verify(courseRepository).findById(999L);
        verify(courseRepository, never()).save(any());
    }

    @Test
    void shouldThrowIllegalArgumentException_WhenTitleIsEmpty() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(mockCourse));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> updateCourseUseCase.execute(
                        1L,
                        "",
                        "New Description"
                )
        );

        assertEquals("Title cannot be empty", ex.getMessage());
        verify(courseRepository).findById(1L);
        verify(courseRepository, never()).save(any());
    }

    @Test
    void shouldThrowIllegalArgumentException_WhenDescriptionIsEmpty() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(mockCourse));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> updateCourseUseCase.execute(
                        1L,
                        "New Title",
                        ""
                )
        );

        assertEquals("Description cannot be empty", ex.getMessage());
        verify(courseRepository).findById(1L);
        verify(courseRepository, never()).save(any());
    }
}
