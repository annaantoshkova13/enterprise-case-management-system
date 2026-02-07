package org.example.enterprisecasemanagementsystem;

import jakarta.validation.constraints.Size;

public class UpdateCourseRequestDTO {

    @Size(min = 3, max = 100, message = "Title must be between 3 and 100 characters")
    private String title;

    @Size(min = 3, max = 500, message = "Description must be between 3 and 500 characters")
    private String description;

    public UpdateCourseRequestDTO() {
    }

    public UpdateCourseRequestDTO(String title, String description) {
        this.title = title;
        this.description = description;
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
}
