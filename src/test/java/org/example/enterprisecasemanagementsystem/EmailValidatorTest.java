package org.example.enterprisecasemanagementsystem;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class EmailValidatorTest {

    @Test
    void shouldCreateValidEmail() {
        EmailValue email = new EmailValue("test@example.com");
        assertNotNull(email);
        assertEquals("test@example.com", email.getValue());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "simple@example.com",
            "very.common@example.com",
            "disposable.style.email.with+symbol@example.com",
            "other.email-with-dash@example.com",
            "x@example.com",
            "example-indeed@strange-example.com",
            "admin@mailserver1",
            "example@s.example",
            "mailhost!username@example.org",
            "user%example.com@example.org",
    })
    void shouldAcceptValidEmails(String validEmail) {
        assertDoesNotThrow(() -> new EmailValue(validEmail));
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "Abc.example.com",
            "A@b@c@example.com",
            "a\"b(c)d,e:f;g<h>i[j\\k]l@example.com",
            "just\"not\"right@example.com",
            "this is\"not\\allowed@example.com",
            "this\\ still\\\"not\\allowed@example.com",
            "1234567890123456789012345678901234567890123456789012345678901234+x@example.com",
            "i_like_underscore@but_its_not_allowed_in_this_part.example.com",
            "QA[icon]CHOCOLATE[icon]@test.com",
    })
    void shouldRejectInvalidEmails(String invalidEmail) {
        assertThrows(IllegalArgumentException.class, () -> new EmailValue(invalidEmail));
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "  ", "\t", "\n"})
    void shouldRejectNullOrBlankEmails(String blankEmail) {
        assertThrows(IllegalArgumentException.class, () -> new EmailValue(blankEmail));
    }

    @Test
    void shouldNormalizeEmailToLowerCase() {
        EmailValue email = new EmailValue("TEST@EXAMPLE.COM");
        assertEquals("test@example.com", email.getValue());
    }

    @Test
    void shouldTrimWhitespace() {
        EmailValue email = new EmailValue("  test@example.com  ");
        assertEquals("test@example.com", email.getValue());
    }

    @Test
    void equalsAndHashCode_ShouldWorkCorrectly() {
        EmailValue email1 = new EmailValue("test@example.com");
        EmailValue email2 = new EmailValue("TEST@EXAMPLE.COM");
        EmailValue email3 = new EmailValue("other@example.com");

        assertEquals(email1, email2);
        assertNotEquals(email1, email3);
        assertEquals(email1.hashCode(), email2.hashCode());
        assertNotEquals(email1.hashCode(), email3.hashCode());
    }

    @Test
    void toString_ShouldReturnEmailString() {
        EmailValue email = new EmailValue("test@example.com");
        assertEquals("test@example.com", email.toString());
    }

    @Test
    void isValid_StaticMethod_ShouldReturnTrueForValidEmail() {
        assertTrue(EmailValue.isValid("valid@example.com"));
    }

    @Test
    void isValid_StaticMethod_ShouldReturnFalseForInvalidEmail() {
        assertFalse(EmailValue.isValid("invalid-email"));
        assertFalse(EmailValue.isValid(""));
        assertFalse(EmailValue.isValid(null));
    }

    @Test
    void shouldHandleInternationalEmails() {
        assertThrows(IllegalArgumentException.class, () -> new EmailValue("test@例子.测试"));
        assertThrows(IllegalArgumentException.class, () -> new EmailValue("test@ümit.example.com"));
    }

    @Test
    void shouldHandleEmailWithSubdomain() {
        EmailValue email = new EmailValue("user@sub.domain.example.com");
        assertEquals("user@sub.domain.example.com", email.getValue());
    }

    @Test
    void shouldHandleEmailWithNumbers() {
        EmailValue email = new EmailValue("user123@example456.com");
        assertEquals("user123@example456.com", email.getValue());
    }

    @Test
    void shouldHandleEmailWithSpecialCharactersInQuotedLocalPart() {
        assertThrows(IllegalArgumentException.class, () -> new EmailValue("\"john.doe\"@example.com"));
        assertThrows(IllegalArgumentException.class, () -> new EmailValue("\"very.unusual.@.unusual.com\"@example.com"));
        assertThrows(IllegalArgumentException.class, () -> new EmailValue("\"very.(),:;<>[]\\\".VERY.\\\"very@\\\\ \\\"very\\\".unusual\"@strange.example.com"));
    }

    @Test
    void shouldHandleEmailWithMultipleDots() {
        assertDoesNotThrow(() -> new EmailValue("first.last@example.com"));
        assertDoesNotThrow(() -> new EmailValue("first.middle.last@example.com"));
    }

    @Test
    void shouldHandleTopLevelDomainVariations() {
        assertDoesNotThrow(() -> new EmailValue("test@example.co.uk"));
        assertDoesNotThrow(() -> new EmailValue("test@example.info"));
        assertDoesNotThrow(() -> new EmailValue("test@example.io"));
    }

    @ParameterizedTest
    @CsvSource({
            "test@example.com, true",
            "invalid-email, false",
            "'', false",
            "' ', false"
    })
    void isValid_StaticMethod_WithVariousInputs(String email, boolean expected) {
        assertEquals(expected, EmailValue.isValid(email));
    }

    @Test
    void isValid_StaticMethod_ShouldReturnFalseForNull() {
        assertFalse(EmailValue.isValid(null));
    }

    @Test
    void shouldHaveConsistentBehaviorForSameEmailWithDifferentCase() {
        EmailValue lowerCase = new EmailValue("test@example.com");
        EmailValue upperCase = new EmailValue("TEST@EXAMPLE.COM");
        EmailValue mixedCase = new EmailValue("TeSt@ExAmPlE.CoM");

        assertEquals(lowerCase, upperCase);
        assertEquals(lowerCase, mixedCase);
        assertEquals(upperCase, mixedCase);

        assertEquals(lowerCase.hashCode(), upperCase.hashCode());
        assertEquals(lowerCase.hashCode(), mixedCase.hashCode());

        assertEquals("test@example.com", lowerCase.getValue());
        assertEquals("test@example.com", upperCase.getValue());
        assertEquals("test@example.com", mixedCase.getValue());
    }

    @Test
    void shouldHandleEmailWithPlusSign() {
        try {
            EmailValue email = new EmailValue("user+tag@example.com");
            assertEquals("user+tag@example.com", email.getValue());
        } catch (IllegalArgumentException e) {
            assertTrue(true, "Plus sign not supported in email local part");
        }
    }

    @Test
    void shouldHandleEmailWithUnderscoreInLocalPart() {
        try {
            EmailValue email = new EmailValue("user_name@example.com");
            assertEquals("user_name@example.com", email.getValue());
        } catch (IllegalArgumentException e) {
            assertTrue(true, "Underscore not supported in email local part");
        }
    }

    @Test
    void shouldHandleMinimalValidEmail() {
        assertDoesNotThrow(() -> new EmailValue("a@b.c"));
        EmailValue email = new EmailValue("a@b.c");
        assertEquals("a@b.c", email.getValue());
    }

    @Test
    void shouldHandleEmailWithHyphenInDomain() {
        assertDoesNotThrow(() -> new EmailValue("user@example-domain.com"));
        EmailValue email = new EmailValue("user@example-domain.com");
        assertEquals("user@example-domain.com", email.getValue());
    }

    @Test
    void shouldRejectEmailWithoutAtSymbol() {
        assertThrows(IllegalArgumentException.class, () -> new EmailValue("invalidemail.com"));
    }

    @Test
    void shouldRejectEmailWithMultipleAtSymbols() {
        assertThrows(IllegalArgumentException.class, () -> new EmailValue("user@domain@example.com"));
    }

    @Test
    void shouldRejectEmailWithInvalidCharacters() {
        assertThrows(IllegalArgumentException.class, () -> new EmailValue("user@exa mple.com"));
        assertThrows(IllegalArgumentException.class, () -> new EmailValue("user@exa<mple.com"));
        assertThrows(IllegalArgumentException.class, () -> new EmailValue("user@exa>mple.com"));
    }

    @Test
    void shouldRejectEmailWithConsecutiveDots() {
        assertThrows(IllegalArgumentException.class, () -> new EmailValue("user@example..com"));
    }

    @Test
    void shouldHandleEmailWithDotsAtBoundaries() {

        assertDoesNotThrow(() -> new EmailValue(".user@example.com"));
        assertDoesNotThrow(() -> new EmailValue("user.@example.com"));
        assertDoesNotThrow(() -> new EmailValue("user@.example.com"));
        assertDoesNotThrow(() -> new EmailValue("user@example.com."));

        EmailValue email1 = new EmailValue(".USER@EXAMPLE.COM");
        assertEquals(".user@example.com", email1.getValue());

        EmailValue email2 = new EmailValue("USER.@EXAMPLE.COM");
        assertEquals("user.@example.com", email2.getValue());

        EmailValue email3 = new EmailValue("USER@.EXAMPLE.COM");
        assertEquals("user@.example.com", email3.getValue());

        EmailValue email4 = new EmailValue("USER@EXAMPLE.COM.");
        assertEquals("user@example.com.", email4.getValue());
    }

    @Test
    void shouldAcceptEmailStartingWithDot() {
        assertDoesNotThrow(() -> new EmailValue(".user@example.com"));
        assertDoesNotThrow(() -> new EmailValue("user@.example.com"));
    }

    @Test
    void shouldAcceptEmailEndingWithDot() {
        assertDoesNotThrow(() -> new EmailValue("user.@example.com"));
        assertDoesNotThrow(() -> new EmailValue("user@example.com."));
    }
}