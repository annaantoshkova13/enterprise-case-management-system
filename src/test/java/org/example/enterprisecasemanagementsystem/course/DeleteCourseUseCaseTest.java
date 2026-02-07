package org.example.enterprisecasemanagementsystem.course;

import org.example.enterprisecasemanagementsystem.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteCourseUseCaseTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private DeleteCourseUseCase deleteCourseUseCase;

    @Test
    void shouldDeleteCourse_WhenCourseExists() {
        when(courseRepository.existsById(1L)).thenReturn(true);

        deleteCourseUseCase.execute(1L);

        verify(courseRepository).existsById(1L);
        verify(courseRepository).deleteById(1L);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenCourseNotFound() {
        when(courseRepository.existsById(999L)).thenReturn(false);

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> deleteCourseUseCase.execute(999L)
        );

        assertEquals("Course not found with id: '999'", ex.getMessage());
        verify(courseRepository).existsById(999L);
        verify(courseRepository, never()).deleteById(any());
    }

}
