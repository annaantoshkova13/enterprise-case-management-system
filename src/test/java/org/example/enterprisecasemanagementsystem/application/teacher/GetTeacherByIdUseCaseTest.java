package org.example.enterprisecasemanagementsystem.application.teacher;

import org.example.enterprisecasemanagementsystem.application.teacher.GetTeacherByIdUseCase;
import org.example.enterprisecasemanagementsystem.domain.Teacher;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetTeacherByIdUseCaseTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private GetTeacherByIdUseCase getTeacherByIdUseCase;

    @Test
    void shouldReturnTeacher_WhenTeacherExists() {
        Teacher teacher = new Teacher("John", "Doe", "Computer Science", null);
        teacher.setId(1L);

        when(teacherRepository.findById(1L)).thenReturn(Optional.of(teacher));

        Teacher result = getTeacherByIdUseCase.execute(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John", result.getFirstName());
        verify(teacherRepository).findById(1L);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenTeacherNotFound() {
        when(teacherRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> getTeacherByIdUseCase.execute(999L)
        );

        assertEquals("Teacher not found with id: '999'", ex.getMessage());
        verify(teacherRepository).findById(999L);
    }
}
