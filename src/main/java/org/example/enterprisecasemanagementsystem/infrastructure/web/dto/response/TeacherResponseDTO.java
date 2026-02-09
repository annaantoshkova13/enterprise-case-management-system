package org.example.enterprisecasemanagementsystem.infrastructure.web.dto.response;

import org.example.enterprisecasemanagementsystem.domain.Teacher;

public class TeacherResponseDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String department;
    private UserResponseDTO user;

    public TeacherResponseDTO() {
    }

    public TeacherResponseDTO(Long id, String firstName, String lastName, String department, UserResponseDTO user) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.department = department;
        this.user = user;
    }

    public static TeacherResponseDTO fromEntity(Teacher teacher) {
        if (teacher == null) {
            return null;
        }

        UserResponseDTO userDTO = null;
        if (teacher.getUser() != null) {
            userDTO = UserResponseDTO.fromEntity(teacher.getUser());
        }

        return new TeacherResponseDTO(
                teacher.getId(),
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getDepartment(),
                userDTO
        );
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }
}