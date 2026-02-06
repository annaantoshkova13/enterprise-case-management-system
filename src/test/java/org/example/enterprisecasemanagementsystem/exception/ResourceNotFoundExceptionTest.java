package org.example.enterprisecasemanagementsystem.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ResourceNotFoundExceptionTest {

    @Test
    void shouldCreateException_WithMessage() {
        String message = "Resource not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(message);

        assertEquals(message, exception.getMessage());
    }

    @Test
    void shouldCreateException_WithResourceDetails() {
        String resourceName = "User";
        String fieldName = "id";
        Object fieldValue = 123L;

        ResourceNotFoundException exception = new ResourceNotFoundException(
                resourceName, fieldName, fieldValue);

        String expectedMessage = String.format(
                "%s not found with %s: '%s'", resourceName, fieldName, fieldValue);

        assertEquals(expectedMessage, exception.getMessage());
    }
}
