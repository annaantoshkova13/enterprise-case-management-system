package org.example.enterprisecasemanagementsystem.infrastructure.web.dto.response;

import org.example.enterprisecasemanagementsystem.domain.Role;
import org.example.enterprisecasemanagementsystem.domain.User;

import java.time.LocalDateTime;

public class UserResponseDTO {

    private Long id;
    private String email;
    private Role role;
    private LocalDateTime createdAt;
    private String fullName;

    public UserResponseDTO() {
    }

    public UserResponseDTO(Long id, String email, Role role, LocalDateTime createdAt, String fullName) {
        this.id = id;
        this.email = email;
        this.role = role;
        this.createdAt = createdAt;
        this.fullName = fullName;
    }

    public static UserResponseDTO fromEntity(User user) {
        if (user == null) {
            return null;
        }

        return new UserResponseDTO(
                user.getId(),
                user.getEmailString(),
                user.getRole(),
                user.getCreatedAt(),
                null
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
