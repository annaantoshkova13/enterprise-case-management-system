package org.example.enterprisecasemanagementsystem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

class EmailValueTest {

    @Test
    void shouldCreateEmail_WhenValid() {
        EmailValue email = new EmailValue("test@example.com");
        assertEquals("test@example.com", email.getValue());
    }

    @Test
    void shouldNormalizeEmail_ToLowerCase() {
        EmailValue email = new EmailValue("TEST@EXAMPLE.COM");
        assertEquals("test@example.com", email.getValue());
    }

    @Test
    void shouldTrimEmail() {
        EmailValue email = new EmailValue("  test@example.com  ");
        assertEquals("test@example.com", email.getValue());
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "   "})
    void shouldThrowException_WhenEmailIsNullOrEmpty(String invalidEmail) {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> new EmailValue(invalidEmail));
        assertEquals("Email cannot be null or empty", ex.getMessage());
    }
    @ParameterizedTest
    @ValueSource(strings = {
            "invalid",
            "test@",
            "@example.com",
            "user@example..com"
    })
    void shouldThrowException_WhenEmailInvalid(String invalidEmail) {
        assertThrows(IllegalArgumentException.class,
                () -> new EmailValue(invalidEmail));
    }

    @Test
    void shouldBeEqual_WhenSameEmail() {
        EmailValue email1 = new EmailValue("test@example.com");
        EmailValue email2 = new EmailValue("test@example.com");
        assertEquals(email1, email2);
        assertEquals(email1.hashCode(), email2.hashCode());
    }

    @Test
    void shouldNotBeEqual_WhenDifferentEmail() {
        EmailValue email1 = new EmailValue("test1@example.com");
        EmailValue email2 = new EmailValue("test2@example.com");
        assertNotEquals(email1, email2);
    }

    @Test
    void testToString() {
        EmailValue email = new EmailValue("test@example.com");
        assertEquals("test@example.com", email.toString());
    }

    @Test
    void isValid_ShouldReturnTrueForValidEmail() {
        assertTrue(EmailValue.isValid("test@example.com"));
        assertTrue(EmailValue.isValid("test@localhost"));
    }

    @Test
    void isValid_ShouldReturnFalseForInvalidEmail() {
        assertFalse(EmailValue.isValid("invalid-email"));
    }

    @Test
    void debugEmailValidation() {
        System.out.println("Current EmailValue behavior:");

        String[] testEmails = {
                "test@example.com",
                "test@localhost",
                "admin@mailserver1",
                "user@example..com",
                "test@.com",
        };

        for (String email : testEmails) {
            try {
                EmailValue ev = new EmailValue(email);
                System.out.println("✓ " + email + " - ACCEPTED");
            } catch (IllegalArgumentException e) {
                System.out.println("✗ " + email + " - REJECTED: " + e.getMessage());
            }
        }
    }
}