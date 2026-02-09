package org.example.enterprisecasemanagementsystem.integration;

import org.example.enterprisecasemanagementsystem.domain.Course;
import org.example.enterprisecasemanagementsystem.domain.Role;
import org.example.enterprisecasemanagementsystem.domain.Teacher;
import org.example.enterprisecasemanagementsystem.domain.User;
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
class CourseIntegrationTest {

    @Autowired private CourseRepository courseRepository;
    @Autowired private TeacherRepository teacherRepository;
    @Autowired private UserRepository userRepository;

    @Test
    void shouldCreateCourseWithTeacherAndUser() {
        User user = new User("teacher@uni.com", "password", Role.TEACHER);
        userRepository.save(user);

        Teacher teacher = new Teacher("John", "Doe", "Computer Science", user);
        teacherRepository.save(teacher);

        Course course = new Course("Mathematics", "Math course", teacher, 30);
        courseRepository.save(course);

        Course saved = courseRepository.findById(course.getId()).orElseThrow();
        assertEquals("Mathematics", saved.getTitle());
        assertEquals("John", saved.getTeacher().getFirstName());
        assertEquals("teacher@uni.com", saved.getTeacher().getUser().getEmailString());
    }
}
