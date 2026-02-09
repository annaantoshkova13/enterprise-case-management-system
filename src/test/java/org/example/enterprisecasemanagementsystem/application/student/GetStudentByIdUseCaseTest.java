package org.example.enterprisecasemanagementsystem.application.student;

import org.example.enterprisecasemanagementsystem.application.student.GetStudentByIdUseCase;
import org.example.enterprisecasemanagementsystem.domain.Student;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.StudentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GetStudentByIdUseCaseTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private GetStudentByIdUseCase getStudentByIdUseCase;

    @Test
    void shouldReturnStudent_WhenStudentExists() {
        Student student = new Student("Alice", "Smith", "CS-101", null);
        student.setId(1L);

        when(studentRepository.findById(1L)).thenReturn(Optional.of(student));

        Student result = getStudentByIdUseCase.execute(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Alice", result.getFirstName());
        verify(studentRepository).findById(1L);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenStudentNotFound() {
        when(studentRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> getStudentByIdUseCase.execute(999L)
        );

        assertEquals("Student not found with id: '999'", ex.getMessage());
        verify(studentRepository).findById(999L);
    }
}