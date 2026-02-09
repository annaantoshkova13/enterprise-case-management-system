package org.example.enterprisecasemanagementsystem.functional;

import org.example.enterprisecasemanagementsystem.domain.Course;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.CourseRepository;
import org.example.enterprisecasemanagementsystem.domain.Role;
import org.example.enterprisecasemanagementsystem.domain.Teacher;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.TeacherRepository;
import org.example.enterprisecasemanagementsystem.domain.User;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class PerformanceTests {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TeacherRepository teacherRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Test
    void shouldHandleMultipleUserCreations() {
        long startTime = System.currentTimeMillis();

        List<User> savedUsers = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            User user = new User("user" + i + "@example.com", "password" + i, Role.STUDENT);
            User savedUser = userRepository.save(user);
            savedUsers.add(savedUser);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertEquals(100, savedUsers.size());
        assertTrue(duration < 5000, "Batch save should complete within 5 seconds");

        List<User> allUsers = userRepository.findAll();
        long ourUsersCount = allUsers.stream()
                .filter(u -> u.getEmailString() != null && u.getEmailString().contains("@example.com"))
                .count();
        assertTrue(ourUsersCount >= 100);
    }

    @Test
    void shouldHandleMultipleCourseCreations() {
        User teacherUser = new User("performance-teacher@example.com", "password", Role.TEACHER);
        teacherUser = userRepository.save(teacherUser);
        Teacher teacher = new Teacher("Performance", "Teacher", "Department", teacherUser);
        teacher = teacherRepository.save(teacher);

        long startTime = System.currentTimeMillis();

        List<Course> savedCourses = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            Course course = new Course("Course " + i, "Description " + i, teacher, 30);
            Course savedCourse = courseRepository.save(course);
            savedCourses.add(savedCourse);
        }

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertEquals(50, savedCourses.size());
        assertTrue(duration < 3000, "Batch course save should complete within 3 seconds");
    }

    @Test
    void shouldHandleLargeCourseWithManyStudents() {
        User teacherUser = new User("large-course-teacher@example.com", "password", Role.TEACHER);
        teacherUser = userRepository.save(teacherUser);
        Teacher teacher = new Teacher("Large", "Course", "Department", teacherUser);
        teacher = teacherRepository.save(teacher);

        Course largeCourse = new Course("Large Course", "Course with many students", teacher, 100);
        largeCourse = courseRepository.save(largeCourse);

        for (int i = 0; i < 50; i++) {
            User studentUser = new User("student" + i + "-large@example.com", "password", Role.STUDENT);
            userRepository.save(studentUser);
        }

        assertTrue(largeCourse.getMaxStudents() == 100);
        assertEquals("Large Course", largeCourse.getTitle());
        assertEquals(teacher, largeCourse.getTeacher());
    }

    @Test
    void shouldHandleFastQueries() {
        User teacherUser = new User("query-teacher@example.com", "password", Role.TEACHER);
        teacherUser = userRepository.save(teacherUser);
        Teacher teacher = new Teacher("Query", "Test", "Department", teacherUser);
        teacher = teacherRepository.save(teacher);

        List<Course> courses = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            Course course = new Course("Query Course " + i, "Description", teacher, 30);
            course = courseRepository.save(course);
            courses.add(course);
        }

        long startTime = System.currentTimeMillis();

        List<Course> allCourses = courseRepository.findAll();
        Long firstCourseId = courses.get(0).getId();
        Course firstCourse = courseRepository.findById(firstCourseId).orElse(null);

        int count = allCourses.size();

        long endTime = System.currentTimeMillis();
        long duration = endTime - startTime;

        assertNotNull(firstCourse);
        assertNotNull(firstCourseId);
        assertTrue(allCourses.size() >= 20);
        assertTrue(count >= 20);
        assertTrue(duration < 1000, "Queries should complete within 1 second");
    }
}