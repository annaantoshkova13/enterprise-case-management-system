package org.example.enterprisecasemanagementsystem.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.enterprisecasemanagementsystem.EmailValue;
import org.example.enterprisecasemanagementsystem.Role;
import org.example.enterprisecasemanagementsystem.student.Student;
import org.example.enterprisecasemanagementsystem.teacher.Teacher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
public class User {
    @Setter
    @Getter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Embedded
    private EmailValue email;

    @Setter
    @Getter
    @Column(nullable = false)
    private String passwordHash;

    @Setter
    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Setter
    @Getter
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Teacher> teachers = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Student> students = new HashSet<>();

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User(EmailValue email, String rawPassword, Role role) {
        validatePassword(rawPassword);
        this.email = email;
        this.passwordHash = hashPassword(rawPassword);
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    public User(String email, String rawPassword, Role role) {
        this(new EmailValue(email), rawPassword, role);
    }

    public User() {
    }

    private String hashPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    private void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }
        if (password.length() < 8) {
            throw new IllegalArgumentException("Password must be at least 8 characters long");
        }
    }

    public boolean checkPassword(String rawPassword) {
        if (rawPassword == null) {
            return false;
        }
        return passwordEncoder.matches(rawPassword, this.passwordHash);
    }

    public void setEmail(EmailValue email) { this.email = email; }

    public void setEmail(String email) { this.email = new EmailValue(email); }

    public String getEmailString() {
        return email != null ? email.toString() : null;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + getEmailString() + '\'' +
                ", role=" + role +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return id != null && id.equals(user.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}