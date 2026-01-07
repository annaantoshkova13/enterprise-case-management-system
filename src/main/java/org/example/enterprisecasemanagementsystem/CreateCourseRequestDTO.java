package org.example.enterprisecasemanagementsystem;

import jakarta.validation.constraints.*;

public class CreateCourseRequestDTO {

    @NotBlank(message = "Title is required")
    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @NotBlank(message = "Description is required")
    @Size(min = 10, max = 1000, message = "Description must be between 10 and 1000 characters")
    private String description;

    @NotNull(message = "Teacher ID is required")
    @Positive(message = "Teacher ID must be positive")
    private Long teacherId;

    @Min(value = 1, message = "Maximum students must be at least 1")
    @Max(value = 100, message = "Maximum students cannot exceed 100")
    private Integer maxStudents = 30;

    public CreateCourseRequestDTO() {
    }

    public CreateCourseRequestDTO(String title, String description, Long teacherId, Integer maxStudents) {
        this.title = title;
        this.description = description;
        this.teacherId = teacherId;
        this.maxStudents = maxStudents;
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

    public Long getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getMaxStudents() {
        return maxStudents;
    }

    public void setMaxStudents(Integer maxStudents) {
        this.maxStudents = maxStudents;
    }
}
