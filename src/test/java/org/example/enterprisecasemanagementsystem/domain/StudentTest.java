package org.example.enterprisecasemanagementsystem.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class StudentTest {

    private User user;
    private Student student;

    @BeforeEach
    void setUp() {
        user = new User("student@university.com", "password", Role.STUDENT);
        student = new Student("Alice", "Smith", "CS-101", user);
    }

    @Test
    void shouldCreateStudent_WithValidParameters() {
        assertEquals("Alice", student.getFirstName());
        assertEquals("Smith", student.getLastName());
        assertEquals("CS-101", student.getGroupName());
        assertEquals(user, student.getUser());
    }

    @Test
    void shouldChangeGroup_WhenValid() {
        student.changeGroup("CS-102");
        assertEquals("CS-102", student.getGroupName());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void shouldThrowException_WhenGroupIsNullOrBlank(String invalidGroup) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> student.changeGroup(invalidGroup));
        assertEquals("Group cannot be empty", ex.getMessage());
    }

    @Test
    void shouldSetUser() {
        User newUser = new User("newstudent@university.com", "password", Role.STUDENT);
        student.setUser(newUser);
        assertEquals(newUser, student.getUser());
    }

    @Test
    void shouldSetGroupName() {
        student.setGroupName("AI-201");
        assertEquals("AI-201", student.getGroupName());
    }

    @Test
    void shouldHaveConstructor_WithUserAndGroup() {
        Student studentWithConstructor = new Student(user, "Group-101");
        assertEquals(user, studentWithConstructor.getUser());
        assertEquals("Group-101", studentWithConstructor.getGroupName());
    }

    @Test
    void shouldHaveConstructor_WithUserOnly() {
        Student studentWithUser = new Student(user);
        assertEquals(user, studentWithUser.getUser());
        assertNull(studentWithUser.getGroupName());
    }

    @Test
    void shouldHaveEmptyConstructor() {
        Student emptyStudent = new Student();
        assertNotNull(emptyStudent);
        assertNull(emptyStudent.getFirstName());
        assertNull(emptyStudent.getLastName());
        assertNull(emptyStudent.getGroupName());
        assertNull(emptyStudent.getUser());
    }
}
