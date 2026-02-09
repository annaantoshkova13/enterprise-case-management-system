package org.example.enterprisecasemanagementsystem.application.student;

import org.example.enterprisecasemanagementsystem.application.student.ListStudentsUseCase;
import org.example.enterprisecasemanagementsystem.domain.Student;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.StudentRepository;
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
class ListStudentsUseCaseTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private ListStudentsUseCase listStudentsUseCase;

    @Test
    void shouldReturnAllStudents() {
        Student student1 = new Student("Alice", "Smith", "CS-101", null);
        Student student2 = new Student("Bob", "Johnson", "CS-102", null);
        List<Student> students = Arrays.asList(student1, student2);

        when(studentRepository.findAll()).thenReturn(students);

        List<Student> result = listStudentsUseCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(studentRepository).findAll();
    }

    @Test
    void shouldReturnEmptyList_WhenNoStudents() {
        when(studentRepository.findAll()).thenReturn(List.of());

        List<Student> result = listStudentsUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(studentRepository).findAll();
    }
}
