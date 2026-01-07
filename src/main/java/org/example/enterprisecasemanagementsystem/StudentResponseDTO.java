package org.example.enterprisecasemanagementsystem;

public class StudentResponseDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String groupName;
    private UserResponseDTO user;

    public StudentResponseDTO() {
    }

    public StudentResponseDTO(Long id, String firstName, String lastName, String groupName, UserResponseDTO user) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.groupName = groupName;
        this.user = user;
    }

    public static StudentResponseDTO fromEntity(org.example.enterprisecasemanagementsystem.student.Student student) {
        return new StudentResponseDTO(
                student.getId(),
                student.getFirstName(),
                student.getLastName(),
                student.getGroupName(),
                UserResponseDTO.fromEntity(student.getUser())
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

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public UserResponseDTO getUser() {
        return user;
    }

    public void setUser(UserResponseDTO user) {
        this.user = user;
    }
}
