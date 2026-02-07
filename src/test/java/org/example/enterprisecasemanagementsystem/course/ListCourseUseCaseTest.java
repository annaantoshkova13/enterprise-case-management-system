package org.example.enterprisecasemanagementsystem.course;

import org.example.enterprisecasemanagementsystem.teacher.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListCourseUseCaseTest {

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private ListCourseUseCase listCourseUseCase;

    @Test
    void shouldReturnAllCourses() {
        Course course1 = new Course("Math", "Math course", null, 30);
        Course course2 = new Course("Physics", "Physics course", null, 25);
        List<Course> courses = Arrays.asList(course1, course2);

        when(courseRepository.findAll()).thenReturn(courses);

        List<Course> result = listCourseUseCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(courseRepository).findAll();
    }

    @Test
    void shouldReturnEmptyList_WhenNoCourses() {
        when(courseRepository.findAll()).thenReturn(List.of());

        List<Course> result = listCourseUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(courseRepository).findAll();
    }

    @Test
    void shouldReturnCoursesWithTeacher() {
        Teacher teacher = new Teacher("John", "Doe", "CS", null);
        Course course = new Course("Math", "Math course", teacher, 30);

        when(courseRepository.findAll()).thenReturn(List.of(course));

        List<Course> result = listCourseUseCase.execute();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John", result.get(0).getTeacher().getFirstName());
    }
}
