package org.example.enterprisecasemanagementsystem.user;

import jakarta.persistence.*;
import org.example.enterprisecasemanagementsystem.EmailValue;
import org.example.enterprisecasemanagementsystem.Role;

import java.time.LocalDateTime;

@Entity
@Table (name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private EmailValue email;

    @Column(nullable = false)
    private String passwordHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public User(EmailValue email, String passwordHash, Role role) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.role = role;
    }

    public User(String email, String passwordHash, Role role) {
        this(new EmailValue(email), passwordHash, role);
    }

    public User() {
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public EmailValue getEmail() { return email; }
    public void setEmail(EmailValue email) { this.email = email; }

    public void setEmail(String email) { this.email = new EmailValue(email); }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public String getEmailString() {
        return email != null ? email.toString() : null;
    }
}
