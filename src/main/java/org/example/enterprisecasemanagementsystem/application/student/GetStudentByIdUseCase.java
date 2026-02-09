package org.example.enterprisecasemanagementsystem.application.student;

import org.example.enterprisecasemanagementsystem.domain.Student;
import org.example.enterprisecasemanagementsystem.infrastructure.exception.ResourceNotFoundException;
import org.example.enterprisecasemanagementsystem.infrastructure.persistence.repository.StudentRepository;

public class GetStudentByIdUseCase {

    private final StudentRepository studentRepository;

    public GetStudentByIdUseCase(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student execute(Long id) {
        return studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Student", "id", id));
    }
}
