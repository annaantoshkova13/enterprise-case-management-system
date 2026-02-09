package org.example.enterprisecasemanagementsystem.application.course;

import org.example.enterprisecasemanagementsystem.domain.Course;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.CourseRepository;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.BusinessException;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.domain.Student;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.StudentRepository;
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
class EnrollStudentUseCaseTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private EnrollStudentUseCase enrollStudentUseCase;

    private Course mockCourse;
    private Student mockStudent;

    @BeforeEach
    void setUp() {
        mockCourse = new Course("Math", "Math course", null, 30);
        mockCourse.setId(1L);

        mockStudent = new Student("Alice", "Smith", "CS-101", null);
        mockStudent.setId(100L);
    }

    @Test
    void shouldEnrollStudent_WhenCourseAndStudentExist() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(mockCourse));
        when(studentRepository.findById(100L)).thenReturn(Optional.of(mockStudent));
        when(courseRepository.save(any(Course.class))).thenReturn(mockCourse);

        Course result = enrollStudentUseCase.execute(1L, 100L);

        assertNotNull(result);
        assertTrue(result.getEnrolledStudents().contains(mockStudent));
        verify(courseRepository).findById(1L);
        verify(studentRepository).findById(100L);
        verify(courseRepository).save(mockCourse);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenCourseNotFound() {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> enrollStudentUseCase.execute(999L, 100L)
        );

        assertEquals("Course not found with id: '999'", ex.getMessage());
        verify(courseRepository).findById(999L);
        verify(studentRepository, never()).findById(any());
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenStudentNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(mockCourse));
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> enrollStudentUseCase.execute(1L, 999L)
        );

        assertEquals("Student not found with id: '999'", ex.getMessage());
        verify(courseRepository).findById(1L);
        verify(studentRepository).findById(999L);
    }

    @Test
    void shouldThrowBusinessException_WhenCourseIsFull() {
        Course fullCourse = new Course("Math", "Math course", null, 1);
        fullCourse.setId(1L);
        fullCourse.enrollStudent(mockStudent); // Already has 1 student

        when(courseRepository.findById(1L)).thenReturn(Optional.of(fullCourse));
        when(studentRepository.findById(101L)).thenReturn(Optional.of(mockStudent));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> enrollStudentUseCase.execute(1L, 101L)
        );

        assertTrue(ex.getMessage().contains("Course is full"));
    }

    @Test
    void shouldThrowBusinessException_WhenStudentAlreadyEnrolled() {
        mockCourse.enrollStudent(mockStudent); // Student already enrolled

        when(courseRepository.findById(1L)).thenReturn(Optional.of(mockCourse));
        when(studentRepository.findById(100L)).thenReturn(Optional.of(mockStudent));

        BusinessException ex = assertThrows(BusinessException.class,
                () -> enrollStudentUseCase.execute(1L, 100L)
        );

        assertTrue(ex.getMessage().contains("already enrolled"));
    }
}
