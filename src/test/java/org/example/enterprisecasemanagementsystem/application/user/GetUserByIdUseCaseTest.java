package org.example.enterprisecasemanagementsystem.application.user;

import org.example.enterprisecasemanagementsystem.application.user.GetUserByIdUseCase;
import org.example.enterprisecasemanagementsystem.domain.Role;
import org.example.enterprisecasemanagementsystem.domain.User;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetUserByIdUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GetUserByIdUseCase getUserByIdUseCase;

    @Test
    void shouldReturnUser_WhenUserExists() {
        User user = new User("test@example.com", "password", Role.STUDENT);
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = getUserByIdUseCase.execute(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("test@example.com", result.getEmailString());
        verify(userRepository).findById(1L);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> getUserByIdUseCase.execute(999L)
        );

        assertEquals("User not found with id: '999'", ex.getMessage());
        verify(userRepository).findById(999L);
    }

    @Test
    void shouldReturnUserWithRole() {
        User user = new User("admin@example.com", "password", Role.ADMIN);
        user.setId(2L);

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        User result = getUserByIdUseCase.execute(2L);

        assertNotNull(result);
        assertEquals(Role.ADMIN, result.getRole());
    }
}
