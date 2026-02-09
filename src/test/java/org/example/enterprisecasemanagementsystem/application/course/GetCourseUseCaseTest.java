package org.example.enterprisecasemanagementsystem.application.course;

import org.example.enterprisecasemanagementsystem.application.course.GetCourseUseCase;
import org.example.enterprisecasemanagementsystem.domain.Course;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.CourseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetCourseUseCaseTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private GetCourseUseCase getCourseUseCase;

    @Test
    void shouldReturnCourse_WhenCourseExists() {
        Course course = new Course("Mathematics", "Math course", null, 30);
        course.setId(1L);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course result = getCourseUseCase.execute(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Mathematics", result.getTitle());
        verify(courseRepository).findById(1L);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenCourseNotFound() {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> getCourseUseCase.execute(999L)
        );

        assertEquals("Course not found with id: '999'", ex.getMessage());
        verify(courseRepository).findById(999L);
    }
}
