package org.example.enterprisecasemanagementsystem.course;

import org.example.enterprisecasemanagementsystem.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.teacher.Teacher;
import org.example.enterprisecasemanagementsystem.teacher.TeacherRepository;
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
class CreateCourseUseCaseTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private CreateCourseUseCase createCourseUseCase;

    private Teacher mockTeacher;

    @BeforeEach
    void setUp() {
        mockTeacher = new Teacher("John", "Doe", "Computer Science", null);
        mockTeacher.setId(1L);
    }

    @Test
    void shouldCreateCourse_WithDefaultMaxStudents() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(mockTeacher));

        Course savedCourse = new Course("Mathematics", "Basic mathematics course", mockTeacher, 30);
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

        Course result = createCourseUseCase.execute(
                "Mathematics",
                "Basic mathematics course",
                1L
        );

        assertNotNull(result);
        assertEquals("Mathematics", result.getTitle());
        assertEquals("Basic mathematics course", result.getDescription());
        assertEquals(mockTeacher, result.getTeacher());
        assertEquals(30, result.getMaxStudents());
        verify(teacherRepository).findById(1L);
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void shouldCreateCourse_WithCustomMaxStudents() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(mockTeacher));

        Course savedCourse = new Course("Mathematics", "Basic mathematics course", mockTeacher, 50);
        when(courseRepository.save(any(Course.class))).thenReturn(savedCourse);

        Course result = createCourseUseCase.execute(
                "Mathematics",
                "Basic mathematics course",
                1L,
                50
        );

        assertNotNull(result);
        assertEquals("Mathematics", result.getTitle());
        assertEquals("Basic mathematics course", result.getDescription());
        assertEquals(mockTeacher, result.getTeacher());
        assertEquals(50, result.getMaxStudents());
        verify(teacherRepository).findById(1L);
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenTeacherNotFound() {
        when(teacherRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> createCourseUseCase.execute(
                        "Mathematics",
                        "Basic mathematics course",
                        999L
                )
        );

        assertEquals("Teacher not found with id: '999'", ex.getMessage());
        verify(teacherRepository).findById(999L);
        verify(courseRepository, never()).save(any());
    }
}
