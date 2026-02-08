package org.example.enterprisecasemanagementsystem;

import org.example.enterprisecasemanagementsystem.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {

    private EmailValue email;
    private User user;

    @BeforeEach
    void setUp() {
        email = new EmailValue("test@example.com");
        user = new User(email, "hashedPassword123", Role.ADMIN);
    }

    @Test
    void shouldCreateUser_WithValidParameters() {
        assertEquals(email, user.getEmail());
        assertEquals("hashedPassword123", user.getPasswordHash());
        assertEquals(Role.ADMIN, user.getRole());

        assertNotNull(user.getCreatedAt());

        LocalDateTime now = LocalDateTime.now();
        assertTrue(user.getCreatedAt().isBefore(now) ||
                user.getCreatedAt().isEqual(now));
    }

    @Test
    void shouldCreateUser_WithStringEmail() {
        User userWithString = new User("user@example.com", "password", Role.STUDENT);
        assertEquals("user@example.com", userWithString.getEmailString());
        assertEquals(Role.STUDENT, userWithString.getRole());
    }

    @Test
    void shouldSetEmail_WithEmailValue() {
        EmailValue newEmail = new EmailValue("new@example.com");
        user.setEmail(newEmail);
        assertEquals(newEmail, user.getEmail());
    }

    @Test
    void shouldSetEmail_WithString() {
        user.setEmail("new@example.com");
        assertEquals("new@example.com", user.getEmailString());
    }

    @Test
    void shouldSetPasswordHash() {
        user.setPasswordHash("newHashedPassword");
        assertEquals("newHashedPassword", user.getPasswordHash());
    }

    @Test
    void shouldSetRole() {
        user.setRole(Role.TEACHER);
        assertEquals(Role.TEACHER, user.getRole());
    }

    @Test
    void shouldSetCreatedAt() {
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);
        assertEquals(now, user.getCreatedAt());
    }

    @Test
    void getEmailString_ShouldReturnString() {
        assertEquals("test@example.com", user.getEmailString());
    }

    @Test
    void getEmailString_ShouldReturnNull_WhenEmailIsNull() {
        User emptyUser = new User();
        assertNull(emptyUser.getEmailString());
    }

    @Test
    void shouldHaveEmptyConstructor() {
        User emptyUser = new User();
        assertNotNull(emptyUser);
        assertNull(emptyUser.getEmail());
        assertNull(emptyUser.getPasswordHash());
        assertNull(emptyUser.getRole());
    }
}
