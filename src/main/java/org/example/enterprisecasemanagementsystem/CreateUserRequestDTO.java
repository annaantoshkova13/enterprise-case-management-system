package org.example.enterprisecasemanagementsystem;

import jakarta.validation.constraints.*;

public class CreateUserRequestDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotBlank(message = "Role is required")
    @Pattern(regexp = "ADMIN|STUDENT|TEACHER",
            message = "Role must be ADMIN, STUDENT or TEACHER")
    private String role;  // String, не Role!

    private String firstName;
    private String lastName;

    public CreateUserRequestDTO() {
    }

    public CreateUserRequestDTO(String email, String password, String role) {
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public CreateUserRequestDTO(String email, String password, String role, String firstName, String lastName) {
        this.email = email;
        this.password = password;
        this.role = role;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Role getRoleAsEnum() {
        if (role == null || role.isBlank()) {
            throw new IllegalArgumentException("Role is null or empty");
        }
        return Role.valueOf(role.toUpperCase());
    }
}
