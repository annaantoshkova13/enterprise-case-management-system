package org.example.enterprisecasemanagementsystem.exception;

import org.example.enterprisecasemanagementsystem.infrastructure.exception.BusinessException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class BusinessExceptionTest {

    @Test
    void shouldCreateException_WithMessage() {
        String message = "Business error occurred";
        BusinessException exception = new BusinessException(message);

        assertEquals(message, exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldCreateException_WithMessageAndCause() {
        String message = "Business error";
        Throwable cause = new IllegalArgumentException("Invalid input");
        BusinessException exception = new BusinessException(message, cause);

        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
