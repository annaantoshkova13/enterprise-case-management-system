package org.example.enterprisecasemanagementsystem;

import java.time.LocalDateTime;
import java.util.Set;

public class CourseResponseDTO {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private Integer maxStudents;
    private TeacherResponseDTO teacher;
    private Set<Long> enrolledStudentIds;

    public CourseResponseDTO() {
    }

    public CourseResponseDTO(Long id, String title, String description, LocalDateTime createdAt,
                             Integer maxStudents, TeacherResponseDTO teacher, Set<Long> enrolledStudentIds) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.maxStudents = maxStudents;
        this.teacher = teacher;
        this.enrolledStudentIds = enrolledStudentIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }

    public TeacherResponseDTO getTeacher() {
        return teacher;
    }

    public void setTeacher(TeacherResponseDTO teacher) {
        this.teacher = teacher;
    }

    public Set<Long> getEnrolledStudentIds() {
        return enrolledStudentIds;
    }

    public void setEnrolledStudentIds(Set<Long> enrolledStudentIds) {
        this.enrolledStudentIds = enrolledStudentIds;
    }
}
