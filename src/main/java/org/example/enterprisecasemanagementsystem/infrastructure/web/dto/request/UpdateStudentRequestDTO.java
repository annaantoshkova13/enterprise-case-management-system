package org.example.enterprisecasemanagementsystem.infrastructure.web.dto.request;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UpdateStudentRequestDTO {

    @Size(min = 2, max = 20, message = "Group name must be between 2 and 20 characters")
    @Pattern(regexp = "^[A-Z0-9\\-_]*$", message = "Group name can only contain uppercase letters, numbers, hyphens and underscores")
    private String groupName;

    public UpdateStudentRequestDTO() {
    }

    public UpdateStudentRequestDTO(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
