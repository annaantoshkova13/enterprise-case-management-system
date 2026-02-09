package org.example.enterprisecasemanagementsystem.integration;

import org.example.enterprisecasemanagementsystem.application.course.CreateCourseUseCase;
import org.example.enterprisecasemanagementsystem.application.course.GetCourseUseCase;
import org.example.enterprisecasemanagementsystem.application.course.ListCourseUseCase;
import org.example.enterprisecasemanagementsystem.application.course.UpdateCourseUseCase;
import org.example.enterprisecasemanagementsystem.domain.Course;
import org.example.enterprisecasemanagementsystem.domain.Role;
import org.example.enterprisecasemanagementsystem.domain.Teacher;
import org.example.enterprisecasemanagementsystem.domain.User;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.CourseRepository;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.TeacherRepository;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class TeacherCourseIntegrationTest {

    @Autowired private CreateCourseUseCase createCourseUseCase;
    @Autowired private GetCourseUseCase getCourseUseCase;
    @Autowired private ListCourseUseCase listCourseUseCase;
    @Autowired private TeacherRepository teacherRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private CourseRepository courseRepository;

    private static final String TEST_PASSWORD = "password123";
    private static final String TEST_PASSWORD_ALT = "password456";

    @Test
    void shouldCreateCourseWithTeacher() {
        User teacherUser = new User("teacher-create@uni.com", TEST_PASSWORD, Role.TEACHER);
        userRepository.save(teacherUser);

        Teacher teacher = new Teacher("John", "Doe", "Computer Science", teacherUser);
        teacherRepository.save(teacher);

        Course course = createCourseUseCase.execute(
                "Advanced Mathematics",
                "Advanced math course for CS majors",
                teacher.getId(),
                25
        );

        assertNotNull(course);
        assertEquals("Advanced Mathematics", course.getTitle());
        assertEquals(teacher, course.getTeacher());
        assertEquals(25, course.getMaxStudents());

        Course fetchedCourse = getCourseUseCase.execute(course.getId());
        assertEquals("John", fetchedCourse.getTeacher().getFirstName());
        assertEquals("Computer Science", fetchedCourse.getTeacher().getDepartment());
    }

    @Test
    void shouldThrowException_WhenCreatingCourseWithNonExistentTeacher() {
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class,
                () -> createCourseUseCase.execute(
                        "Invalid Course",
                        "Course with non-existent teacher",
                        999L,
                        30
                )
        );

        assertEquals("Teacher not found with id: '999'", exception.getMessage());
    }

    @Test
    void shouldListAllTeacherCourses() {
        User teacherUser = new User("teacher-list@uni.com", TEST_PASSWORD, Role.TEACHER);
        userRepository.save(teacherUser);

        Teacher teacher = new Teacher("Jane", "Smith", "Physics", teacherUser);
        teacherRepository.save(teacher);

        // Create multiple courses for the same teacher
        Course course1 = new Course("Physics I", "Introductory physics", teacher, 30);
        Course course2 = new Course("Physics II", "Advanced physics", teacher, 25);
        Course course3 = new Course("Quantum Mechanics", "Quantum physics", teacher, 20);

        courseRepository.save(course1);
        courseRepository.save(course2);
        courseRepository.save(course3);

        User otherTeacherUser = new User("other-teacher@uni.com", TEST_PASSWORD_ALT, Role.TEACHER);
        userRepository.save(otherTeacherUser);
        Teacher otherTeacher = new Teacher("Bob", "Johnson", "Chemistry", otherTeacherUser);
        teacherRepository.save(otherTeacher);
        Course otherCourse = new Course("Chemistry", "Chemistry course", otherTeacher, 30);
        courseRepository.save(otherCourse);

        var allCourses = listCourseUseCase.execute();
        assertTrue(allCourses.size() >= 4);

        boolean hasPhysics1 = allCourses.stream()
                .anyMatch(c -> c.getTitle().equals("Physics I") && c.getTeacher().equals(teacher));
        boolean hasPhysics2 = allCourses.stream()
                .anyMatch(c -> c.getTitle().equals("Physics II") && c.getTeacher().equals(teacher));
        boolean hasQuantum = allCourses.stream()
                .anyMatch(c -> c.getTitle().equals("Quantum Mechanics") && c.getTeacher().equals(teacher));
        boolean hasChemistry = allCourses.stream()
                .anyMatch(c -> c.getTitle().equals("Chemistry") && c.getTeacher().equals(otherTeacher));

        assertTrue(hasPhysics1);
        assertTrue(hasPhysics2);
        assertTrue(hasQuantum);
        assertTrue(hasChemistry);
    }

    @Test
    void shouldUpdateCourseDetails() {
        User teacherUser = new User("teacher-update@uni.com", TEST_PASSWORD, Role.TEACHER);
        userRepository.save(teacherUser);

        Teacher teacher = new Teacher("John", "Doe", "Mathematics", teacherUser);
        teacherRepository.save(teacher);

        Course course = new Course("Old Title", "Old Description", teacher, 30);
        courseRepository.save(course);

        UpdateCourseUseCase updateCourseUseCase = new UpdateCourseUseCase(courseRepository);
        Course updatedCourse = updateCourseUseCase.execute(
                course.getId(),
                "New Title",
                "New Description"
        );

        assertEquals("New Title", updatedCourse.getTitle());
        assertEquals("New Description", updatedCourse.getDescription());
        assertEquals(teacher, updatedCourse.getTeacher());
        assertEquals(30, updatedCourse.getMaxStudents());
    }
}