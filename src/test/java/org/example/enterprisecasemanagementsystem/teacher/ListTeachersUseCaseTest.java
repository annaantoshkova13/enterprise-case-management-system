package org.example.enterprisecasemanagementsystem.teacher;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ListTeachersUseCaseTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private ListTeachersUseCase listTeachersUseCase;

    @Test
    void shouldReturnAllTeachers() {
        Teacher teacher1 = new Teacher("John", "Doe", "Computer Science", null);
        Teacher teacher2 = new Teacher("Jane", "Smith", "Mathematics", null);
        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

        when(teacherRepository.findAll()).thenReturn(teachers);

        List<Teacher> result = listTeachersUseCase.execute();

        assertNotNull(result);
        assertEquals(2, result.size());
        verify(teacherRepository).findAll();
    }

    @Test
    void shouldReturnEmptyList_WhenNoTeachers() {
        when(teacherRepository.findAll()).thenReturn(List.of());

        List<Teacher> result = listTeachersUseCase.execute();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(teacherRepository).findAll();
    }
}
