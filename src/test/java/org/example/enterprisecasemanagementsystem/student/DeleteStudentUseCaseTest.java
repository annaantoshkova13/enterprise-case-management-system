package org.example.enterprisecasemanagementsystem.student;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DeleteStudentUseCaseTest {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private DeleteStudentUseCase deleteStudentUseCase;

    @Test
    void shouldDeleteStudent() {
        deleteStudentUseCase.execute(1L);

        verify(studentRepository).deleteById(1L);
    }
}
