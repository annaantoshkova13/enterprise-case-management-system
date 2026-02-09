package org.example.enterprisecasemanagementsystem.infrastructure;

import org.example.enterprisecasemanagementsystem.infrastructure.web.ApiResponse;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ApiResponseTest {

    @Test
    void shouldCreateSuccessResponse_WithDataOnly() {
        String data = "Test Data";
        ApiResponse<String> response = ApiResponse.success(data);

        assertTrue(response.isSuccess());
        assertEquals("Operation successful", response.getMessage());
        assertEquals(data, response.getData());
        assertNull(response.getError());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void shouldCreateSuccessResponse_WithDataAndMessage() {
        String data = "Test Data";
        String message = "Custom message";
        ApiResponse<String> response = ApiResponse.success(data, message);

        assertTrue(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertEquals(data, response.getData());
        assertNull(response.getError());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void shouldCreateErrorResponse() {
        String message = "Error occurred";
        ApiResponse.ErrorDetails errorDetails = new ApiResponse.ErrorDetails("ERR001", "Details");

        ApiResponse<String> response = ApiResponse.error(message, errorDetails);

        assertFalse(response.isSuccess());
        assertEquals(message, response.getMessage());
        assertNull(response.getData());
        assertEquals(errorDetails, response.getError());
        assertNotNull(response.getTimestamp());
    }

    @Test
    void errorDetailsShouldHaveCodeAndDetails() {
        ApiResponse.ErrorDetails errorDetails = new ApiResponse.ErrorDetails("VALIDATION_ERROR", "Email is invalid");

        assertEquals("VALIDATION_ERROR", errorDetails.getCode());
        assertEquals("Email is invalid", errorDetails.getDetails());
    }

    @Test
    void shouldSetAndGetProperties() {
        ApiResponse<String> response = new ApiResponse<>();
        LocalDateTime now = LocalDateTime.now();
        ApiResponse.ErrorDetails error = new ApiResponse.ErrorDetails();

        response.setTimestamp(now);
        response.setSuccess(true);
        response.setMessage("Test");
        response.setData("Data");
        response.setError(error);

        assertEquals(now, response.getTimestamp());
        assertTrue(response.isSuccess());
        assertEquals("Test", response.getMessage());
        assertEquals("Data", response.getData());
        assertEquals(error, response.getError());
    }
}
