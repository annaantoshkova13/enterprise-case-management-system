package org.example.enterprisecasemanagementsystem;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateStudentRequestDTO {
    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Group name is required")
    @Size(min = 1, max = 20, message = "Group name must be between 1 and 20 characters")
    private String groupName;

    @NotNull(message = "User ID is required")
    private Long userId;

    public CreateStudentRequestDTO() {
    }

    public CreateStudentRequestDTO(String firstName, String lastName, String groupName, Long userId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupName = groupName;
        this.userId = userId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
