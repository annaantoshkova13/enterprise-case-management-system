package org.example.enterprisecasemanagementsystem.application.user;

import org.example.enterprisecasemanagementsystem.application.user.CreateUserUseCase;
import org.example.enterprisecasemanagementsystem.domain.Role;
import org.example.enterprisecasemanagementsystem.domain.EmailValue;
import org.example.enterprisecasemanagementsystem.domain.User;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.BusinessException;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.UserRepository;
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
        String email = "test@example.com";
        String password = "password123";
        Role role = Role.STUDENT;

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("any-hash-will-work");

        User savedUser = new User();
        savedUser.setId(1L);
        savedUser.setEmail(new EmailValue(email));
        savedUser.setRole(role);

        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        User result = createUserUseCase.execute(email, password, role);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(email, result.getEmailString());
        assertEquals(role, result.getRole());

        verify(passwordEncoder).encode(password);
        verify(userRepository).existsByEmail(email);
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