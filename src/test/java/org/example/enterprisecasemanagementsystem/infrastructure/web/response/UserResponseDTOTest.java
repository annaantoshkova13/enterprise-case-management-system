package org.example.enterprisecasemanagementsystem.infrastructure.web.response;

import org.example.enterprisecasemanagementsystem.domain.Role;
import org.example.enterprisecasemanagementsystem.domain.User;
import org.example.enterprisecasemanagementsystem.infrastructure.web.dto.response.UserResponseDTO;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserResponseDTOTest {

    @Test
    void fromEntity_ShouldConvertUserToDTO() {
        User user = new User("test@example.com", "password", Role.STUDENT);
        user.setId(1L);
        LocalDateTime now = LocalDateTime.now();
        user.setCreatedAt(now);

        UserResponseDTO dto = UserResponseDTO.fromEntity(user);

        assertEquals(1L, dto.getId());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals(Role.STUDENT, dto.getRole());
        assertEquals(now, dto.getCreatedAt());
    }

    @Test
    void fromEntity_ShouldHandleNullCreatedAt() {
        User user = new User("test@example.com", "password", Role.TEACHER);
        user.setId(2L);
        user.setCreatedAt(null);

        UserResponseDTO dto = UserResponseDTO.fromEntity(user);

        assertEquals(2L, dto.getId());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals(Role.TEACHER, dto.getRole());
        assertNull(dto.getCreatedAt());
    }

    @Test
    void shouldSetAndGetAllProperties() {
        UserResponseDTO dto = new UserResponseDTO();
        LocalDateTime now = LocalDateTime.now();

        dto.setId(1L);
        dto.setEmail("user@example.com");
        dto.setRole(Role.ADMIN);
        dto.setCreatedAt(now);
        dto.setFullName("John Doe");

        assertEquals(1L, dto.getId());
        assertEquals("user@example.com", dto.getEmail());
        assertEquals(Role.ADMIN, dto.getRole());
        assertEquals(now, dto.getCreatedAt());
        assertEquals("John Doe", dto.getFullName());
    }

    @Test
    void shouldHaveAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();

        UserResponseDTO dto = new UserResponseDTO(
                1L,
                "test@example.com",
                Role.TEACHER,
                now,
                "Jane Smith"
        );

        assertEquals(1L, dto.getId());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals(Role.TEACHER, dto.getRole());
        assertEquals(now, dto.getCreatedAt());
        assertEquals("Jane Smith", dto.getFullName());
    }

    @Test
    void shouldHaveConstructorWithoutNames() {
        LocalDateTime now = LocalDateTime.now();

        UserResponseDTO dto = new UserResponseDTO(
                1L,
                "test@example.com",
                Role.STUDENT,
                now,
                null
        );

        assertEquals(1L, dto.getId());
        assertEquals("test@example.com", dto.getEmail());
        assertEquals(Role.STUDENT, dto.getRole());
        assertEquals(now, dto.getCreatedAt());
        assertNull(dto.getFullName());
    }
}
