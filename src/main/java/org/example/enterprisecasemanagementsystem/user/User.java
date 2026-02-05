package org.example.enterprisecasemanagementsystem.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.enterprisecasemanagementsystem.EmailValue;
import org.example.enterprisecasemanagementsystem.Role;
import org.example.enterprisecasemanagementsystem.student.Student;
import org.example.enterprisecasemanagementsystem.teacher.Teacher;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table (name = "users")
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

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Teacher> teachers = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Student> students = new HashSet<>();

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

    public void setEmail(EmailValue email) { this.email = email; }

    public void setEmail(String email) { this.email = new EmailValue(email); }

    public String getEmailString() {
        return email != null ? email.toString() : null;
    }

}
