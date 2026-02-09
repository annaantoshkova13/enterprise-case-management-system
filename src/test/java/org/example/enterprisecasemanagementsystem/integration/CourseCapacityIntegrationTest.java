package org.example.enterprisecasemanagementsystem.integration;

import org.example.enterprisecasemanagementsystem.application.course.EnrollStudentUseCase;
import org.example.enterprisecasemanagementsystem.application.course.UnenrollStudentUseCase;
import org.example.enterprisecasemanagementsystem.domain.*;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.BusinessException;
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
class CourseCapacityIntegrationTest {

    @Autowired private EnrollStudentUseCase enrollStudentUseCase;
    @Autowired private CourseRepository courseRepository;
    @Autowired private StudentRepository studentRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TeacherRepository teacherRepository;

    @Test
    void shouldThrowException_WhenEnrollingBeyondCapacity() {
        User teacherUser = new User("teacher@uni.com", "password123", Role.TEACHER); // Исправлено
        User student1User = new User("student1@uni.com", "password123", Role.STUDENT); // Исправлено
        User student2User = new User("student2@uni.com", "password123", Role.STUDENT); // Исправлено

        userRepository.save(teacherUser);
        userRepository.save(student1User);
        userRepository.save(student2User);

        Teacher teacher = new Teacher("John", "Doe", "CS", teacherUser);
        teacherRepository.save(teacher);

        Course course = new Course("Limited Course", "Only one spot", teacher, 1);
        courseRepository.save(course);

        Student student1 = new Student("Alice", "Smith", "CS-101", student1User);
        Student student2 = new Student("Bob", "Johnson", "CS-101", student2User);
        studentRepository.save(student1);
        studentRepository.save(student2);

        Course enrolledCourse = enrollStudentUseCase.execute(course.getId(), student1.getId());
        assertEquals(1, enrolledCourse.getCurrentEnrollment());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> enrollStudentUseCase.execute(course.getId(), student2.getId()));

        assertTrue(exception.getMessage().contains("Course is full"));
        assertEquals(1, courseRepository.findById(course.getId()).get().getCurrentEnrollment());
    }

    @Test
    void shouldHandleConcurrentEnrollments() {
        User teacherUser = new User("teacher@uni.com", "password123", Role.TEACHER); // Исправлено
        User student1User = new User("student1-concurrent@uni.com", "password123", Role.STUDENT); // Исправлено
        User student2User = new User("student2-concurrent@uni.com", "password123", Role.STUDENT); // Исправлено

        userRepository.save(teacherUser);
        userRepository.save(student1User);
        userRepository.save(student2User);

        Teacher teacher = new Teacher("John", "Doe", "CS", teacherUser);
        teacherRepository.save(teacher);

        Course course = new Course("Concurrent Course", "Test concurrent enrollments", teacher, 2);
        courseRepository.save(course);

        Student student1 = new Student("Alice", "Smith", "CS-101", student1User);
        Student student2 = new Student("Bob", "Johnson", "CS-101", student2User);
        studentRepository.save(student1);
        studentRepository.save(student2);

        Course enrolledCourse1 = enrollStudentUseCase.execute(course.getId(), student1.getId());
        Course enrolledCourse2 = enrollStudentUseCase.execute(course.getId(), student2.getId());

        assertEquals(2, enrolledCourse2.getCurrentEnrollment());
        assertTrue(enrolledCourse2.getEnrolledStudents().contains(student1));
        assertTrue(enrolledCourse2.getEnrolledStudents().contains(student2));
    }

    @Test
    void shouldPreventDoubleEnrollment() {
        User teacherUser = new User("teacher-double@uni.com", "password123", Role.TEACHER); // Исправлено
        User studentUser = new User("student-double@uni.com", "password123", Role.STUDENT); // Исправлено

        userRepository.save(teacherUser);
        userRepository.save(studentUser);

        Teacher teacher = new Teacher("John", "Doe", "CS", teacherUser);
        teacherRepository.save(teacher);

        Course course = new Course("Double Enrollment Course", "Test", teacher, 30);
        courseRepository.save(course);

        Student student = new Student("Alice", "Smith", "CS-101", studentUser);
        studentRepository.save(student);

        Course enrolledCourse = enrollStudentUseCase.execute(course.getId(), student.getId());
        assertEquals(1, enrolledCourse.getCurrentEnrollment());

        BusinessException exception = assertThrows(BusinessException.class,
                () -> enrollStudentUseCase.execute(course.getId(), student.getId()));

        assertTrue(exception.getMessage().contains("already enrolled"));
        assertEquals(1, courseRepository.findById(course.getId()).get().getCurrentEnrollment());
    }

    @Test
    void shouldUnenrollAndReEnrollStudent() {
        User teacherUser = new User("teacher-reenroll@uni.com", "password123", Role.TEACHER); // Исправлено
        User studentUser = new User("student-reenroll@uni.com", "password123", Role.STUDENT); // Исправлено

        userRepository.save(teacherUser);
        userRepository.save(studentUser);

        Teacher teacher = new Teacher("John", "Doe", "CS", teacherUser);
        teacherRepository.save(teacher);

        Course course = new Course("Re-enrollment Course", "Test", teacher, 30);
        courseRepository.save(course);

        Student student = new Student("Alice", "Smith", "CS-101", studentUser);
        studentRepository.save(student);

        Course enrolledCourse = enrollStudentUseCase.execute(course.getId(), student.getId());
        assertEquals(1, enrolledCourse.getCurrentEnrollment());

        UnenrollStudentUseCase unenrollStudentUseCase = new UnenrollStudentUseCase(courseRepository, studentRepository);
        Course unenrolledCourse = unenrollStudentUseCase.execute(course.getId(), student.getId());
        assertEquals(0, unenrolledCourse.getCurrentEnrollment());

        Course reEnrolledCourse = enrollStudentUseCase.execute(course.getId(), student.getId());
        assertEquals(1, reEnrolledCourse.getCurrentEnrollment());
    }
}