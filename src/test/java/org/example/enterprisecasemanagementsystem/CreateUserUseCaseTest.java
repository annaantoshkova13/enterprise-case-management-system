package org.example.enterprisecasemanagementsystem;

import org.example.enterprisecasemanagementsystem.exception.BusinessException;
import org.example.enterprisecasemanagementsystem.user.CreateUserUseCase;
import org.example.enterprisecasemanagementsystem.user.User;
import org.example.enterprisecasemanagementsystem.user.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
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

    @Captor
    private ArgumentCaptor<User> userCaptor;

    @BeforeEach
    void setUp() {
    }

    @Test
    void shouldCreateUser_WhenEmailIsUnique() {
        String email = "test@example.com";
        String password = "password123";
        Role role = Role.STUDENT;

        when(userRepository.existsByEmail(email)).thenReturn(false);
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        User createdUser = createUserUseCase.execute(email, password, role);

        assertNotNull(createdUser);
        assertEquals(email, createdUser.getEmail().getValue());
        assertEquals(role, createdUser.getRole());

        verify(userRepository).existsByEmail(email);
        verify(passwordEncoder).encode(password);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldThrowException_WhenEmailAlreadyExists() {
        String email = "existing@example.com";
        String password = "password123";
        Role role = Role.STUDENT;

        when(userRepository.existsByEmail(email)).thenReturn(true);

        BusinessException ex = assertThrows(
                BusinessException.class,
                () -> createUserUseCase.execute(email, password, role)
        );

        assertEquals("User with email " + email + " already exists", ex.getMessage());

        verify(userRepository).existsByEmail(email);
        verify(passwordEncoder, never()).encode(any());
        verify(userRepository, never()).save(any());
    }
}