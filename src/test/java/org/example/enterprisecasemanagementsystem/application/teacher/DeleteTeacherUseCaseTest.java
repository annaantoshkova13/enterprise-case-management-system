package org.example.enterprisecasemanagementsystem.application.teacher;

import org.example.enterprisecasemanagementsystem.application.teacher.DeleteTeacherUseCase;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class DeleteTeacherUseCaseTest {

    @Mock
    private TeacherRepository teacherRepository;

    @InjectMocks
    private DeleteTeacherUseCase deleteTeacherUseCase;

    @Test
    void shouldDeleteTeacher() {
        deleteTeacherUseCase.execute(1L);

        verify(teacherRepository).deleteById(1L);
    }
}
