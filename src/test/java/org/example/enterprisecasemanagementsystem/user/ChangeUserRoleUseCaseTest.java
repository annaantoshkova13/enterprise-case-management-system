package org.example.enterprisecasemanagementsystem.user;

import org.example.enterprisecasemanagementsystem.Role;
import org.example.enterprisecasemanagementsystem.exception.ResourceNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ChangeUserRoleUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ChangeUserRoleUseCase changeUserRoleUseCase;

    @Test
    void shouldChangeUserRole_WhenUserExists() {
        User user = new User("test@example.com", "password", Role.STUDENT);
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenReturn(user);

        User result = changeUserRoleUseCase.execute(1L, Role.TEACHER);

        assertNotNull(result);
        assertEquals(Role.TEACHER, result.getRole());
        verify(userRepository).findById(1L);
        verify(userRepository).save(user);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> changeUserRoleUseCase.execute(999L, Role.ADMIN)
        );

        assertEquals("User not found with id: '999'", ex.getMessage());
        verify(userRepository).findById(999L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldNotChangeRole_WhenSameRoleProvided() {
        User user = new User("test@example.com", "password", Role.STUDENT);
        user.setId(1L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        User result = changeUserRoleUseCase.execute(1L, Role.STUDENT);

        assertNotNull(result);
        assertEquals(Role.STUDENT, result.getRole());
        verify(userRepository).findById(1L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldHandleNullRole() {

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> changeUserRoleUseCase.execute(1L, null)
        );

        assertEquals("New role cannot be null", ex.getMessage());

        verify(userRepository, never()).findById(any());
        verify(userRepository, never()).save(any());
    }
}
