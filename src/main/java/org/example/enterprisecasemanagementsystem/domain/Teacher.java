package org.example.enterprisecasemanagementsystem.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "teachers")
public class Teacher extends Profile {

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String department;

    public Teacher() {
    }

    public Teacher(String firstName, String lastName, String department, User user) {
        super(firstName, lastName);
        this.department = department;
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void changeDepartment(String newDepartment) {
        if (newDepartment == null || newDepartment.isBlank()) {
            throw new IllegalArgumentException("Department cannot be empty");
        }
        this.department = newDepartment;
    }
}
