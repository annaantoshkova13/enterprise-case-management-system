package org.example.enterprisecasemanagementsystem.application.teacher;

import org.example.enterprisecasemanagementsystem.application.teacher.CreateTeacherUseCase;
import org.example.enterprisecasemanagementsystem.domain.Role;
import org.example.enterprisecasemanagementsystem.domain.Teacher;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.BusinessException;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.domain.User;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.TeacherRepository;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateTeacherUseCaseTest {

    @Mock
    private TeacherRepository teacherRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateTeacherUseCase createTeacherUseCase;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User("teacher@example.com", "password", Role.TEACHER);
        mockUser.setId(1L);
    }

    @Test
    void shouldCreateTeacher_WithValidParametersAndUserId() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(teacherRepository.save(any(Teacher.class))).thenAnswer(invocation -> {
            Teacher teacher = invocation.getArgument(0);
            teacher.setId(100L);
            return teacher;
        });

        Teacher result = createTeacherUseCase.execute(
                "John",
                "Doe",
                "Computer Science",
                1L
        );

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("Computer Science", result.getDepartment());
        assertEquals(mockUser, result.getUser());
        assertEquals(100L, result.getId());

        verify(userRepository).findById(1L);
        verify(teacherRepository).save(any(Teacher.class));
    }

    @Test
    void shouldCreateTeacher_WithValidParametersAndUserObject() {
        when(teacherRepository.save(any(Teacher.class))).thenAnswer(invocation -> {
            Teacher teacher = invocation.getArgument(0);
            teacher.setId(100L);
            return teacher;
        });

        Teacher result = createTeacherUseCase.execute(
                "John",
                "Doe",
                "Computer Science",
                mockUser
        );

        assertNotNull(result);
        assertEquals("John", result.getFirstName());
        assertEquals("Doe", result.getLastName());
        assertEquals("Computer Science", result.getDepartment());
        assertEquals(mockUser, result.getUser());

        verify(teacherRepository).save(any(Teacher.class));
        verify(userRepository, never()).findById(any());
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> createTeacherUseCase.execute(
                        "John",
                        "Doe",
                        "Computer Science",
                        999L
                )
        );

        assertEquals("User not found with id: '999'", ex.getMessage());
        verify(userRepository).findById(999L);
        verify(teacherRepository, never()).save(any());
    }

    @Test
    void shouldThrowBusinessException_WhenUserIsNull() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> createTeacherUseCase.execute(
                        "John",
                        "Doe",
                        "Computer Science",
                        (User) null
                )
        );

        assertEquals("User cannot be null", ex.getMessage());
        verify(userRepository, never()).findById(any());
        verify(teacherRepository, never()).save(any());
    }
}