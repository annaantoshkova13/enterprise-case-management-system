package org.example.enterprisecasemanagementsystem.infrastructure.web.response;

import org.example.enterprisecasemanagementsystem.domain.*;
import org.example.enterprisecasemanagementsystem.infrastructure.web.dto.response.CourseResponseDTO;
import org.example.enterprisecasemanagementsystem.infrastructure.web.dto.response.TeacherResponseDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class CourseResponseDTOTest {

    @Test
    void fromEntity_ShouldConvertCourseToDTO() {
        User user = new User("teacher@example.com", "password", Role.TEACHER);
        Teacher teacher = new Teacher("John", "Doe", "Computer Science", user);
        teacher.setId(1L);

        Course course = new Course("Mathematics", "Math course", teacher, 30);
        course.setId(2L);

        Student student = new Student("Alice", "Smith", "CS-101", null);
        student.setId(3L);
        course.enrollStudent(student);

        CourseResponseDTO dto = CourseResponseDTO.fromEntity(course);

        assertEquals(2L, dto.getId()); // Теперь будет 2L
        assertEquals("Mathematics", dto.getTitle());
        assertEquals("Math course", dto.getDescription());
        assertEquals(30, dto.getMaxStudents());
        assertEquals(1, dto.getCurrentEnrollment());
        assertNotNull(dto.getCreatedAt());
        assertNotNull(dto.getTeacher());
        assertEquals("John", dto.getTeacher().getFirstName());
        assertNotNull(dto.getEnrolledStudentIds());
        assertTrue(dto.getEnrolledStudentIds().contains(3L));
    }

    @Test
    void fromEntity_ShouldHandleEmptyEnrolledStudents() {
        User user = new User("teacher@example.com", "password", Role.TEACHER);
        Teacher teacher = new Teacher("Jane", "Smith", "Physics", user);

        Course course = new Course("Physics", "Physics course", teacher, 25);
        course.setId(1L);

        CourseResponseDTO dto = CourseResponseDTO.fromEntity(course);

        assertNotNull(dto.getEnrolledStudentIds());
        assertTrue(dto.getEnrolledStudentIds().isEmpty());
        assertEquals(0, dto.getCurrentEnrollment());
    }

    @Test
    void fromEntity_ShouldHandleNullTeacher() {
        Course course = new Course("Chemistry", "Chemistry course", null, 20);
        course.setId(1L);

        CourseResponseDTO dto = CourseResponseDTO.fromEntity(course);

        assertNull(dto.getTeacher());
        assertEquals("Chemistry", dto.getTitle());
        assertEquals(20, dto.getMaxStudents());
    }

    @Test
    void shouldSetAndGetAllProperties() {
        CourseResponseDTO dto = new CourseResponseDTO();
        TeacherResponseDTO teacherDTO = new TeacherResponseDTO();
        LocalDateTime now = LocalDateTime.now();
        Set<Long> studentIds = new HashSet<>();
        studentIds.add(1L);
        studentIds.add(2L);

        dto.setId(1L);
        dto.setTitle("Mathematics");
        dto.setDescription("Advanced math");
        dto.setMaxStudents(30);
        dto.setCreatedAt(now);
        dto.setTeacher(teacherDTO);
        dto.setEnrolledStudentIds(studentIds);

        assertEquals(1L, dto.getId());
        assertEquals("Mathematics", dto.getTitle());
        assertEquals("Advanced math", dto.getDescription());
        assertEquals(30, dto.getMaxStudents());
        assertEquals(2, dto.getCurrentEnrollment());
        assertEquals(now, dto.getCreatedAt());
        assertEquals(teacherDTO, dto.getTeacher());
        assertEquals(studentIds, dto.getEnrolledStudentIds());
        assertEquals(2, dto.getEnrolledStudentIds().size());
    }

    @Test
    void shouldHaveAllArgsConstructor() {
        TeacherResponseDTO teacherDTO = new TeacherResponseDTO();
        LocalDateTime createdAt = LocalDateTime.now();
        Set<Long> studentIds = Set.of(1L, 2L);

        CourseResponseDTO dto = new CourseResponseDTO(
                1L,
                "Mathematics",
                "Math course",
                createdAt,
                30,
                teacherDTO,
                studentIds
        );

        assertEquals(1L, dto.getId());
        assertEquals("Mathematics", dto.getTitle());
        assertEquals("Math course", dto.getDescription());
        assertEquals(30, dto.getMaxStudents());
        assertEquals(2, dto.getCurrentEnrollment());
        assertEquals(createdAt, dto.getCreatedAt());
        assertEquals(teacherDTO, dto.getTeacher());
        assertEquals(studentIds, dto.getEnrolledStudentIds());
    }
}
