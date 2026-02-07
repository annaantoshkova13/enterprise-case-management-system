package org.example.enterprisecasemanagementsystem;

import org.example.enterprisecasemanagementsystem.course.*;
import org.example.enterprisecasemanagementsystem.teacher.*;
import org.example.enterprisecasemanagementsystem.user.*;
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
