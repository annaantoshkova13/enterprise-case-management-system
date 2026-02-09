package org.example.enterprisecasemanagementsystem.domain;

public enum Role {
    ADMIN,
    STUDENT,
    TEACHER;
    public boolean hasHigherPrivilegesThan(Role other) {
        if (this == ADMIN && (other == TEACHER || other == STUDENT)) {
            return true;
        }
        if (this == TEACHER && other == STUDENT) {
            return true;
        }
        return false;
    }
}