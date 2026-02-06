package org.example.enterprisecasemanagementsystem.user;

import org.example.enterprisecasemanagementsystem.Role;
import org.example.enterprisecasemanagementsystem.exception.BusinessException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateUserUseCaseTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private CreateUserUseCase createUserUseCase;

    @Test
    void shouldCreateUser_WhenEmailIsUnique() {
        // Given
        String email = "test@example.com";
        String password = "password123";
        Role role = Role.STUDENT;

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        User result = createUserUseCase.execute(email, password, role);

        assertNotNull(result);
        assertEquals(email, result.getEmailString());
        assertEquals("encodedPassword", result.getPasswordHash());
        assertEquals(role, result.getRole());
        assertEquals(1L, result.getId());

        verify(userRepository).existsByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowBusinessException_WhenEmailAlreadyExists() {
        String email = "existing@example.com";
        String password = "password123";
        Role role = Role.STUDENT;

        when(userRepository.existsByEmail(email)).thenReturn(true);

        BusinessException ex = assertThrows(BusinessException.class,
                () -> createUserUseCase.execute(email, password, role)
        );

        assertEquals("User with email " + email + " already exists", ex.getMessage());
        verify(userRepository).existsByEmail(email);
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }
}