package org.example.enterprisecasemanagementsystem;

import org.example.enterprisecasemanagementsystem.course.Course;
import org.example.enterprisecasemanagementsystem.course.CourseRepository;
import org.example.enterprisecasemanagementsystem.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.student.Student;
import org.example.enterprisecasemanagementsystem.student.StudentRepository;
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
class UnenrollStudentUseCaseTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private UnenrollStudentUseCase unenrollStudentUseCase;

    private Course mockCourse;
    private Student mockStudent;

    @BeforeEach
    void setUp() {
        mockCourse = new Course("Math", "Math course", null, 30);
        mockCourse.setId(1L);

        mockStudent = new Student("Alice", "Smith", "CS-101", null);
        mockStudent.setId(100L);

        mockCourse.enrollStudent(mockStudent); // Enroll student first
    }

    @Test
    void shouldUnenrollStudent_WhenStudentIsEnrolled() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(mockCourse));
        when(studentRepository.findById(100L)).thenReturn(Optional.of(mockStudent));
        when(courseRepository.save(any(Course.class))).thenReturn(mockCourse);

        Course result = unenrollStudentUseCase.execute(1L, 100L);

        assertNotNull(result);
        assertFalse(result.getEnrolledStudents().contains(mockStudent));
        verify(courseRepository).findById(1L);
        verify(studentRepository).findById(100L);
        verify(courseRepository).save(mockCourse);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenCourseNotFound() {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> unenrollStudentUseCase.execute(999L, 100L)
        );

        assertEquals("Course not found with id: '999'", ex.getMessage());
        verify(courseRepository).findById(999L);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenStudentNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(mockCourse));
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> unenrollStudentUseCase.execute(1L, 999L)
        );

        assertEquals("Student not found with id: '999'", ex.getMessage());
        verify(courseRepository).findById(1L);
        verify(studentRepository).findById(999L);
    }

    @Test
    void shouldHandleStudentNotEnrolled() {
        Course emptyCourse = new Course("Physics", "Physics course", null, 30);
        emptyCourse.setId(2L);

        when(courseRepository.findById(2L)).thenReturn(Optional.of(emptyCourse));
        when(studentRepository.findById(100L)).thenReturn(Optional.of(mockStudent));
        when(courseRepository.save(any(Course.class))).thenReturn(emptyCourse);

        Course result = unenrollStudentUseCase.execute(2L, 100L);

        assertNotNull(result);
        assertFalse(result.getEnrolledStudents().contains(mockStudent));
    }
}
