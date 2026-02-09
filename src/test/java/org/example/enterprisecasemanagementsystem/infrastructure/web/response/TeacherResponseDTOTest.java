package org.example.enterprisecasemanagementsystem.infrastructure.web.response;

import org.example.enterprisecasemanagementsystem.domain.Role;
import org.example.enterprisecasemanagementsystem.domain.Teacher;
import org.example.enterprisecasemanagementsystem.domain.User;
import org.example.enterprisecasemanagementsystem.infrastructure.web.dto.response.TeacherResponseDTO;
import org.example.enterprisecasemanagementsystem.infrastructure.web.dto.response.UserResponseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TeacherResponseDTOTest {

    @Test
    void fromEntity_ShouldConvertTeacherToDTO() {
        User user = new User("teacher@example.com", "password", Role.TEACHER);
        user.setId(1L);

        Teacher teacher = new Teacher("John", "Doe", "Computer Science", user);
        teacher.setId(2L);

        TeacherResponseDTO dto = TeacherResponseDTO.fromEntity(teacher);

        assertEquals(2L, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("Computer Science", dto.getDepartment());
        assertNotNull(dto.getUser());
        assertEquals(1L, dto.getUser().getId());
        assertEquals("teacher@example.com", dto.getUser().getEmail());
        assertEquals(Role.TEACHER, dto.getUser().getRole());
    }

    @Test
    void shouldSetAndGetProperties() {
        TeacherResponseDTO dto = new TeacherResponseDTO();
        UserResponseDTO userDTO = new UserResponseDTO();

        dto.setId(1L);
        dto.setFirstName("Jane");
        dto.setLastName("Smith");
        dto.setDepartment("Mathematics");
        dto.setUser(userDTO);

        assertEquals(1L, dto.getId());
        assertEquals("Jane", dto.getFirstName());
        assertEquals("Smith", dto.getLastName());
        assertEquals("Mathematics", dto.getDepartment());
        assertEquals(userDTO, dto.getUser());
    }

    @Test
    void shouldHaveConstructor() {
        UserResponseDTO userDTO = new UserResponseDTO(1L, "test@example.com", Role.TEACHER, null, null);
        TeacherResponseDTO dto = new TeacherResponseDTO(
                1L,
                "John",
                "Doe",
                "Computer Science",
                userDTO
        );

        assertEquals(1L, dto.getId());
        assertEquals("John", dto.getFirstName());
        assertEquals("Doe", dto.getLastName());
        assertEquals("Computer Science", dto.getDepartment());
        assertEquals(userDTO, dto.getUser());
    }
}
