package org.example.enterprisecasemanagementsystem.student;

import org.example.enterprisecasemanagementsystem.Role;
import org.example.enterprisecasemanagementsystem.exception.BusinessException;
import org.example.enterprisecasemanagementsystem.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.user.User;
import org.example.enterprisecasemanagementsystem.user.UserRepository;
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
public class CreateStudentUseCaseTest {

    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CreateStudentUseCase createStudentUseCase;

    private User mockUser;

    @BeforeEach
    void setUp() {
        mockUser = new User("student@example.com", "password", Role.STUDENT);
        mockUser.setId(1L);
    }

    @Test
    void shouldCreateStudent_WithValidParameters() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student student = invocation.getArgument(0);
            student.setId(100L);
            return student;
        });

        Student result = createStudentUseCase.execute(
                "Alice",
                "Smith",
                "CS-101",
                1L
        );

        assertNotNull(result);
        assertEquals("Alice", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("CS-101", result.getGroupName());
        assertEquals(mockUser, result.getUser());

        verify(userRepository).findById(1L);
        verify(studentRepository).save(any(Student.class));
    }

    @Test
    void shouldCreateStudent_WithUserObject() {
        when(studentRepository.save(any(Student.class))).thenAnswer(invocation -> {
            Student student = invocation.getArgument(0);
            student.setId(100L);
            return student;
        });

        Student result = createStudentUseCase.execute(
                "Alice",
                "Smith",
                "CS-101",
                mockUser
        );

        assertNotNull(result);
        assertEquals("Alice", result.getFirstName());
        assertEquals("Smith", result.getLastName());
        assertEquals("CS-101", result.getGroupName());
        assertEquals(mockUser, result.getUser());

        verify(studentRepository).save(any(Student.class));
        verify(userRepository, never()).findById(any());
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenUserNotFound() {
        when(userRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> createStudentUseCase.execute(
                        "Alice",
                        "Smith",
                        "CS-101",
                        999L
                )
        );

        assertEquals("User not found with id: '999'", ex.getMessage());
        verify(userRepository).findById(999L);
        verify(studentRepository, never()).save(any());
    }

    @Test
    void shouldThrowBusinessException_WhenUserIsNull() {
        BusinessException ex = assertThrows(BusinessException.class,
                () -> createStudentUseCase.execute(
                        "Alice",
                        "Smith",
                        "CS-101",
                        (User) null
                )
        );

        assertEquals("User cannot be null", ex.getMessage());
        verify(studentRepository, never()).save(any());
    }
}
