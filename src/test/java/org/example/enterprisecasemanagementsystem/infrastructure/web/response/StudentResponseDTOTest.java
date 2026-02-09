package org.example.enterprisecasemanagementsystem.infrastructure.web.response;

import org.example.enterprisecasemanagementsystem.domain.Role;
import org.example.enterprisecasemanagementsystem.domain.Student;
import org.example.enterprisecasemanagementsystem.domain.User;
import org.example.enterprisecasemanagementsystem.infrastructure.web.dto.response.StudentResponseDTO;
import org.example.enterprisecasemanagementsystem.infrastructure.web.dto.response.UserResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class StudentResponseDTOTest {

    @Test
    void fromEntity_ShouldConvertStudentToDTO() {
        User user = new User("student@example.com", "password", Role.STUDENT);
        user.setId(1L);

        Student student = new Student("Alice", "Smith", "CS-101", user);
        student.setId(2L);

        StudentResponseDTO dto = StudentResponseDTO.fromEntity(student);

        assertEquals(2L, dto.getId());
        assertEquals("Alice", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
        assertEquals("CS-101", dto.getGroupName());
        assertNotNull(dto.getUser());
        assertEquals(1L, dto.getUser().getId());
        assertEquals("student@example.com", dto.getUser().getEmail());
        assertEquals(Role.STUDENT, dto.getUser().getRole());
    }

    @Test
    void fromEntity_ShouldHandleNullUser() {
        Student student = new Student("Bob", "Johnson", "CS-102", null);
        student.setId(3L);

        StudentResponseDTO dto = StudentResponseDTO.fromEntity(student);

        assertEquals(3L, dto.getId());
        assertEquals("Bob", dto.getFirstName());
        assertEquals("Johnson", dto.getLastName());
        assertEquals("CS-102", dto.getGroupName());
        assertNull(dto.getUser());
    }

    @Test
    void shouldSetAndGetProperties() {
        StudentResponseDTO dto = new StudentResponseDTO();
        UserResponseDTO userDTO = new UserResponseDTO();

        dto.setId(1L);
        dto.setFirstName("Jane");
        dto.setLastName("Doe");
        dto.setGroupName("AI-201");
        dto.setUser(userDTO);

        assertEquals(1L, dto.getId());
        assertEquals("Jane", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("AI-201", dto.getGroupName());
        assertEquals(userDTO, dto.getUser());
    }

    @Test
    void shouldHaveAllArgsConstructor() {
        UserResponseDTO userDTO = new UserResponseDTO(1L, "test@example.com", Role.STUDENT, null, null);
        StudentResponseDTO dto = new StudentResponseDTO(
                1L,
                "John",
                "Doe",
                "CS-101",
                userDTO
        );

        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("CS-101", dto.getGroupName());
        assertEquals(userDTO, dto.getUser());
    }

    @Test
    void shouldHaveNoArgsConstructor() {
        StudentResponseDTO dto = new StudentResponseDTO();
        assertNotNull(dto);
        assertNull(dto.getId());
        assertNull(dto.getFirstName());
        assertNull(dto.getLastName());
        assertNull(dto.getGroupName());
        assertNull(dto.getUser());
    }
}
