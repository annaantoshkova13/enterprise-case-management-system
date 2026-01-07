package org.example.enterprisecasemanagementsystem;

import java.time.LocalDateTime;

public class ApiResponse<T> {

    private LocalDateTime timestamp;
    private boolean success;
    private String message;
    private T data;
    private ErrorDetails error;

    public ApiResponse() {
    }

    public ApiResponse(LocalDateTime timestamp, boolean success, String message, T data, ErrorDetails error) {
        this.timestamp = timestamp;
        this.success = success;
        this.message = message;
        this.data = data;
        this.error = error;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                LocalDateTime.now(),
                true,
                "Operation successful",
                data,
                null
        );
    }

    public static <T> ApiResponse<T> success(T data, String message) {
        return new ApiResponse<>(
                LocalDateTime.now(),
                true,
                message,
                data,
                null
        );
    }

    public static <T> ApiResponse<T> error(String message, ErrorDetails error) {
        return new ApiResponse<>(
                LocalDateTime.now(),
                false,
                message,
                null,
                error
        );
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public T getData() {
        return data;
    }

    public ErrorDetails getError() {
        return error;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public void setError(ErrorDetails error) {
        this.error = error;
    }

    public static class ErrorDetails {
        private String code;
        private String details;

        public ErrorDetails() {
        }

        public ErrorDetails(String code, String details) {
            this.code = code;
            this.details = details;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getDetails() {
            return details;
        }

        public void setDetails(String details) {
            this.details = details;
        }
    }
}
