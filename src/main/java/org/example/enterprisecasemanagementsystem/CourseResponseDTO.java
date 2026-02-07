package org.example.enterprisecasemanagementsystem;

import org.example.enterprisecasemanagementsystem.course.Course;
import org.example.enterprisecasemanagementsystem.student.Student;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

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

    public static CourseResponseDTO fromEntity(Course course) {
        if (course == null) {
            return null;
        }

        TeacherResponseDTO teacherDTO = null;
        if (course.getTeacher() != null) {
            teacherDTO = TeacherResponseDTO.fromEntity(course.getTeacher());
        }

        Set<Long> studentIds = null;
        if (course.getEnrolledStudents() != null) {
            studentIds = course.getEnrolledStudents().stream()
                    .map(Student::getId)
                    .collect(Collectors.toSet());
        }

        return new CourseResponseDTO(
                course.getId(),
                course.getTitle(),
                course.getDescription(),
                course.getCreatedAt(),
                course.getMaxStudents(),
                teacherDTO,
                studentIds
        );
    }

    public Integer getCurrentEnrollment() {
        return this.enrolledStudentIds != null ? this.enrolledStudentIds.size() : 0;
    }

    public Integer getAvailableSlots() {
        if (maxStudents == null || enrolledStudentIds == null) {
            return 0;
        }
        return Math.max(0, maxStudents - enrolledStudentIds.size());
    }

    public Boolean hasAvailableSlots() {
        return getAvailableSlots() > 0;
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