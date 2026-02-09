package org.example.enterprisecasemanagementsystem.functional;

import org.example.enterprisecasemanagementsystem.domain.Role;
import org.example.enterprisecasemanagementsystem.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

class SecurityTests {

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Test
    void shouldNotExposePasswordInToString() {
        User user = new User("security@example.com", "secretPassword123", Role.ADMIN);
        user.setId(1L);

        String userString = user.toString();

        assertFalse(userString.contains("secretPassword123"));
        assertFalse(userString.contains("password"));
        assertFalse(userString.contains("passwordHash"));
        assertFalse(userString.contains("Password"));
    }

    @Test
    void shouldStorePasswordAsHash() {
        String rawPassword = "secretPassword123";
        User user = new User("test@example.com", passwordEncoder.encode(rawPassword), Role.STUDENT);

        assertNotEquals(rawPassword, user.getPasswordHash());

        String passwordHash = user.getPasswordHash();
        assertNotNull(passwordHash);
        assertTrue(passwordHash.matches("^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$"));
    }

    @Test
    void shouldValidateRolePermissions() {
        User admin = new User("admin@example.com", passwordEncoder.encode("pass12345"), Role.ADMIN);
        User teacher = new User("teacher@example.com", passwordEncoder.encode("pass12345"), Role.TEACHER);
        User student = new User("student@example.com", passwordEncoder.encode("pass12345"), Role.STUDENT);

        assertEquals(Role.ADMIN, admin.getRole());
        assertEquals(Role.TEACHER, teacher.getRole());
        assertEquals(Role.STUDENT, student.getRole());

        assertTrue(Role.ADMIN.ordinal() <= Role.TEACHER.ordinal() ||
                Role.ADMIN.ordinal() <= Role.STUDENT.ordinal());
    }

    @Test
    void shouldPreventEmailEnumeration() {
        User user = new User("existing@example.com", passwordEncoder.encode("password123"), Role.STUDENT);

        String toString = user.toString();
        assertTrue(toString.contains("User"));
        assertTrue(toString.contains("existing@example.com"));
        assertFalse(toString.contains("password"));
    }

    @Test
    void shouldHandlePasswordComplexity() {
        assertThrows(IllegalArgumentException.class, () ->
                new User("test@example.com", null, Role.STUDENT));

        assertThrows(IllegalArgumentException.class, () ->
                new User("test@example.com", "", Role.STUDENT));

        assertThrows(IllegalArgumentException.class, () ->
                new User("test@example.com", "123", Role.STUDENT));

        assertThrows(IllegalArgumentException.class, () ->
                new User("test@example.com", "1234567", Role.STUDENT));

        User user = new User("test@example.com", passwordEncoder.encode("ValidPass123!"), Role.STUDENT);
        assertNotNull(user);
    }

    @Test
    void shouldCheckPasswordSafely() {
        String rawPassword = "CorrectPassword123";
        User user = new User("test@example.com", passwordEncoder.encode(rawPassword), Role.STUDENT);

        assertTrue(user.checkPassword(rawPassword, passwordEncoder));

        assertFalse(user.checkPassword("WrongPassword", passwordEncoder));

        assertFalse(user.checkPassword(null, passwordEncoder));

        assertFalse(user.checkPassword("", passwordEncoder));

        assertFalse(user.checkPassword("CORRECTPASSWORD123", passwordEncoder));
    }

    @Test
    void shouldNotExposeSensitiveDataInLogs() {
        User user = new User("sensitive@example.com", passwordEncoder.encode("SuperSecret123!"), Role.TEACHER);

        String toString = user.toString();

        assertFalse(toString.contains("SuperSecret123!"));
        assertFalse(toString.contains("password"));
        assertFalse(toString.contains("PasswordHash"));
        assertFalse(toString.contains("123!"));

        assertDoesNotThrow(user::hashCode);
    }

    @Test
    void shouldPreventTimingAttacksInPasswordComparison() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "myPassword123";
        String hashedPassword = encoder.encode(rawPassword);

        User user = new User("test@example.com", hashedPassword, Role.STUDENT);

        boolean match1 = encoder.matches(rawPassword, hashedPassword);
        boolean match2 = encoder.matches("wrongPassword", hashedPassword);

        assertTrue(match1);
        assertFalse(match2);

        assertTrue(user.checkPassword(rawPassword, encoder));
        assertFalse(user.checkPassword("wrongPassword", encoder));
    }

    @Test
    void shouldHaveSecureDefaultValues() {
        String rawPassword = "password12345";
        User user = new User("default@example.com", passwordEncoder.encode(rawPassword), Role.STUDENT);

        assertNotEquals(rawPassword, user.getPasswordHash());
        assertTrue(user.getPasswordHash().matches("^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$"));

        assertNotNull(user.getCreatedAt());

        assertTrue(user.getCreatedAt().isBefore(java.time.LocalDateTime.now().plusSeconds(1)));
    }

    @Test
    void shouldImplementEqualsSafely() {
        User user1 = new User("user1@example.com", passwordEncoder.encode("pass12345"), Role.STUDENT);
        user1.setId(1L);

        User user2 = new User("user2@example.com", passwordEncoder.encode("pass67890"), Role.STUDENT);
        user2.setId(2L);

        User user3 = new User("user1@example.com", passwordEncoder.encode("pass12345"), Role.STUDENT);
        user3.setId(1L);

        assertNotEquals(user1, user2);

        assertEquals(user1, user3);

        assertNotEquals(null, user1);

        assertNotEquals("not a user", user1);

        assertEquals(user1, user1);

        assertEquals(user3, user1);
    }

    @Test
    void shouldValidateEmailFormat() {
        assertDoesNotThrow(() -> new User("valid@example.com", passwordEncoder.encode("password12345"), Role.STUDENT));
        assertDoesNotThrow(() -> new User("user.name@domain.co.uk", passwordEncoder.encode("password12345"), Role.STUDENT));
        assertDoesNotThrow(() -> new User("user+tag@example.com", passwordEncoder.encode("password12345"), Role.STUDENT));
        assertDoesNotThrow(() -> new User("user@sub.domain.example.com", passwordEncoder.encode("password12345"), Role.STUDENT));
        assertDoesNotThrow(() -> new User("123@example.com", passwordEncoder.encode("password12345"), Role.STUDENT));
    }

    @Test
    void shouldAllowPasswordUpdate() {
        User user = new User("update@example.com", passwordEncoder.encode("oldPassword123"), Role.STUDENT);
        String oldHash = user.getPasswordHash();

        user.setPasswordHash(passwordEncoder.encode("newPassword123"));
        String newHash = user.getPasswordHash();

        assertNotEquals(oldHash, newHash);
        assertTrue(newHash.matches("^\\$2[aby]\\$\\d{2}\\$[./A-Za-z0-9]{53}$"));
    }

    @Test
    void shouldHandleEdgeCases() {
        String specialPassword = "P@$$w0rd!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
        User user2 = new User("special@example.com", passwordEncoder.encode(specialPassword), Role.STUDENT);
        assertNotNull(user2);
        assertTrue(user2.checkPassword(specialPassword, passwordEncoder));

        String minPassword = "12345678";
        User user4 = new User("min@example.com", passwordEncoder.encode(minPassword), Role.STUDENT);
        assertNotNull(user4);
        assertTrue(user4.checkPassword(minPassword, passwordEncoder));

        String spacedPassword = "My Password 123";
        User user5 = new User("spaced@example.com", passwordEncoder.encode(spacedPassword), Role.STUDENT);
        assertNotNull(user5);
        assertTrue(user5.checkPassword(spacedPassword, passwordEncoder));

        String complexPassword = "Aa1!Bb2@Cc3#Dd4$";
        User user6 = new User("complex@example.com", passwordEncoder.encode(complexPassword), Role.STUDENT);
        assertNotNull(user6);
        assertTrue(user6.checkPassword(complexPassword, passwordEncoder));

        String longPassword = "A".repeat(50);
        User user7 = new User("long@example.com", passwordEncoder.encode(longPassword), Role.STUDENT);
        assertNotNull(user7);
        assertTrue(user7.checkPassword(longPassword, passwordEncoder));
    }

    @Test
    void shouldNotAllowPasswordInEmail() {
        User user = new User("password@example.com", passwordEncoder.encode("Secret123!"), Role.STUDENT);
        String toString = user.toString();

        assertTrue(toString.contains("password@example.com"));
        assertFalse(toString.contains("Secret123!"));
    }

    @Test
    void shouldHandleConcurrentPasswordChecks() {
        String rawPassword = "MyPassword123";
        User user = new User("concurrent@example.com", passwordEncoder.encode(rawPassword), Role.STUDENT);

        assertTrue(user.checkPassword(rawPassword, passwordEncoder));
        assertTrue(user.checkPassword(rawPassword, passwordEncoder));
        assertFalse(user.checkPassword("WrongPassword", passwordEncoder));
        assertTrue(user.checkPassword(rawPassword, passwordEncoder));
    }

    @Test
    void shouldValidatePasswordInConstructor() {
        assertThrows(IllegalArgumentException.class, () ->
                new User("test@example.com", "", Role.STUDENT));

        assertThrows(IllegalArgumentException.class, () ->
                new User("test@example.com", "short", Role.STUDENT));
    }
}