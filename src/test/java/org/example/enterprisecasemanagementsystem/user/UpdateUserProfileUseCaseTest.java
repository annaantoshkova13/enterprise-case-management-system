package org.example.enterprisecasemanagementsystem.user;

import org.example.enterprisecasemanagementsystem.exception.BusinessException;
import org.example.enterprisecasemanagementsystem.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UpdateUserProfileUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UpdateUserProfileUseCase updateUserProfileUseCase;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User("old@example.com", "oldPassword", Role.STUDENT);
        mockUser.setId(1L);
    }

    @Test
    void shouldUpdateEmail_WhenNewEmailIsUnique() {
        String newEmail = "new@example.com";

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.existsByEmail(newEmail)).thenReturn(false);
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User result = updateUserProfileUseCase.execute(1L, newEmail, null);

        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail(newEmail);
        verify(userRepository).save(mockUser);
        verify(passwordEncoder, never()).encode(any());
    }

    @Test
    void shouldUpdatePassword_WhenNewPasswordProvided() {
        String newPassword = "newPassword123";

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(passwordEncoder.encode(newPassword)).thenReturn("encodedNewPassword");
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User result = updateUserProfileUseCase.execute(1L, "old@example.com", newPassword);

        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(passwordEncoder).encode(newPassword);
        verify(userRepository).save(mockUser);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> updateUserProfileUseCase.execute(999L, "new@example.com", "password")
        );

        assertEquals("User not found with id: '999'", ex.getMessage());
        verify(userRepository).findById(999L);
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldThrowBusinessException_WhenNewEmailAlreadyExists() {
        String newEmail = "existing@example.com";

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.existsByEmail(newEmail)).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> updateUserProfileUseCase.execute(1L, newEmail, null)
        );

        assertEquals("Email " + newEmail + " is already taken", ex.getMessage());
        verify(userRepository).findById(1L);
        verify(userRepository).existsByEmail(newEmail);
        verify(userRepository, never()).save(any());
    }

    @Test
    void shouldNotCheckEmailUniqueness_WhenEmailUnchanged() {
        String sameEmail = "old@example.com";

        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User result = updateUserProfileUseCase.execute(1L, sameEmail, null);

        assertNotNull(result);
        verify(userRepository).findById(1L);
        verify(userRepository, never()).existsByEmail(sameEmail);
        verify(userRepository).save(mockUser);
    }

    @Test
    void shouldNotUpdatePassword_WhenNewPasswordIsNullOrBlank() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(userRepository.save(any(User.class))).thenReturn(mockUser);

        User result1 = updateUserProfileUseCase.execute(1L, "old@example.com", null);
        assertNotNull(result1);

        User result2 = updateUserProfileUseCase.execute(1L, "old@example.com", "");
        assertNotNull(result2);

        User result3 = updateUserProfileUseCase.execute(1L, "old@example.com", "   ");
        assertNotNull(result3);

        verify(passwordEncoder, never()).encode(any());
    }
}
