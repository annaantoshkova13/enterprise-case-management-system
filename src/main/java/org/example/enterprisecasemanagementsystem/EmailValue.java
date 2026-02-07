package org.example.enterprisecasemanagementsystem;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;


import java.io.Serializable;
import java.util.regex.Pattern;


@Embeddable
@JsonDeserialize(using = EmailValueDeserializer.class)
public class EmailValue implements Serializable {

    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$"
    );

    @Column(name = "email", nullable = false, unique = true)
    private String value;

    public EmailValue() {
    }

    @JsonCreator
    public EmailValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or empty");
        }

        String trimmedEmail = value.trim().toLowerCase();

        if (!isValid(trimmedEmail)) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }

        this.value = trimmedEmail;
    }

    @JsonValue
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

        String trimmedEmail = email.trim().toLowerCase();

        if (!trimmedEmail.contains("@")) {
            return false;
        }

        String[] parts = trimmedEmail.split("@");
        if (parts.length != 2) {
            return false;
        }

        String localPart = parts[0];
        String domainPart = parts[1];

        if (localPart.isEmpty() || localPart.length() > 64) {
            return false;
        }

        if (domainPart.isEmpty() || domainPart.length() > 255) {
            return false;
        }

        if (!domainPart.contains(".")) {
        }

        if (domainPart.contains("..") || localPart.contains("..")) {
            return false;
        }

        return EMAIL_PATTERN.matcher(trimmedEmail).matches();
    }
}
