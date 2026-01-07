package org.example.enterprisecasemanagementsystem;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.regex.Pattern;

@Embeddable
public class EmailValue implements Serializable {

    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");

    @Column(name = "email", nullable = false, unique = true)
    private String value;

    public EmailValue() {
        // Пустой конструктор для JPA
    }

    public EmailValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        String trimmedEmail = value.trim().toLowerCase();

        if (!EMAIL_PATTERN.matcher(trimmedEmail).matches()) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }

        this.value = trimmedEmail;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmailValue that = (EmailValue) o;
        return value != null ? value.equals(that.value) : that.value == null;
    }

    @Override
    public int hashCode() {
        return value != null ? value.hashCode() : 0;
    }

    @Override
    public String toString() {
        return value;
    }

    public static boolean isValid(String email) {
        if (email == null || email.isBlank()) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email.trim().toLowerCase()).matches();
    }
}
