package org.example.enterprisecasemanagementsystem.domain;

import jakarta.persistence.*;

@Entity
@Table(name = "students")
public class Student extends Profile {
    @Column(name = "group_name")
    private String groupName;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    public Student() {
    }

    public Student(String firstName, String lastName, String groupName, User user) {
        super(firstName, lastName);
        this.groupName = groupName;
        this.user = user;
    }

    public Student(User user, String groupName) {
        this.user = user;
        this.groupName = groupName;
    }

    public Student(User user) {
        this.user = user;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void changeGroup(String newGroup) {
        if (newGroup == null || newGroup.isBlank()) {
            throw new IllegalArgumentException("Group cannot be empty");
        }
        this.groupName = newGroup;
    }
}
