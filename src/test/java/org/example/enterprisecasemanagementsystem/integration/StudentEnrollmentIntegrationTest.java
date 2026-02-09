package org.example.enterprisecasemanagementsystem.integration;

import org.example.enterprisecasemanagementsystem.application.course.EnrollStudentUseCase;
import org.example.enterprisecasemanagementsystem.application.course.UnenrollStudentUseCase;
import org.example.enterprisecasemanagementsystem.domain.*;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.CourseRepository;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.StudentRepository;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.TeacherRepository;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class StudentEnrollmentIntegrationTest {

    @Autowired private EnrollStudentUseCase enrollStudentUseCase;
    @Autowired private UnenrollStudentUseCase unenrollStudentUseCase;
    @Autowired private CourseRepository courseRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TeacherRepository teacherRepository;

    @Test
    void shouldEnrollAndUnenrollStudent() {
        User teacherUser = new User("teacher@uni.com", "password123", Role.TEACHER);
        User studentUser = new User("student@uni.com", "password456", Role.STUDENT);

        userRepository.save(teacherUser);
        userRepository.save(studentUser);

        Teacher teacher = new Teacher("John", "Doe", "CS", teacherUser);
        teacherRepository.save(teacher);

        Course course = new Course("Math", "Desc", teacher, 10);
        courseRepository.save(course);

        Student student = new Student("Alice", "Smith", "CS-101", studentUser);
        studentRepository.save(student);

        Course enrolledCourse = enrollStudentUseCase.execute(course.getId(), student.getId());

        assertNotNull(enrolledCourse);
        assertTrue(enrolledCourse.getEnrolledStudents().contains(student));
        assertEquals(1, enrolledCourse.getCurrentEnrollment());

        Course unenrolledCourse = unenrollStudentUseCase.execute(course.getId(), student.getId());

        assertNotNull(unenrolledCourse);
        assertFalse(unenrolledCourse.getEnrolledStudents().contains(student));
        assertEquals(0, unenrolledCourse.getCurrentEnrollment());
    }

    @Test
    void shouldThrowWhenEnrollingNonExistentStudent() {
        User teacherUser = new User("teacher2@uni.com", "password123", Role.TEACHER);
        userRepository.save(teacherUser);

        Teacher teacher = new Teacher("Jane", "Doe", "CS", teacherUser);
        teacherRepository.save(teacher);

        Course course = new Course("Physics", "Physics course", teacher, 10);
        courseRepository.save(course);

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> enrollStudentUseCase.execute(course.getId(), 999L));

        assertEquals("Student not found with id: '999'", exception.getMessage());
    }

    @Test
    void shouldThrowWhenEnrollingToNonExistentCourse() {
        User studentUser = new User("student2@uni.com", "password789", Role.STUDENT);
        userRepository.save(studentUser);

        Student student = new Student("Bob", "Johnson", "CS-102", studentUser);
        studentRepository.save(student);

        Exception exception = assertThrows(ResourceNotFoundException.class,
                () -> enrollStudentUseCase.execute(999L, student.getId()));

        assertEquals("Course not found with id: '999'", exception.getMessage());
    }
}
