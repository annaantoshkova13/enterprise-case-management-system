package org.example.enterprisecasemanagementsystem;

import org.example.enterprisecasemanagementsystem.user.User;
import org.example.enterprisecasemanagementsystem.course.Course;
import org.example.enterprisecasemanagementsystem.exception.BusinessException;
import org.example.enterprisecasemanagementsystem.student.Student;
import org.example.enterprisecasemanagementsystem.teacher.Teacher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CourseTest {

    private Teacher teacher;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User("teacher@school.com", "password", Role.TEACHER);
        teacher = new Teacher("John", "Doe", "Mathematics", user);
    }

    @Test
    public void shouldEnrollStudent_WhenSpaceAvailable() {
        Course course = new Course("Math", "Description", teacher, 30);
        Student student = new Student("Alice", "Smith", "Group-101", user);

        course.enrollStudent(student);

        assertTrue(course.isStudentEnrolled(student));
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    public void shouldThrowException_WhenCourseIsFull() {
        Course course = new Course("Math", "Description", teacher, 1);
        Student student1 = new Student("Alice", "Smith", "Group-101", user);
        Student student2 = new Student("Bob", "Johnson", "Group-101", user);

        course.enrollStudent(student1);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> course.enrollStudent(student2));

        assertEquals("Course is full. Maximum students: 1", ex.getMessage());
    }

    @Test
    public void shouldThrowException_WhenStudentAlreadyEnrolled() {
        Course course = new Course("Math", "Description", teacher, 30);
        Student student = new Student("Alice", "Smith", "Group-101", user);

        course.enrollStudent(student);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> course.enrollStudent(student));

        assertEquals("Student is already enrolled in this course", ex.getMessage());
    }

    @Test
    public void shouldUnenrollStudent() {
        Course course = new Course("Math", "Description", teacher, 30);
        Student student = new Student("Alice", "Smith", "Group-101", user);

        course.enrollStudent(student);
        assertTrue(course.isStudentEnrolled(student));

        course.unenrollStudent(student);
        assertFalse(course.isStudentEnrolled(student));
        assertEquals(0, course.getCurrentEnrollment());
    }

    @Test
    public void shouldCalculateAvailableSlots() {
        Course course = new Course("Math", "Description", teacher, 30);
        Student student = new Student("Alice", "Smith", "Group-101", user);

        assertEquals(30, course.getMaxStudents());
        assertTrue(course.hasAvailableSlots());

        course.enrollStudent(student);
        assertEquals(29, course.getMaxStudents() - course.getCurrentEnrollment());
    }

    @Test
    public void shouldUpdateCourseTitleAndDescription() {
        Course course = new Course("Old Title", "Old Description", teacher, 30);

        course.update("New Title", "New Description");

        assertEquals("New Title", course.getTitle());
        assertEquals("New Description", course.getDescription());
    }

    @Test
    public void shouldThrowException_WhenUpdateWithNullTitle() {
        Course course = new Course("Math", "Description", teacher, 30);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> course.update(null, "New Description"));

        assertEquals("Title cannot be empty", ex.getMessage());
    }

    @Test
    public void shouldThrowException_WhenUpdateWithBlankDescription() {
        Course course = new Course("Math", "Description", teacher, 30);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> course.update("New Title", ""));

        assertEquals("Description cannot be empty", ex.getMessage());
    }
}