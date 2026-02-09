package org.example.enterprisecasemanagementsystem.application.teacher;

import org.example.enterprisecasemanagementsystem.application.teacher.UpdateTeacherDepartmentUseCase;
import org.example.enterprisecasemanagementsystem.domain.Teacher;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.TeacherRepository;
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
public class UpdateTeacherDepartmentUseCaseTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private UpdateTeacherDepartmentUseCase updateTeacherDepartmentUseCase;

    private Teacher mockTeacher;

    @BeforeEach
    void setUp() {
        mockTeacher = new Teacher("John", "Doe", "Computer Science", null);
        mockTeacher.setId(1L);
    }

    @Test
    void shouldUpdateDepartment_WhenTeacherExists() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(mockTeacher));
        when(teacherRepository.save(any(Teacher.class))).thenReturn(mockTeacher);

        Teacher result = updateTeacherDepartmentUseCase.execute(1L, "Mathematics");

        assertNotNull(result);
        assertEquals("Mathematics", result.getDepartment());
        verify(teacherRepository).findById(1L);
        verify(teacherRepository).save(mockTeacher);
    }

    @Test
    void shouldThrowResourceNotFoundException_WhenTeacherNotFound() {
        when(teacherRepository.findById(999L)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class,
                () -> updateTeacherDepartmentUseCase.execute(999L, "Mathematics")
        );

        assertEquals("Teacher not found with id: '999'", ex.getMessage());
        verify(teacherRepository).findById(999L);
        verify(teacherRepository, never()).save(any());
    }

    @Test
    void shouldThrowIllegalArgumentException_WhenDepartmentIsEmpty() {
        when(teacherRepository.findById(1L)).thenReturn(Optional.of(mockTeacher));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class,
                () -> updateTeacherDepartmentUseCase.execute(1L, "")
        );

        assertEquals("Department cannot be empty", ex.getMessage());
        verify(teacherRepository).findById(1L);
        verify(teacherRepository, never()).save(any());
    }
}
