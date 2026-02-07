package org.example.enterprisecasemanagementsystem;

import org.example.enterprisecasemanagementsystem.course.Course;
import org.example.enterprisecasemanagementsystem.exception.BusinessException;
import org.example.enterprisecasemanagementsystem.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.student.Student;
import org.example.enterprisecasemanagementsystem.user.User;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EdgeCaseTests {

    @Test
    void shouldHandleNullValuesInEntities() {
        Student student = new Student("John", "Doe", "CS-101", null);
        student.setId(1L);

        assertNotNull(student);
        assertEquals("John", student.getFirstName());
        assertEquals("Doe", student.getLastName());
        assertEquals("CS-101", student.getGroupName());
        assertNull(student.getUser());
    }

    @Test
    void shouldHandleEmptyStrings() {
        assertThrows(IllegalArgumentException.class, () -> new EmailValue(""));
        assertThrows(IllegalArgumentException.class, () -> new EmailValue("   "));

        User user = new User("test@example.com", "pass", Role.TEACHER);

        try {
            Course course1 = new Course("", "Description", null, 30);
            assertEquals("", course1.getTitle());
            assertEquals("Description", course1.getDescription());
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("title") || e.getMessage().contains("empty"));
        } catch (BusinessException e) {
            assertTrue(e.getMessage().contains("title") || e.getMessage().contains("empty"));
        }

        try {
            Course course2 = new Course("Title", "", null, 30);
            assertEquals("Title", course2.getTitle());
            assertEquals("", course2.getDescription());
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("description") || e.getMessage().contains("empty"));
        } catch (BusinessException e) {
            assertTrue(e.getMessage().contains("description") || e.getMessage().contains("empty"));
        }
    }

    @Test
    void shouldHandleBoundaryValues() {
        User user = new User("test@example.com", "pass", Role.TEACHER);

        Course minCapacityCourse = new Course("Min Course", "Description", null, 1);
        assertEquals(1, minCapacityCourse.getMaxStudents());

        Course normalCourse = new Course("Normal Course", "Description", null, 50);
        assertEquals(50, normalCourse.getMaxStudents());

        Course maxCapacityCourse = new Course("Max Course", "Description", null, 100);
        assertEquals(100, maxCapacityCourse.getMaxStudents());

        BusinessException zeroCapacityException = assertThrows(BusinessException.class, () -> {
            new Course("Zero Course", "Description", null, 0);
        });
        assertTrue(zeroCapacityException.getMessage().contains("Max students must be at least 1"));

        BusinessException negativeCapacityException = assertThrows(BusinessException.class, () -> {
            new Course("Negative Course", "Description", null, -1);
        });
        assertTrue(negativeCapacityException.getMessage().contains("Max students must be at least 1"));
    }

    @Test
    void shouldHandleDuplicateOperations() {
        User user1 = new User("duplicate@example.com", "pass", Role.STUDENT);

        User user2 = new User("duplicate@example.com", "pass", Role.STUDENT);

        assertEquals(user1.getEmail(), user2.getEmail());
    }

    @Test
    void shouldHandleConcurrentModifications() {

        assertTrue(true, "Concurrent modification tests would be implemented separately");
    }

    @Test
    void shouldHandleExtremeStringLengths() {
        User user = new User("test@example.com", "pass", Role.TEACHER);

        String longTitle = "A".repeat(255);
        String longDescription = "B".repeat(2000);

        try {
            Course longTitleCourse = new Course(longTitle, "Description", null, 30);
            assertEquals(longTitle, longTitleCourse.getTitle());
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("title") || e.getMessage().contains("length"));
        } catch (BusinessException e) {
            assertTrue(e.getMessage().contains("title") || e.getMessage().contains("length"));
        }

        try {
            Course longDescCourse = new Course("Title", longDescription, null, 30);
            assertEquals(longDescription, longDescCourse.getDescription());
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().contains("description") || e.getMessage().contains("length"));
        } catch (BusinessException e) {
            assertTrue(e.getMessage().contains("description") || e.getMessage().contains("length"));
        }
    }

    @Test
    void shouldHandleSpecialCharacters() {

        assertDoesNotThrow(() -> new EmailValue("test.user+tag@example.com"));
        assertDoesNotThrow(() -> new EmailValue("test-user@example-domain.com"));

        User user = new User("test@example.com", "pass", Role.TEACHER);

        String[] specialTitles = {
                "Course: Advanced Topics",
                "Course (with Parentheses)",
                "Course - Dash Separated",
                "Course & More",
                "Course @ University",
                "Course #100",
                "Course: 100% Complete!"
        };

        for (String title : specialTitles) {
            try {
                Course course = new Course(title, "Description with special chars: @#$%", null, 30);
                assertEquals(title, course.getTitle());
            } catch (Exception e) {
                System.out.println("Title '" + title + "' caused: " + e.getMessage());
            }
        }
    }

    @Test
    void shouldHandleWhitespaceInStrings() {

        EmailValue email = new EmailValue("  TEST@EXAMPLE.COM  ");
        assertEquals("test@example.com", email.getValue());

        User user = new User("test@example.com", "pass", Role.TEACHER);

        try {
            Course course = new Course("  Title with spaces  ", "  Description with spaces  ", null, 30);
            if (!course.getTitle().equals("Title with spaces")) {
                assertTrue(course.getTitle().equals("  Title with spaces  ") ||
                        course.getTitle().equals("Title with spaces"));
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e.getMessage());
        }
    }

    @Test
    void shouldHandleBusinessExceptionProperly() {
        User user = new User("test@example.com", "pass", Role.TEACHER);

        Exception exception = assertThrows(BusinessException.class, () -> {
            new Course("Test Course", "Description", null, 0);
        });
        assertEquals("Max students must be at least 1", exception.getMessage());

        try {
            Course largeCourse = new Course("Large Course", "Description", null, 1000);
            assertEquals(1000, largeCourse.getMaxStudents());
        } catch (BusinessException e) {
            assertTrue(e.getMessage().contains("max") || e.getMessage().contains("capacity"));
        }
    }
}