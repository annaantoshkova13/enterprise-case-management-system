package org.example.enterprisecasemanagementsystem.user;

import org.example.enterprisecasemanagementsystem.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListUsersUseCaseTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ListUsersUseCase listUsersUseCase;

    @Test
    void shouldReturnAllUsers() {
        User user1 = new User("user1@example.com", "password123", Role.STUDENT);
        User user2 = new User("user2@example.com", "password456", Role.TEACHER);
        List<User> users = Arrays.asList(user1, user2);

        when(userRepository.findAll()).thenReturn(users);

        List<User> result = listUsersUseCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository).findAll();
    }

    @Test
    void shouldReturnEmptyList_WhenNoUsers() {
        when(userRepository.findAll()).thenReturn(List.of());

        List<User> result = listUsersUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository).findAll();
    }

    @Test
    void shouldReturnUsersWithDifferentRoles() {
        User student = new User("student@example.com", "student123", Role.STUDENT);
        User teacher = new User("teacher@example.com", "teacher123", Role.TEACHER);
        User admin = new User("admin@example.com", "admin12345", Role.ADMIN);

        when(userRepository.findAll()).thenReturn(Arrays.asList(student, teacher, admin));

        List<User> result = listUsersUseCase.execute();

        assertEquals(3, result.size());
        assertEquals(Role.STUDENT, result.get(0).getRole());
        assertEquals(Role.TEACHER, result.get(1).getRole());
        assertEquals(Role.ADMIN, result.get(2).getRole());
    }
}