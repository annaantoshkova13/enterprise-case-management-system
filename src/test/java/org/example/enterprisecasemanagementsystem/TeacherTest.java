package org.example.enterprisecasemanagementsystem;


import org.example.enterprisecasemanagementsystem.teacher.Teacher;
import org.example.enterprisecasemanagementsystem.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class TeacherTest {

    private User user;
    private Teacher teacher;

    @BeforeEach
    public void setUp() {
        user = new User("teacher@school.com", "password", Role.TEACHER);
        teacher = new Teacher("Jon", "Jonson", "QA", user);
    }

    @Test
    @DisplayName("Should change department when valid department provided")
    public void shouldChangeDepartment_WhenValid() {
        teacher.changeDepartment("Java");
        assertEquals("Java", teacher.getDepartment());
    }

    @Test
    @DisplayName("Should preserve department when same department provided")
    public void shouldPreserveDepartment_WhenSameDepartment() {
        String originalDepartment = teacher.getDepartment();
        teacher.changeDepartment(originalDepartment);
        assertEquals(originalDepartment, teacher.getDepartment());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    @DisplayName("Should throw exception when department is null, empty or blank")
    public void shouldFail_WhenDepartmentIsInvalid(String invalidDepartment) {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> teacher.changeDepartment(invalidDepartment)
        );

        assertEquals("Department cannot be empty", ex.getMessage());
    }

    @Test
    @DisplayName("Should accept department with special characters")
    public void shouldAccept_DepartmentWithSpecialCharacters() {
        teacher.changeDepartment("Computer Science & Engineering");
        assertEquals("Computer Science & Engineering", teacher.getDepartment());
    }

    @Test
    @DisplayName("Should accept department with numbers")
    public void shouldAccept_DepartmentWithNumbers() {
        teacher.changeDepartment("CS101");
        assertEquals("CS101", teacher.getDepartment());
    }

    @Test
    @DisplayName("Should accept long department name")
    public void shouldAccept_LongDepartmentName() {
        String longDepartment = "Department of Computer Science and Information Technology";
        teacher.changeDepartment(longDepartment);
        assertEquals(longDepartment, teacher.getDepartment());
    }
}
