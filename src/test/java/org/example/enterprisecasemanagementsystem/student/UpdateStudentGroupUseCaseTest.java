package org.example.enterprisecasemanagementsystem.student;

import org.example.enterprisecasemanagementsystem.exception.ResourceNotFoundException;
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
public class UpdateStudentGroupUseCaseTest {
    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private UpdateStudentGroupUseCase updateStudentGroupUseCase;

    private Student mockStudent;

    @BeforeEach
    void setUp() {
        mockStudent = new Student("Alice", "Smith", "CS-101", null);
        mockStudent.setId(1L);
    }

    @Test
    void shouldUpdateGroup_WhenStudentExists() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(mockStudent));
        when(studentRepository.save(any(Student.class))).thenReturn(mockStudent);

        Student result = updateStudentGroupUseCase.execute(1L, "CS-102");

        assertNotNull(result);
        assertEquals("CS-102", result.getGroupName());
        verify(studentRepository).findById(1L);
        verify(studentRepository).save(mockStudent);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenStudentNotFound() {
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> updateStudentGroupUseCase.execute(999L, "CS-102")
        );

        assertEquals("Student not found with id: '999'", ex.getMessage());
        verify(studentRepository).findById(999L);
        verify(studentRepository, never()).save(any());
    }

    @Test
    void shouldThrowIllegalArgumentException_WhenGroupIsEmpty() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(mockStudent));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> updateStudentGroupUseCase.execute(1L, "")
        );

        assertEquals("Group cannot be empty", ex.getMessage());
        verify(studentRepository).findById(1L);
        verify(studentRepository, never()).save(any());
    }

    @Test
    void shouldThrowIllegalArgumentException_WhenGroupIsNull() {
        when(studentRepository.findById(1L)).thenReturn(Optional.of(mockStudent));


        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> updateStudentGroupUseCase.execute(1L, null)
        );

        assertEquals("Group cannot be empty", ex.getMessage());
        verify(studentRepository).findById(1L);
        verify(studentRepository, never()).save(any());
    }
}
